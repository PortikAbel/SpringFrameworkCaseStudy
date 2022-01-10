package edu.bbte.idde.paim1949.backend.dao.jdbc;

import edu.bbte.idde.paim1949.backend.model.Tour;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class JavaTypeToSqlType {

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

    public static String getSqlType(Class<?> javaType) {
        return TYPE_TO_SQL_TYPE.get(javaType);
    }
}
