package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dto.incoming.TourAddToRegionDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.TourDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.TourReducedDto;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.TourMapper;
import edu.bbte.idde.paim1949.backend.model.Region;
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
@RequestMapping("/api/regions")
public class ToursOfRegionController {

    @Autowired
    RegionDao regionDao;

    @Autowired
    TourMapper tourMapper;

    @GetMapping("/{regionId}/tours")
    public Collection<TourReducedDto> findToursOfRegion(@PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        return tourMapper.modelsToReducedDtos(region.getTours());
    }

    @PostMapping("/{regionId}/tours")
    public ResponseEntity<TourDetailsDto> addTourToRegion(
            @RequestBody @Valid TourAddToRegionDto tourAddToRegionDto,
            @PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        Tour tour = tourMapper.addToRegionDtoToModel(tourAddToRegionDto);
        tour.setRegion(region);
        region.getTours().add(tour);
        regionDao.save(region);
        tour = region.getTours().stream()
                .reduce((first, second) -> second)
                .orElse(null);
        assert tour != null;
        URI createUri = URI.create("/api/tours/" + tour.getId());
        return ResponseEntity.created(createUri).body(tourMapper.modelToDetailsDto(tour));
    }

    @DeleteMapping("/{regionId}/tours/{tourId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTourOfRegion(@PathVariable("regionId") Long regionId, @PathVariable("tourId") Long tourId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        if (!region.getTours().removeIf(tour -> tour.getId().equals(tourId))) {
            throw new NotFoundException("Tour with id " + tourId + " not found");
        }
        regionDao.save(region);
    }

}
