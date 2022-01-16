package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.RefugeDao;
import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RefugeDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RefugeReducedDto;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.RefugeMapper;
import edu.bbte.idde.paim1949.backend.model.Refuge;
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
@RequestMapping("/api/refuges")
public class RefugeController {

    @Autowired
    RefugeMapper refugeMapper;

    @Autowired
    RefugeDao refugeDao;

    @GetMapping
    public Collection<RefugeReducedDto> findAllRefuges() {
        return refugeMapper.modelsToReducedDtos(refugeDao.findAll());
    }

    @GetMapping("/{refugeId}")
    public RefugeDetailsDto findRefugeById(@PathVariable("refugeId") Long refugeId) {
        Refuge refuge = refugeDao.findById(refugeId)
                .orElseThrow(() -> new NotFoundException("Refuge with id " + refugeId + " not found"));
        return refugeMapper.modelToDetailsDto(refuge);
    }

    @PostMapping
    public ResponseEntity<RefugeDetailsDto> createRefuge(@RequestBody @Valid RefugeCreationDto refugeCreationDto) {
        Refuge refuge = refugeDao.save(refugeMapper.creationDtoToModel(refugeCreationDto));
        URI createUri = URI.create("/api/refuges/" + refuge.getId());
        return ResponseEntity.created(createUri).body(refugeMapper.modelToDetailsDto(refuge));
    }

    @PutMapping("/{refugeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateRefuge(
            @PathVariable("refugeId") Long refugeId,
            @RequestBody @Valid RefugeCreationDto refugeUpdateDto) {
        Refuge refugeUpdateModel = refugeMapper.creationDtoToModel(refugeUpdateDto);
        refugeUpdateModel.setId(refugeId);
        refugeDao.save(refugeUpdateModel);
    }

    @PatchMapping("/{refugeId}")
    public ResponseEntity<RefugeDetailsDto> mergeRefuge(
            @PathVariable("refugeId") Long refugeId,
            @RequestBody @Valid RefugeUpdateDto refugeUpdateDto) {
        if (!refugeDao.existsById(refugeId)) {
            throw new NotFoundException("Refuge with id " + refugeId + " does not exists");
        }
        Refuge refugeUpdateModel = refugeMapper.updateDtoToModel(refugeUpdateDto);
        refugeUpdateModel.setId(refugeId);
        Refuge mergedRefuge = refugeDao.save(refugeUpdateModel);
        URI mergeUri = URI.create("/api/refuges/" + mergedRefuge.getId());
        return ResponseEntity.created(mergeUri).body(refugeMapper.modelToDetailsDto(mergedRefuge));
    }

    @DeleteMapping("/{refugeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRefuge(@PathVariable("refugeId") Long refugeId) {
        if (!refugeDao.existsById(refugeId)) {
            throw new NotFoundException("Refuge with id " + refugeId + " not found");
        }
        refugeDao.deleteById(refugeId);
    }
}
