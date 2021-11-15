package edu.bbte.idde.paim1949.backend.config;

import lombok.Data;

@Data
public class Config {
    private String daoType;
    private String driverClass;
    private JdbcConfig connection = new JdbcConfig();
}
