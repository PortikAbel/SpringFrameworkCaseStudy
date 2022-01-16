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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/api/regions")
@CrossOrigin(
        origins = "http://localhost:3000",
        exposedHeaders = "X-Total-Count"
)
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
    public ResponseEntity<Collection<RegionReducedDto>> findPaginated(
            @PageableDefault(size = 5)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                    Pageable pageable) {
        Page<RegionReducedDto> page = regionDao.findAll(pageable).map(regionMapper::modelToReducedDto);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(page.getTotalElements()))
                .body(page.getContent());
    }

    @GetMapping("/{regionId}")
    public RegionDetailsDto findRegionById(@PathVariable("regionId") Long regionId) {
        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        Region region = regionDao.findById(regionId).orElse(null);
        assert region != null;

        return regionMapper.modelToDetailsDto(region);
    }

    @GetMapping("/{regionId}/tours")
    public ResponseEntity<Collection<TourReducedDto>> findToursOfRegion(
            @PathVariable("regionId") Long regionId,
            @PageableDefault(size = 5)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                    Pageable pageable) {
        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        Region region = regionDao.findById(regionId).orElse(null);
        assert region != null;

        Page<TourReducedDto> page = tourDao.findByRegion(region, pageable).map(tourMapper::modelToReducedDto);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(page.getTotalElements()))
                .body(page.getContent());
    }

    @GetMapping("/{regionId}/refuges")
    public ResponseEntity<Collection<RefugeReducedDto>> findRefugesOfRegion(
            @PathVariable("regionId") Long regionId,
            @PageableDefault(size = 5)
            @SortDefault(sort = "id", direction = Sort.Direction.ASC)
                    Pageable pageable
    ) {
        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        Region region = regionDao.findById(regionId).orElse(null);
        assert region != null;

        Page<RefugeReducedDto> page = refugeDao.findByRegion(region, pageable).map(refugeMapper::modelToReducedDto);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(page.getTotalElements()))
                .body(page.getContent());
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

        if (!regionDao.existsById(regionId)) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        Tour tour = tourMapper.addToRegionDtoToModel(tourAddToRegionDto);
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
    }
}
