package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.annotation.IgnoreColumn;
import edu.bbte.idde.paim1949.backend.annotation.RefToOne;
import edu.bbte.idde.paim1949.backend.dao.Dao;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Repository
@Profile("prod")
@RequiredArgsConstructor
public abstract class AbstractJdbcDao<T extends BaseEntity> implements Dao<T> {
    @Autowired
    protected DataSource dataSource;
    @NonNull
    protected Class<T> modelClass;
    protected List<Field> fields;
    private static final Map<Class<?>, String> TYPE_TO_SQL_TYPE;

    static {
        TYPE_TO_SQL_TYPE = new ConcurrentHashMap<>();
        TYPE_TO_SQL_TYPE.put(Integer.class, "INT");
        TYPE_TO_SQL_TYPE.put(Float.class, "FLOAT");
        TYPE_TO_SQL_TYPE.put(Double.class, "DOUBLE");
        TYPE_TO_SQL_TYPE.put(Long.class, "BIGINT");
        TYPE_TO_SQL_TYPE.put(Boolean.class, "BOOLEAN");
        TYPE_TO_SQL_TYPE.put(String.class, "VARCHAR(255)");
        TYPE_TO_SQL_TYPE.put(Date.class, "DATETIME");
        TYPE_TO_SQL_TYPE.put(Tour.SignShape.class,
                "ENUM('" + Arrays.stream(Tour.SignShape.values())
                        .map(Enum::name)
                        .collect(Collectors.joining("','")) + "')");
        TYPE_TO_SQL_TYPE.put(Tour.SignColour.class,
                "ENUM('" + Arrays.stream(Tour.SignColour.values())
                        .map(Enum::name)
                        .collect(Collectors.joining("','")) + "')");
    }

    @PostConstruct
    protected void init() {
        fields = Arrays.stream(modelClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(IgnoreColumn.class) == null)
                .collect(Collectors.toList());
        try (Connection connection = dataSource.getConnection()) {
            // creating table
            StringBuilder creator = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
            creator.append(modelClass.getSimpleName()).append('(');
            for (Field field: fields) {
                creator.append(field.getName())
                        .append(' ')
                        .append(TYPE_TO_SQL_TYPE.get(field.getType()))
                        .append(',');
                if (field.getAnnotation(RefToOne.class) != null) {
                    creator.append(" FOREIGN KEY (")
                            .append(field.getName())
                            .append(") REFERENCES ")
                            .append(field.getAnnotation(RefToOne.class).refTableName())
                            .append('(')
                            .append(field.getAnnotation(RefToOne.class).refColumnName())
                            .append("),");
                }
            }
            creator.append("id BIGINT PRIMARY KEY AUTO_INCREMENT)");

            log.info("Executing table creation script '{}'", creator);
            Statement createStatement = connection.createStatement();
            createStatement.executeUpdate(creator.toString());
        } catch (SQLException e) {
            log.error("Could not create table {}: {}", modelClass.getSimpleName(), e.toString());
        }
    }

    @Override
    public Collection<T> findAll() {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(modelClass.getSimpleName());
        log.info("Built select statement: {}", selector);

        Collection<T> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selector.toString());
            while (resultSet.next()) {
                T selectedModel = modelClass.getDeclaredConstructor().newInstance();
                selectedModel.setId(resultSet.getLong("id"));
                for (Field field: fields) {
                    Object attribute = field.getType().isEnum()
                            ? resultSet.getString(field.getName())
                            : resultSet.getObject(field.getName(), field.getType());
                    String setterName = "set"
                            + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                            + field.getName().substring(1);
                    Method setter = modelClass.getDeclaredMethod(setterName,
                            field.getType().isEnum() ? String.class : field.getType());
                    setter.invoke(selectedModel, attribute);
                }
                result.add(selectedModel);
            }
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException e) {
            log.error("Could not instantiate from model class");
        }
        return result;
    }

    @Override
    public T findById(Long id) {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(modelClass.getSimpleName());
        selector.append(" WHERE id=").append(id);
        log.info("Built select statement: {}", selector);
        T selectedModel = null;

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selector.toString());

            if (!resultSet.next()) {
                return null;
            }
            selectedModel = modelClass.getDeclaredConstructor().newInstance();
            selectedModel.setId(resultSet.getLong("id"));
            for (Field field: fields) {
                Object attribute = field.getType().isEnum()
                        ? resultSet.getString(field.getName())
                        : resultSet.getObject(field.getName(), field.getType());
                String setterName = "set"
                        + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                        + field.getName().substring(1);
                Method setter = modelClass.getDeclaredMethod(setterName,
                        field.getType().isEnum() ? String.class : field.getType());
                setter.invoke(selectedModel, attribute);
            }
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException e) {
            log.error("Could not instantiate from model class");
        }
        return selectedModel;
    }

    @Override
    public T create(T value) {
        StringBuilder inserter = new StringBuilder("INSERT INTO ")
                .append(modelClass.getSimpleName())
                .append('(');
        inserter.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        inserter.append(") VALUES (")
                .append(IntStream.range(0, fields.size())
                        .mapToObj(i -> "?")
                        .collect(Collectors.joining(",")));
        inserter.append(')');
        log.info("Built insert statement '{}'", inserter);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(inserter.toString());
            int i = 1;
            for (Field field : fields) {
                Method getter = modelClass.getDeclaredMethod("get"
                        + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                        + field.getName().substring(1));
                Object getterResult = getter.invoke(value);
                insertStatement.setObject(i++, getterResult);
            }
            log.info("Executing statement");
            insertStatement.executeUpdate();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
            if (!resultSet.next()) {
                return null;
            }
            value.setId(resultSet.getLong(1));
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Could not instantiate from model class");
        }
        return value;
    }

    @Override
    public T update(Long id, T value) {
        List<Field> fieldsToUpdate = fields.stream()
                .filter(field -> {
                    try {
                        Method getter = modelClass.getDeclaredMethod("get"
                                + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                                + field.getName().substring(1));
                        Object getterResult = getter.invoke(value);
                        return getterResult != null;
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        log.error("Could not instantiate from model class");
                    }
                    return false;
                }).collect(Collectors.toList());

        StringBuilder updater = new StringBuilder("UPDATE ")
                .append(modelClass.getSimpleName())
                .append(" SET ");
        updater.append(fieldsToUpdate.stream()
                .map(field -> field.getName() + "=?")
                .collect(Collectors.joining(",")));
        updater.append(" WHERE id=?");
        log.info("Built update statement '{}'", updater);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement updateStatement = connection.prepareStatement(updater.toString());
            int i = 1;
            for (Field field : fieldsToUpdate) {
                Method getter = modelClass.getDeclaredMethod("get"
                        + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                        + field.getName().substring(1));
                Object getterResult = getter.invoke(value);
                updateStatement.setObject(i++, getterResult);
            }
            updateStatement.setLong(i, id);
            log.info("Executing statement");
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL execution failed.");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Could not instantiate from model class");
        }
        return value;
    }

    @Override
    public boolean delete(Long id) {
        StringBuilder delete = new StringBuilder("DELETE FROM ");
        delete.append(modelClass.getSimpleName());
        delete.append(" WHERE id=?");
        log.info("Built delete statement '{}'", delete);

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement deleteStatement = connection.prepareStatement(delete.toString());
            deleteStatement.setLong(1, id);
            log.info("Executing statement");
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
            return false;
        }
        return true;
    }
}
