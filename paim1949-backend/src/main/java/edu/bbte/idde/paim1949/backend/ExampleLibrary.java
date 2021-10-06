package edu.bbte.idde.paim1949.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class ExampleLibrary {

    public static final Logger LOG = LoggerFactory.getLogger(ExampleLibrary.class);

    public void logHello() throws IOException {
        InputStream propsStream = ExampleLibrary.class.getResourceAsStream("/hello.properties");
        Properties props = new Properties();
        props.load(propsStream);
        LOG.info("Hello " + props.get("name"));
    }
}