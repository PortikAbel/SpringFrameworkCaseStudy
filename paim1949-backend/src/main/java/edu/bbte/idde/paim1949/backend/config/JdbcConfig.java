package edu.bbte.idde.paim1949.backend.config;

import lombok.Data;

@Data
public class JdbcConfig {
    private String url;
    private String user;
    private String password;
    private Integer poolSize = 1;
}
