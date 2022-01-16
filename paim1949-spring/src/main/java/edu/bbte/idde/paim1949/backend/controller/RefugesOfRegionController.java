package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.RegionDao;
import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeAddToRegionDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RefugeDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RefugeReducedDto;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.RefugeMapper;
import edu.bbte.idde.paim1949.backend.model.Refuge;
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
public class RefugesOfRegionController {

    @Autowired
    RegionDao regionDao;

    @Autowired
    RefugeMapper refugeMapper;

    @GetMapping("/{regionId}/refuges")
    public Collection<RefugeReducedDto> findRefugesOfRegion(@PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        return refugeMapper.modelsToReducedDtos(region.getRefuges());
    }

    @PostMapping("/{regionId}/refuges")
    public ResponseEntity<RefugeDetailsDto> addRefugeToRegion(
            @RequestBody @Valid RefugeAddToRegionDto refugeAddToRegionDto,
            @PathVariable("regionId") Long regionId) {
        Region region = regionDao.findById(regionId)
                .orElseThrow(() -> new NotFoundException("Region with id " + regionId + " not found"));
        Refuge refuge = refugeMapper.addToRegionDtoToModel(refugeAddToRegionDto);
        refuge.setRegion(region);
        region.getRefuges().add(refuge);
        regionDao.save(region);
        refuge = region.getRefuges().stream()
                .reduce((first, second) -> second)
                .orElse(null);
        assert refuge != null;
        URI createUri = URI.create("/api/refuges/" + refuge.getId());
        return ResponseEntity.created(createUri).body(refugeMapper.modelToDetailsDto(refuge));
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
