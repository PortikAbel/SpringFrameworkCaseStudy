package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.RegionRepository;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.*;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.RegionMapper;
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

    @PostMapping
    public ResponseEntity<Mono<RegionDetailsDto>> createRegion(
            @RequestBody @Valid RegionCreationDto regionCreationDto) {
        Mono<Region> region = regionDao.save(regionMapper.creationDtoToModel(regionCreationDto));
        URI createUri = URI.create("/api/tours/" + region.map(BaseEntity::getId));
        return ResponseEntity.created(createUri).body(region.map(regionMapper::modelToDetailsDto));
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
}
