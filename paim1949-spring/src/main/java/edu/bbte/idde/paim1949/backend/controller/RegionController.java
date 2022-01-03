package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RegionDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RegionReducedDto;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.RegionMapper;
import edu.bbte.idde.paim1949.backend.model.Region;
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
    RegionMapper regionMapper;
    @Autowired
    RegionDao regionDao;

    @GetMapping
    public Collection<RegionReducedDto> findAllRegions() {
        return regionMapper.modelsToReducedDtos(regionDao.findAll());
    }

    @GetMapping("/{regionId}")
    public RegionDetailsDto findRegionById(@PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId).orElse(null);

        if (region == null) {
            throw new NotFoundException("Region with id " + regionId + " not found");
        }
        return regionMapper.modelToDetailsDto(region);
    }

    @PostMapping
    public ResponseEntity<RegionDetailsDto> createRegion(@RequestBody @Valid RegionCreationDto regionCreationDto) {
        Region region = regionDao.save(regionMapper.creationDtoToModel(regionCreationDto));
        URI createUri = URI.create("/api/regions/" + region.getId());
        return ResponseEntity.created(createUri).body(regionMapper.modelToDetailsDto(region));
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
}
