package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.model.Refuge;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
public class RefugeJdbcDao extends AbstractJdbcDao<Refuge> implements RefugeDao {
    public RefugeJdbcDao() {
        super(Refuge.class);
    }

    @Override
    public Collection<Refuge> findByRegionId(Long regionId) {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(modelClass.getSimpleName());
        selector.append(" WHERE regionId=").append(regionId);
        log.info("Built select statement: {}", selector);

        Collection<Refuge> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selector.toString());
            while (resultSet.next()) {
                Refuge selectedModel = modelClass.getDeclaredConstructor().newInstance();
                selectedModel.setId(resultSet.getLong("id"));
                for (Field field: fields) {
                    Object attribute = field.getType().isEnum() ?
                            resultSet.getString(field.getName()) :
                            resultSet.getObject(field.getName(), field.getType());
                    String setterName = "set"
                            + field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                            + field.getName().substring(1);
                    Method setter = modelClass.getDeclaredMethod(setterName,
                            field.getType().isEnum() ? String.class : field.getType());
                    setter.invoke(selectedModel, attribute);
                }
                result.add(selectedModel);
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
}
