package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.exception.DatabaseException;
import edu.bbte.idde.paim1949.backend.exception.ReflectionException;
import edu.bbte.idde.paim1949.backend.model.Refuge;
import edu.bbte.idde.paim1949.backend.model.Region;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

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
@Repository
@Profile("jdbc")
@DependsOn("regionJdbcDao")
public class RefugeJdbcDao extends AbstractJdbcDao<Refuge> implements RefugeDao {
    public RefugeJdbcDao() {
        super(Refuge.class);
    }

    @Override
    public Collection<Refuge> findByRegion(Region region) {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(modelClass.getSimpleName());
        selector.append(" WHERE region=").append(region.getId());
        log.info("Built select statement: {}", selector);

        Collection<Refuge> result = new ArrayList<>();
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
}
