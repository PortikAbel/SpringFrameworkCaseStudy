package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.annotation.RefByMany;
import edu.bbte.idde.paim1949.backend.annotation.RefToOne;
import edu.bbte.idde.paim1949.backend.dao.Dao;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public abstract class AbstractJdbcDao<T extends BaseEntity> implements Dao<T> {
    protected final ConnectionPool connectionPool;
    protected final Class<T> modelClass;
    protected final String tableName;
    protected final List<Field> fields;
    private static final JdbcDaoFactory JDBC_DAO_FACTORY = new JdbcDaoFactory();

    protected AbstractJdbcDao(Class<T> modelClass) {
        this.modelClass = modelClass;
        this.tableName = modelClass.getSimpleName().toLowerCase(Locale.ROOT);
        fields = Arrays.asList(modelClass.getDeclaredFields());
        connectionPool = ConnectionPool.getInstance();
        createTable();
    }

    private void createTable() {
        List<String> alterTables = new ArrayList<>();
        StringBuilder creator = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        creator.append(tableName).append('(');
        for (Field field: fields) {
            RefByMany refByMany = field.getAnnotation(RefByMany.class);
            if (refByMany == null) {
                RefToOne refToOne = field.getAnnotation(RefToOne.class);
                creator.append(field.getName())
                        .append(' ')
                        .append(refToOne == null
                                ? JavaTypeToSqlType.getSqlType(field.getType())
                                : JavaTypeToSqlType.getSqlType(Long.class))
                        .append(',');
                if (refToOne != null) {
                    creator.append(" FOREIGN KEY (")
                            .append(field.getName())
                            .append(") REFERENCES ")
                            .append(Objects.equals(refToOne.refTableName(), "")
                                    ? field.getType().getSimpleName().toLowerCase(Locale.ROOT)
                                    : refToOne.refTableName())
                            .append('(')
                            .append(refToOne.refColumnName())
                            .append("),");
                }
            } else if (Objects.equals(refByMany.refBy(), "")) {
                ParameterizedType collectionType = (ParameterizedType) field.getGenericType();
                Class<?> collectionClass = (Class<?>) collectionType.getActualTypeArguments()[0];
                String refTableName = collectionClass.getSimpleName().toLowerCase(Locale.ROOT);

                String alter = "ALTER TABLE " + refTableName + " ADD COLUMN "
                        + refTableName + '_' + tableName + ' ' + JavaTypeToSqlType.getSqlType(Long.class)
                        + ", ADD FOREIGN KEY (" + refTableName + '_' + tableName
                        + ") REFERENCES " + tableName + "(id)";
                alterTables.add(alter);
            }
        }
        creator.append("id BIGINT PRIMARY KEY AUTO_INCREMENT)");

        log.info("Executing table creation script '{}' with alter tables {}", creator, alterTables);

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            Statement createStatement = connection.createStatement();
            createStatement.addBatch(creator.toString());
            for (String alter: alterTables) {
                createStatement.addBatch(alter);
            }
            createStatement.executeBatch();
        } catch (JdbcException e) {
            log.error("Could not connect to database");
        } catch (SQLException e) {
            log.error("Could not create table {}: {}", tableName, e.toString());
        } finally {
            connectionPool.returnConnection(connection);
        }
    }

    @Override
    public Collection<T> findAll() {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .filter(field -> field.getAnnotation(RefByMany.class) == null)
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(tableName);
        log.info("Built select statement: {}", selector);

        Collection<T> result = new ArrayList<>();
        Connection connection = null;

        try {
            connection = connectionPool.getConnection();
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selector.toString());
            while (resultSet.next()) {
                result.add(getFromResultSet(resultSet));
            }
        } catch (JdbcException e) {
            log.error("Could not connect to database");
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException e) {
            log.error("Could not instantiate from model class");
        } finally {
            connectionPool.returnConnection(connection);
        }
        return result;
    }

    @Override
    public T findById(Long id) {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .filter(field -> field.getAnnotation(RefByMany.class) == null)
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(tableName);
        selector.append(" WHERE id=").append(id);
        log.info("Built select statement: {}", selector);
        T selectedModel = null;
        Connection connection = null;

        try {
            connection = connectionPool.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selector.toString());

            if (resultSet.next()) {
                selectedModel = getFromResultSet(resultSet);
            }
        } catch (JdbcException e) {
            log.error("Could not connect to database");
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException e) {
            log.error("Could not instantiate from model class");
        } finally {
            connectionPool.returnConnection(connection);
        }
        return selectedModel;
    }

    protected T getFromResultSet(ResultSet resultSet)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException, SQLException {
        T selectedModel = modelClass.getDeclaredConstructor().newInstance();
        selectedModel.setId(resultSet.getLong("id"));
        for (Field field: fields) {
            Object attribute = null;
            RefByMany refByMany = field.getAnnotation(RefByMany.class);
            if (refByMany == null) {
                RefToOne refToOne = field.getAnnotation(RefToOne.class);
                if (refToOne == null) {
                    attribute = field.getType().isEnum()
                            ? resultSet.getString(field.getName())
                            : resultSet.getObject(field.getName(), field.getType());
                } else if (refToOne.eagerFetch()) {
                    String refEntityName = Objects.equals(refToOne.refTableName(), "")
                            ? field.getType().getSimpleName().toLowerCase(Locale.ROOT)
                            : refToOne.refTableName();
                    Method daoGetter = JdbcDaoFactory.class.getDeclaredMethod(
                            "get" + refEntityName.substring(0, 1).toUpperCase(Locale.ROOT)
                                    + refEntityName.substring(1) + "Dao");
                    Dao dao = (Dao) daoGetter.invoke(JDBC_DAO_FACTORY);
                    attribute = dao.findById(resultSet.getLong(field.getName()));
                }
                String setterName = "set"
                        + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                        + field.getName().substring(1);
                Method setter = modelClass.getDeclaredMethod(setterName,
                        field.getType().isEnum() ? String.class : field.getType());
                setter.invoke(selectedModel, attribute);
            }
        }
        return selectedModel;
    }

    @Override
    public T create(T value) {
        StringBuilder inserter = new StringBuilder("INSERT INTO ")
                .append(tableName)
                .append('(');
        inserter.append(fields.stream()
                .filter(field -> field.getAnnotation(RefByMany.class) == null)
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        inserter.append(") VALUES (")
                .append(IntStream.range(0, (int) fields.stream()
                                .filter(field -> field.getAnnotation(RefByMany.class) == null).count())
                        .mapToObj(i -> "?")
                        .collect(Collectors.joining(",")));
        inserter.append(')');
        log.info("Built insert statement '{}'", inserter);

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement insertStatement = connection.prepareStatement(inserter.toString());
            int i = 1;
            for (Field field : fields.stream()
                    .filter(field -> field.getAnnotation(RefByMany.class) == null)
                    .collect(Collectors.toList())) {
                Method getter = modelClass.getDeclaredMethod("get"
                        + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                        + field.getName().substring(1));
                Object getterResult = getter.invoke(value);
                if (field.getAnnotation(RefToOne.class) == null) {
                    insertStatement.setObject(i++, getterResult);
                } else if (getterResult == null) {
                    insertStatement.setObject(i++, null);
                } else {
                    insertStatement.setLong(i++, ((BaseEntity)getterResult).getId());
                }
            }
            log.info("Executing statement");
            insertStatement.executeUpdate();
        } catch (JdbcException e) {
            log.error("Could not connect to database");
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Could not instantiate from model class");
        } finally {
            connectionPool.returnConnection(connection);
        }
        return value;
    }

    @Override
    public T update(Long id, T value) {
        StringBuilder updater = new StringBuilder("UPDATE ")
                .append(tableName)
                .append(" SET ");
        updater.append(fields.stream()
                .filter(field -> field.getAnnotation(RefByMany.class) == null)
                .map(field -> field.getName() + "=?")
                .collect(Collectors.joining(",")));
        updater.append(" WHERE id=?");
        log.info("Built update statement '{}'", updater);

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement updateStatement = connection.prepareStatement(updater.toString());
            int i = 1;
            for (Field field : fields.stream()
                    .filter(field -> field.getAnnotation(RefByMany.class) == null)
                    .collect(Collectors.toList())) {

                Method getter = modelClass.getDeclaredMethod("get"
                        + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                        + field.getName().substring(1));
                Object getterResult = getter.invoke(value);
                if (field.getAnnotation(RefToOne.class) == null) {
                    updateStatement.setObject(i++, getterResult);
                } else if (getterResult == null) {
                    updateStatement.setObject(i++, null);
                } else {
                    updateStatement.setLong(i++, ((BaseEntity)getterResult).getId());
                }
            }
            updateStatement.setLong(i, id);
            log.info("Executing statement");
            updateStatement.executeUpdate();
        } catch (JdbcException e) {
            log.error("Could not connect to database");
        } catch (SQLException e) {
            log.error("SQL execution failed.");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Could not instantiate from model class");
        } finally {
            connectionPool.returnConnection(connection);
        }
        return value;
    }

    @Override
    public T delete(Long id) {
        StringBuilder delete = new StringBuilder("DELETE FROM ");
        delete.append(tableName);
        delete.append(" WHERE id=?");
        log.info("Built delete statement '{}'", delete);

        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            PreparedStatement deleteStatement = connection.prepareStatement(delete.toString());
            deleteStatement.setLong(1, id);
            log.info("Executing statement");
            deleteStatement.executeUpdate();
        } catch (JdbcException e) {
            log.error("Could not connect to database");
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
        } finally {
            connectionPool.returnConnection(connection);
        }
        return null;
    }
}
