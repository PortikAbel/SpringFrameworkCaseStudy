package edu.bbte.idde.paim1949.backend;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class Main {
    @Autowired
    private TourDao tourDao;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            log.info("Hello world!");
            log.info("Populate tours.");
            populateTours();
            log.info("Get all tours: {}", tourDao.findAll());
        };
    }

    private void populateTours() {
        Tour tour1 = new Tour(12.3f, 787, Tour.SignShape.CIRCLE, Tour.SignColour.RED, 1, 1L);
        Tour tour2 = new Tour(26.4f, 1733, Tour.SignShape.TRIANGLE, Tour.SignColour.YELLOW, 2, 2L);
        Tour tour3 = new Tour(23.1f, 1587, Tour.SignShape.LINE, Tour.SignColour.RED, 2, 2L);

        tourDao.create(tour1);
        tourDao.create(tour2);
        tourDao.create(tour3);
    }
}
