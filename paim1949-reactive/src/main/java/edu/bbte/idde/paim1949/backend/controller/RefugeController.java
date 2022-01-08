package edu.bbte.idde.paim1949.backend.controller;

import edu.bbte.idde.paim1949.backend.dao.RefugeRepository;
import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RefugeDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RefugeReducedDto;
import edu.bbte.idde.paim1949.backend.exception.NotFoundException;
import edu.bbte.idde.paim1949.backend.mapper.RefugeMapper;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import edu.bbte.idde.paim1949.backend.model.Refuge;
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
@RequestMapping("/api/refuges")
public class RefugeController {

    @Autowired
    RefugeMapper refugeMapper;

    @Autowired
    RefugeRepository refugeDao;

    @GetMapping
    public Flux<RefugeReducedDto> findAllRefuges() {
        return refugeDao.findAll().map(refugeMapper::modelToReducedDto);
    }

    @GetMapping("/{refugeId}")
    public Mono<RefugeDetailsDto> findRefugeById(@PathVariable("refugeId") Long refugeId) {
        Mono<Refuge> refuge = refugeDao.findById(refugeId);
        refuge.switchIfEmpty(Mono.error(new NotFoundException("Refuge with id " + refugeId + " not found")));
        return refuge.map(refugeMapper::modelToDetailsDto);
    }

    @PostMapping
    public ResponseEntity<Mono<RefugeDetailsDto>> createRefuge(
            @RequestBody @Valid RefugeCreationDto refugeCreationDto) {
        Mono<Refuge> refuge = refugeDao.save(refugeMapper.creationDtoToModel(refugeCreationDto));
        URI createUri = URI.create("/api/refuges/" + refuge.map(BaseEntity::getId));
        return ResponseEntity.created(createUri).body(refuge.map(refugeMapper::modelToDetailsDto));
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
    public ResponseEntity<Mono<RefugeDetailsDto>> mergeRefuge(
            @PathVariable("refugeId") Long refugeId,
            @RequestBody @Valid RefugeUpdateDto refugeUpdateDto) {
        Mono<Refuge> mergedRefuge = Mono.just(refugeId)
                .flatMap(refugeDao::existsById)
                .flatMap(exists -> {
                    if (exists) {
                        Refuge refugeUpdateModel = refugeMapper.updateDtoToModel(refugeUpdateDto);
                        refugeUpdateModel.setId(refugeId);
                        return refugeDao.save(refugeUpdateModel);
                    } else {
                        return Mono.error(new NotFoundException("Refuge with id " + refugeId + " not found"));
                    }
                });
        URI mergeUri = URI.create("/api/refuges/" + mergedRefuge.map(BaseEntity::getId));
        return ResponseEntity.created(mergeUri).body(mergedRefuge.map(refugeMapper::modelToDetailsDto));
    }

    @DeleteMapping("/{refugeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRefuge(@PathVariable("refugeId") Long refugeId) {
        Mono.just(refugeId)
                .flatMap(refugeDao::existsById)
                .flatMap(exists -> {
                    if (!exists) {
                        return Mono.error(new NotFoundException("Refuge with id " + refugeId + " not found"));
                    }
                    return Mono.just(true);
                });
        refugeDao.deleteById(refugeId);
    }
}
