package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.RefugeRepository;
import edu.bbte.idde.paim1949.backend.dao.RegionRepository;
import edu.bbte.idde.paim1949.backend.dao.TourRepository;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.*;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.RefugeMapper;
import edu.bbte.idde.paim1949.backend.mapper.RegionMapper;
import edu.bbte.idde.paim1949.backend.mapper.TourMapper;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import edu.bbte.idde.paim1949.backend.model.Region;
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
@RequestMapping("/api/regions")
public class RegionController {

    @Autowired
    RegionRepository regionDao;

    @Autowired
    RegionMapper regionMapper;

    @GetMapping
    public Flux<RegionReducedDto> findAllRegions() {
        return regionDao.findAll().map(regionMapper::modelToReducedDto);
    }

    @GetMapping("/{regionId}")
    public Mono<RegionDetailsDto> findRegionById(@PathVariable("regionId") Long regionId) {
        Mono<Region> region = regionDao.findById(regionId);
        region.switchIfEmpty(Mono.error(new NotFoundException("Region with id " + regionId + " not found")));
        return region.map(regionMapper::modelToDetailsDto);
    }
    /*
    @GetMapping("/{regionId}/tours")
    public Flux<TourReducedDto> findToursOfRegion(@PathVariable("regionId") Long regionId) {
        Mono.just(regionId)
                .flatMap(regionDao::existsById)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NotFoundException("Region with id " + regionId + " not found"));
                    }
                    return Mono.just(true);
                });
        Mono<Region> region = regionDao.findById(regionId);

        return region.map(Region::getTours).map(tourMapper::modelToReducedDto);
    }

    @GetMapping("/{regionId}/refuges")
    public Collection<RefugeReducedDto> findRefugesOfRegion(@PathVariable("regionId") Long regionId) {
        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        Region region = regionDao.findById(regionId).orElse(null);
        assert region != null;

        return refugeMapper.modelsToReducedDtos(region.getRefuges());
    }
    */
    @PostMapping
    public ResponseEntity<Mono<RegionDetailsDto>> createRegion(
            @RequestBody @Valid RegionCreationDto regionCreationDto) {
        Mono<Region> region = regionDao.save(regionMapper.creationDtoToModel(regionCreationDto));
        URI createUri = URI.create("/api/tours/" + region.map(BaseEntity::getId));
        return ResponseEntity.created(createUri).body(region.map(regionMapper::modelToDetailsDto));
    }
    /*
    @PostMapping("/{regionId}/tours")
    public ResponseEntity<TourDetailsDto> addTourToRegion(
            @RequestBody @Valid TourAddToRegionDto tourAddToRegionDto,
            @PathVariable("regionId") Long regionId) {

        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        Tour tour = tourMapper.addToRegionDtoToModel(tourAddToRegionDto);
        Region region = regionDao.findById(regionId).orElse(null);
        assert region != null;
        tour.setRegion(region);
        tourDao.save(tour);
        URI createUri = URI.create("/api/tours/" + tour.getId());
        return ResponseEntity.created(createUri).body(tourMapper.modelToDetailsDto(tour));
    }

    @PostMapping("/{regionId}/refuges")
    public ResponseEntity<RefugeDetailsDto> addRefugeToRegion(
            @RequestBody @Valid RefugeAddToRegionDto refugeAddToRegionDto,
            @PathVariable("regionId") Long regionId) {

        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        Refuge refuge = refugeMapper.addToRegionDtoToModel(refugeAddToRegionDto);
        Region region = regionDao.findById(regionId).orElse(null);
        assert region != null;
        refuge.setRegion(region);
        refugeDao.save(refuge);
        URI createUri = URI.create("/api/refuges/" + refuge.getId());
        return ResponseEntity.created(createUri).body(refugeMapper.modelToDetailsDto(refuge));
    }
    */
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
    public ResponseEntity<Mono<RegionDetailsDto>> mergeRegion(
            @PathVariable("regionId") Long regionId,
            @RequestBody @Valid RegionUpdateDto regionUpdateDto) {
        Mono<Region> mergedRegion = Mono.just(regionId)
                .flatMap(regionDao::existsById)
                .flatMap(exists -> {
                    if (exists) {
                        Region regionUpdateModel = regionMapper.updateDtoToModel(regionUpdateDto);
                        regionUpdateModel.setId(regionId);
                        return regionDao.save(regionUpdateModel);
                    } else {
                        return Mono.error(new NotFoundException("Region with id " + regionId + " not found"));
                    }
                });
        URI mergeUri = URI.create("/api/regions/" + mergedRegion.map(BaseEntity::getId));
        return ResponseEntity.created(mergeUri).body(mergedRegion.map(regionMapper::modelToDetailsDto));
    }

    @DeleteMapping("/{regionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRegion(@PathVariable("regionId") Long regionId) {
        Mono.just(regionId)
                .flatMap(regionDao::existsById)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NotFoundException("Region with id " + regionId + " not found"));
                    }
                    return Mono.just(true);
                });
        regionDao.deleteById(regionId);
    }
    /*
    @DeleteMapping("/{regionId}/tours/{tourId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTourOfRegion(@PathVariable("regionId") Long regionId, @PathVariable("tourId") Long tourId) {
        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        if (!tourDao.existsById(tourId)) {
            throw new NotFoundException("Tour with id " + tourId + " not found");
        }
        Region region = regionDao.findById(regionId).orElse(null);
        Tour tour = tourDao.findById(tourId).orElse(null);
        assert region != null && tour != null;

        region.getTours().remove(tour);
        regionDao.save(region);
        tourDao.deleteById(tourId);
    }

    @DeleteMapping("/{regionId}/refuges/{refugeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRefugeOfRegion(@PathVariable("regionId") Long regionId, @PathVariable("refugeId") Long refugeId) {
        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        if (!refugeDao.existsById(refugeId)) {
            throw new NotFoundException("Refuge with id " + refugeId + " not found");
        }
        Region region = regionDao.findById(regionId).orElse(null);
        Refuge refuge = refugeDao.findById(refugeId).orElse(null);
        assert region != null && refuge != null;

        region.getRefuges().remove(refuge);
        regionDao.save(region);
        refugeDao.deleteById(refugeId);
    }*/
}
