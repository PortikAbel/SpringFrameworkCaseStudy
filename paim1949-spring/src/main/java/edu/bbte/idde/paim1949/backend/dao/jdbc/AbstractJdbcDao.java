package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.annotation.IgnoreColumn;
import edu.bbte.idde.paim1949.backend.annotation.RefToOne;
import edu.bbte.idde.paim1949.backend.dao.Dao;
import edu.bbte.idde.paim1949.backend.exception.DatabaseException;
import edu.bbte.idde.paim1949.backend.exception.ReflectionException;
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
@Profile("jdbc")
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
                        .append(field.getAnnotation(RefToOne.class) == null
                                ? TYPE_TO_SQL_TYPE.get(field.getType())
                                : TYPE_TO_SQL_TYPE.get(Long.class))
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
            throw new DatabaseException();
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
                result.add(getFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
            throw new DatabaseException();
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException e) {
            log.error("Could not instantiate from model class");
            throw new ReflectionException();
        }
        return result;
    }

    @Override
    public Optional<T> findById(Long id) {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(modelClass.getSimpleName());
        selector.append(" WHERE id=").append(id);
        log.info("Built select statement: {}", selector);
        T selectedModel;

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selector.toString());

            if (!resultSet.next()) {
                return Optional.empty();
            }

            selectedModel = getFromResultSet(resultSet);
        } catch (SQLException e) {
            log.error("SQL execution failed: {}", e.toString());
            throw new DatabaseException();
        } catch (InvocationTargetException
                | InstantiationException
                | IllegalAccessException
                | NoSuchMethodException e) {
            log.error("Could not instantiate from model class");
            throw new ReflectionException();
        }
        return Optional.of(selectedModel);
    }

    protected T getFromResultSet(ResultSet resultSet)
            throws NoSuchMethodException, InvocationTargetException, InstantiationException,
            IllegalAccessException, SQLException {
        T selectedModel = modelClass.getDeclaredConstructor().newInstance();
        selectedModel.setId(resultSet.getLong("id"));
        for (Field field: fields) {
            Object attribute;
            if (field.getAnnotation(RefToOne.class) == null) {
                attribute = field.getType().isEnum()
                        ? resultSet.getString(field.getName())
                        : resultSet.getObject(field.getName(), field.getType());
            } else {
                attribute = field.getType().getDeclaredConstructor().newInstance();
                Method idSetter = field.getType().getMethod("setId", Long.class);
                idSetter.invoke(attribute, resultSet.getLong(field.getName()));
            }
            String setterName = "set"
                    + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                    + field.getName().substring(1);
            Method setter = modelClass.getDeclaredMethod(setterName,
                    field.getType().isEnum() ? String.class : field.getType());
            setter.invoke(selectedModel, attribute);
        }
        return selectedModel;
    }

    @Override
    public T save(T value) {
        if (value.getId() == null) {
            return insert(value, false);
        }
        T oldValue = findById(value.getId()).orElse(null);
        if (oldValue == null) {
            return insert(value, true);
        }
        return update(oldValue, value);
    }

    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    @Override
    public void deleteById(Long id) {
        Optional<T> optionalT = findById(id);
        if (!optionalT.isPresent()) {
            return;
        }

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
            throw new DatabaseException();
        }
    }

    private T insert(T value, boolean idIsGiven) {
        StringBuilder inserter = new StringBuilder("INSERT INTO ")
                .append(modelClass.getSimpleName())
                .append('(');
        inserter.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        if (idIsGiven) {
            inserter.append(",id");
        }
        inserter.append(") VALUES (")
                .append(IntStream.range(0, fields.size())
                        .mapToObj(i -> "?")
                        .collect(Collectors.joining(",")));
        if (idIsGiven) {
            inserter.append(",?");
        }
        inserter.append(')');
        log.info("Built insert statement '{}'", inserter);
        return insertValue(inserter.toString(), value, idIsGiven);
    }

    private T insertValue(String inserter, T value, boolean withId) {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement insertStatement = connection.prepareStatement(inserter);
            int i = 1;
            for (Field field : fields) {
                Method getter = modelClass.getDeclaredMethod("get"
                        + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                        + field.getName().substring(1));
                Object getterResult = getter.invoke(value);
                if (field.getAnnotation(RefToOne.class) == null) {
                    insertStatement.setObject(i++, getterResult);
                } else {
                    log.debug("get ID of object {}", getterResult);
                    Method idGetter = field.getType().getMethod("getId");
                    log.debug("ID getter found {}.", idGetter.getName());
                    insertStatement.setObject(i++, idGetter.invoke(getterResult));
                    log.debug("ID set to insert statement.");
                }
            }
            if (withId) {
                insertStatement.setLong(i, value.getId());
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
            throw new DatabaseException();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Could not instantiate from model class");
            throw new ReflectionException();
        }

        return value;
    }

    private T update(T oldValue, T newValue) {
        List<Field> fieldsToUpdate = fields.stream()
                .filter(field -> {
                    try {
                        Method getter = modelClass.getDeclaredMethod("get"
                                + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                                + field.getName().substring(1));
                        Object getterResult = getter.invoke(newValue);
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
                String suffixGetterSetter = field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                        + field.getName().substring(1);
                Method getter = modelClass.getDeclaredMethod("get" + suffixGetterSetter);
                Object getterResult = getter.invoke(newValue);
                if (field.getAnnotation(RefToOne.class) == null) {
                    updateStatement.setObject(i++, getterResult);
                    Method setter = modelClass.getDeclaredMethod("set" + suffixGetterSetter,
                            field.getType().isEnum() ? String.class : field.getType());
                    setter.invoke(oldValue, getterResult);
                } else {
                    Method idGetter = field.getType().getDeclaredMethod("getId");
                    Long refId = (Long) idGetter.invoke(getterResult);
                    updateStatement.setLong(i++, refId);
                    Method idSetter = modelClass.getDeclaredMethod("setId", Long.class);
                    idSetter.invoke(oldValue, refId);
                }
            }
            updateStatement.setLong(i, oldValue.getId());
            log.info("Executing statement");
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL execution failed.");
            throw new DatabaseException();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Could not instantiate from model class");
            throw new ReflectionException();
        }
        return oldValue;
    }
}
