package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.model.Refuge;
import edu.bbte.idde.paim1949.backend.model.Region;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
public final class RefugeJdbcDao extends AbstractJdbcDao<Refuge> implements RefugeDao {

    private static class InstanceHolder {
        public static final RefugeJdbcDao INSTANCE = new RefugeJdbcDao();
    }

    public static RefugeJdbcDao getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private RefugeJdbcDao() {
        super(Refuge.class);
    }

    @Override
    public Collection<Refuge> findByRegion(Region region) {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(modelClass.getSimpleName());
        selector.append(" WHERE regionId=").append(region.getId());
        log.info("Built select statement: {}", selector);

        Collection<Refuge> result = new ArrayList<>();
        Connection connection = null;
        try {
            connection = connectionPool.getConnection();
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selector.toString());
            while (resultSet.next()) {
                result.add((Refuge) getFromResultSet(resultSet, modelClass, fields));
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
