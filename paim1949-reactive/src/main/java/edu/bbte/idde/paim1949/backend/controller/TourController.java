package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.TourRepository;
import edu.bbte.idde.paim1949.backend.dto.incoming.TourCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.TourUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.TourDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.TourReducedDto;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.TourMapper;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/api/tours")
public class TourController {
    @Autowired
    TourMapper tourMapper;
    @Autowired
    TourRepository tourDao;

    @GetMapping
    public Flux<TourReducedDto> findAllTours() {
        return tourDao.findAll().map(tourMapper::modelToReducedDto);
    }

    @GetMapping("/{tourId}")
    public Mono<TourDetailsDto> findTourById(@PathVariable("tourId") Long tourId) {
        Mono<Tour> tour = tourDao.findById(tourId);
        tour.switchIfEmpty(Mono.error(new NotFoundException("Tour with id " + tourId + " not found")));
        return tour.map(tourMapper::modelToDetailsDto);
    }

    @PostMapping
    public ResponseEntity<Mono<TourDetailsDto>> createTour(@RequestBody @Valid TourCreationDto tourCreationDto) {
        Mono<Tour> tour = tourDao.save(tourMapper.creationDtoToModel(tourCreationDto));
        URI createUri = URI.create("/api/tours/" + tour.map(BaseEntity::getId));
        return ResponseEntity.created(createUri).body(tour.map(tourMapper::modelToDetailsDto));
    }

    @PutMapping("/{tourId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTour(
            @PathVariable("tourId") Long tourId,
            @RequestBody @Valid TourCreationDto tourUpdateDto) {
        Tour tourUpdateModel = tourMapper.creationDtoToModel(tourUpdateDto);
        tourUpdateModel.setId(tourId);
        tourDao.save(tourUpdateModel);
    }

    @PatchMapping("/{tourId}")
    public ResponseEntity<Mono<TourDetailsDto>> mergeTour(
            @PathVariable("tourId") Long tourId,
            @RequestBody @Valid TourUpdateDto tourUpdateDto) {
        Mono<Tour> mergedTour = Mono.just(tourId)
                .flatMap(tourDao::existsById)
                .flatMap(exists -> {
                    if (exists) {
                        Tour tourUpdateModel = tourMapper.updateDtoToModel(tourUpdateDto);
                        tourUpdateModel.setId(tourId);
                        return tourDao.save(tourUpdateModel);
                    } else {
                        return Mono.error(new NotFoundException("Tour with id " + tourId + " not found"));
                    }
                });
        URI mergeUri = URI.create("/api/tours/" + mergedTour.map(BaseEntity::getId));
        return ResponseEntity.created(mergeUri).body(mergedTour.map(tourMapper::modelToDetailsDto));
    }

    @DeleteMapping("/{tourId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTour(@PathVariable("tourId") Long tourId) {
        Mono.just(tourId)
                .flatMap(tourDao::existsById)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NotFoundException("Tour with id " + tourId + " not found"));
                    }
                    return Mono.just(true);
                });
        return tourDao.deleteById(tourId);
    }
}
