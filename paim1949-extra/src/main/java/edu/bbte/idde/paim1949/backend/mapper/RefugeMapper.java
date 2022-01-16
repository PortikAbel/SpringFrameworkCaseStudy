package edu.bbte.idde.paim1949.backend.mapper;

import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeAddToRegionDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RefugeUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RefugeDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RefugeReducedDto;
import edu.bbte.idde.paim1949.backend.model.Refuge;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RefugeMapper {

    @IterableMapping(elementTargetType = RefugeReducedDto.class)
    public abstract Collection<RefugeReducedDto> modelsToReducedDtos(Iterable<Refuge> model);

    public abstract RefugeReducedDto modelToReducedDto(Refuge model);

    public abstract RefugeDetailsDto modelToDetailsDto(Refuge model);

    public abstract Refuge creationDtoToModel(RefugeCreationDto dto);

    public abstract Refuge creationDtoToModel(RefugeCreationDto dto, Long id);

    public abstract Refuge addToRegionDtoToModel(RefugeAddToRegionDto dto);

    public abstract Refuge updateDtoToModel(RefugeUpdateDto dto, Long id);
}
