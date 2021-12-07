package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
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
public class TourJdbcDao extends AbstractJdbcDao<Tour> implements TourDao {
    public TourJdbcDao() {
        super(Tour.class);
    }

    @PostConstruct
    protected void init() {
        super.init();
    }

    @Override
    public Collection<Tour> findByRegionId(Long regionId) {
        StringBuilder selector = new StringBuilder("SELECT ");
        selector.append(fields.stream()
                .map(Field::getName)
                .collect(Collectors.joining(",")));
        selector.append(",id FROM ").append(modelClass.getSimpleName());
        selector.append(" WHERE regionId=").append(regionId);
        log.info("Built select statement: {}", selector);

        Collection<Tour> result = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            Statement selectStatement = connection.createStatement();
            ResultSet resultSet = selectStatement.executeQuery(selector.toString());
            while (resultSet.next()) {
                Tour selectedModel = modelClass.getDeclaredConstructor().newInstance();
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
}
