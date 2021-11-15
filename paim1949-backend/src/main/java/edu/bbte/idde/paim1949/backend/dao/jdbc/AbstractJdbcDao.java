package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.Dao;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public abstract class AbstractJdbcDao<T extends BaseEntity> implements Dao<T> {
    private Connection connection;
    private final Class<T> modelClass;
    private final Field[] fields;

    public AbstractJdbcDao(Class<T> modelClass) {
        this.modelClass = modelClass;
        fields = modelClass.getDeclaredFields();
        try {
            connection = ConnectionPool.getInstance().getConnection();
        } catch (JdbcException e) {
            log.error("Could not connect to database");
        }
    }

    @Override
    public Collection<T> findAll() {
        StringBuilder selector = new StringBuilder("SELECT ");
        for (Field field: fields) {
            selector.append(field.getName()).append(",");
        }
        selector.append("id FROM ").append(modelClass.getSimpleName());
        Collection<T> result = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selector.toString());
            while (resultSet.next()) {
                T selectedModel = modelClass.getDeclaredConstructor().newInstance();
                selectedModel.setId(resultSet.getLong("id"));
                for (Field field: fields) {
                    Object attribute = resultSet.getObject(field.getName(), field.getType());
                    String setterName = "set"
                            + field.getName().substring(0, 1).toUpperCase()
                            + field.getName().substring(1);
                    Method setter = modelClass.getDeclaredMethod(setterName, field.getType());
                    setter.invoke(selectedModel, attribute);
                }
                result.add(selectedModel);
            }
        } catch (SQLException e) {
            log.error("SQL execution failed.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            log.error("Could not instantiate from model class");
        }
        return result;
    }

    @Override
    public T findById(Long id) {
        StringBuilder selector = new StringBuilder("SELECT ");
        for (Field field: fields) {
            selector.append(field.getName()).append(",");
        }
        selector.append("id FROM ").append(modelClass.getSimpleName());
        selector.append(" WHERE id=").append(id);
        T selectedModel = null;

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selector.toString());

            resultSet.next();
            selectedModel = modelClass.getDeclaredConstructor().newInstance();
            selectedModel.setId(resultSet.getLong("id"));
            for (Field field: fields) {
                Object attribute = resultSet.getObject(field.getName(), field.getType());
                String setterName = "set"
                        + field.getName().substring(0, 1).toUpperCase()
                        + field.getName().substring(1);
                Method setter = modelClass.getDeclaredMethod(setterName, field.getType());
                setter.invoke(selectedModel, attribute);
            }
        } catch (SQLException e) {
            log.error("SQL execution failed.");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            log.error("Could not instantiate from model class");
        }
        return selectedModel;
    }

    @Override
    public T create(T t) {
        StringBuilder inserter = new StringBuilder("INSERT INTO ")
                .append(modelClass.getSimpleName())
                .append(" (");
        for (Field field: fields) {
            inserter.append(field.getName()).append(",");
        }
        inserter.append("id) VALUES (")
                .append(IntStream.range(0, fields.length).mapToObj(i -> "?").collect(Collectors.joining(",")));

        log.info("Built insert statement '{}'", inserter);
        try (PreparedStatement insertStatement = connection.prepareStatement(inserter.toString())) {
            int i = 1;
            for (Field field : fields) {
                Method getter = modelClass.getDeclaredMethod("get"
                        + field.getName().substring(0, 1).toUpperCase()
                        + field.getName().substring(1));
                Object getterResult = getter.invoke(t);
                insertStatement.setObject(i++, getterResult);
            }
            insertStatement.setLong(i, t.getId());
            log.info("Executing statement");
            insertStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL execution failed.");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Could not instantiate from model class");
        }
        return t;
    }

    @Override
    public T update(Long id, T t) {
        StringBuilder updater = new StringBuilder("UPDATE ")
                .append(modelClass.getSimpleName())
                .append(" SET ");
        updater.append(Arrays.stream(fields)
                .map(field -> field.getName() + "=?")
                .collect(Collectors.joining(",")));
        updater.append(" WHERE id=?");
        log.info("Built update statement '{}'", updater);
        try (PreparedStatement updateStatement = connection.prepareStatement(updater.toString())) {
            int i = 1;
            for (Field field : fields) {
                Method getter = modelClass.getDeclaredMethod("get"
                        + field.getName().substring(0, 1).toUpperCase()
                        + field.getName().substring(1));
                Object getterResult = getter.invoke(t);
                updateStatement.setObject(i++, getterResult);
            }
            updateStatement.setLong(i, t.getId());
            log.info("Executing statement");
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL execution failed.");
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Could not instantiate from model class");
        }
        return t;
    }

    @Override
    public T delete(Long id) {
        StringBuilder delete = new StringBuilder("DELETE FROM ");
        delete.append(modelClass.getSimpleName());
        delete.append(" WHERE id=").append(id);

        log.info("Built delete statement '{}'", delete);
        try (PreparedStatement deleteStatement = connection.prepareStatement(delete.toString())) {
            deleteStatement.setLong(1, id);
            log.info("Executing statement");
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("SQL execution failed.");
        }
        return null;
    }
}
