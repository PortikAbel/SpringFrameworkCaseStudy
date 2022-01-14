package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dao.TourDao;
import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeAddToRegionDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.TourAddToRegionDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.*;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.RefugeMapper;
import edu.bbte.idde.paim1949.backend.mapper.RegionMapper;
import edu.bbte.idde.paim1949.backend.mapper.TourMapper;
import edu.bbte.idde.paim1949.backend.model.Refuge;
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
public class RegionController {

    @Autowired
    RegionDao regionDao;
    @Autowired
    TourDao tourDao;
    @Autowired
    RefugeDao refugeDao;

    @Autowired
    RegionMapper regionMapper;
    @Autowired
    TourMapper tourMapper;
    @Autowired
    RefugeMapper refugeMapper;

    @GetMapping
    public Collection<RegionReducedDto> findAllRegions() {
        return regionMapper.modelsToReducedDtos(regionDao.findAll());
    }

    @GetMapping("/{regionId}")
    public RegionDetailsDto findRegionById(@PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        return regionMapper.modelToDetailsDto(region);
    }

    @GetMapping("/{regionId}/tours")
    public Collection<TourReducedDto> findToursOfRegion(@PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        return tourMapper.modelsToReducedDtos(region.getTours());
    }

    @GetMapping("/{regionId}/refuges")
    public Collection<RefugeReducedDto> findRefugesOfRegion(@PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        return refugeMapper.modelsToReducedDtos(region.getRefuges());
    }

    @PostMapping
    public ResponseEntity<RegionDetailsDto> createRegion(@RequestBody @Valid RegionCreationDto regionCreationDto) {
        Region region = regionDao.save(regionMapper.creationDtoToModel(regionCreationDto));
        URI createUri = URI.create("/api/regions/" + region.getId());
        return ResponseEntity.created(createUri).body(regionMapper.modelToDetailsDto(region));
    }

    @PostMapping("/{regionId}/tours")
    public ResponseEntity<TourDetailsDto> addTourToRegion(
            @RequestBody @Valid TourAddToRegionDto tourAddToRegionDto,
            @PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        Tour tour = tourMapper.addToRegionDtoToModel(tourAddToRegionDto);
        region.getTours().add(tour);
        regionDao.save(region);
        URI createUri = URI.create("/api/tours/" + tour.getId());
        return ResponseEntity.created(createUri).body(tourMapper.modelToDetailsDto(tour));
    }

    @PostMapping("/{regionId}/refuges")
    public ResponseEntity<RefugeDetailsDto> addRefugeToRegion(
            @RequestBody @Valid RefugeAddToRegionDto refugeAddToRegionDto,
            @PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        Refuge refuge = refugeMapper.addToRegionDtoToModel(refugeAddToRegionDto);
        region.getRefuges().add(refuge);
        regionDao.save(region);
        URI createUri = URI.create("/api/refuges/" + refuge.getId());
        return ResponseEntity.created(createUri).body(refugeMapper.modelToDetailsDto(refuge));
    }

    @PutMapping("/{regionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRegion(
            @PathVariable("regionId") Long regionId,
            @RequestBody @Valid RegionCreationDto regionUpdateDto) {
        Region regionUpdateModel = regionMapper.creationDtoToModel(regionUpdateDto);
        regionUpdateModel.setId(regionId);
        regionDao.save(regionUpdateModel);
    }

    @PatchMapping("/{regionId}")
    public ResponseEntity<RegionDetailsDto> mergeRegion(
            @PathVariable("regionId") Long regionId,
            @RequestBody @Valid RegionUpdateDto regionUpdateDto) {
        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " does not exists");
        }
        Region regionUpdateModel = regionMapper.updateDtoToModel(regionUpdateDto);
        regionUpdateModel.setId(regionId);
        Region mergedRegion = regionDao.save(regionUpdateModel);
        URI mergeUri = URI.create("/api/regions/" + mergedRegion.getId());
        return ResponseEntity.created(mergeUri).body(regionMapper.modelToDetailsDto(mergedRegion));
    }

    @DeleteMapping("/{regionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegion(@PathVariable("regionId") Long regionId) {
        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        regionDao.deleteById(regionId);
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

    @DeleteMapping("/{regionId}/refuges/{refugeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRefugeOfRegion(@PathVariable("regionId") Long regionId, @PathVariable("refugeId") Long refugeId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        if (!region.getRefuges().removeIf(refuge -> refuge.getId().equals(refugeId))) {
            throw new NotFoundException("Refuge with id " + refugeId + " not found");
        }
        regionDao.save(region);
    }
}
