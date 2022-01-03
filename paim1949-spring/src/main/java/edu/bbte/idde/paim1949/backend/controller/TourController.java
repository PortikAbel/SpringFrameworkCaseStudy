package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.dto.incoming.TourCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.TourUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.TourDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.TourReducedDto;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.TourMapper;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/api/tours")
public class TourController {
    @Autowired
    TourMapper tourMapper;
    @Autowired
    TourDao tourDao;

    @GetMapping
    public Collection<TourReducedDto> findAllTours() {
        return tourMapper.modelsToReducedDtos(tourDao.findAll());
    }

    @GetMapping("/{tourId}")
    public TourDetailsDto findTourById(@PathVariable("tourId") Long tourId) {
        Tour tour = tourDao.findById(tourId).orElse(null);

        if (tour == null) {
            throw new NotFoundException("Tour with id " + tourId + " not found");
        }
        return tourMapper.modelToDetailsDto(tour);
    }

    @PostMapping
    public ResponseEntity<TourDetailsDto> createTour(@RequestBody @Valid TourCreationDto tourCreationDto) {
        Tour tour = tourDao.save(tourMapper.creationDtoToModel(tourCreationDto));
        URI createUri = URI.create("/api/tours/" + tour.getId());
        return ResponseEntity.created(createUri).body(tourMapper.modelToDetailsDto(tour));
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
    public ResponseEntity<TourDetailsDto> mergeTour(
            @PathVariable("tourId") Long tourId,
            @RequestBody @Valid TourUpdateDto tourUpdateDto) {
        if (!tourDao.existsById(tourId)) {
            throw new NotFoundException("Tour with id " + tourId + " does not exists");
        }
        Tour tourUpdateModel = tourMapper.updateDtoToModel(tourUpdateDto);
        tourUpdateModel.setId(tourId);
        Tour mergedTour = tourDao.save(tourUpdateModel);
        URI mergeUri = URI.create("/api/tours/" + mergedTour.getId());
        return ResponseEntity.created(mergeUri).body(tourMapper.modelToDetailsDto(mergedTour));
    }

    @DeleteMapping("/{tourId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTour(@PathVariable("tourId") Long tourId) {
        if (!tourDao.existsById(tourId)) {
            throw new NotFoundException("Tour with id " + tourId + " not found");
        }
        tourDao.deleteById(tourId);
    }
}
