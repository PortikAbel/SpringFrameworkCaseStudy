package edu.bbte.idde.paim1949.backend.mapper;

import edu.bbte.idde.paim1949.backend.dto.incoming.ReferencedRegionDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.RegionUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RegionDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.RegionReducedDto;
import edu.bbte.idde.paim1949.backend.model.Region;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class RegionMapper {

    public abstract RegionReducedDto modelToReducedDto(Region model);

    public abstract RegionDetailsDto modelToDetailsDto(Region model);

    public abstract Region creationDtoToModel(RegionCreationDto dto);

    public abstract Region creationDtoToModel(RegionCreationDto dto, Long id);

    public abstract Region updateDtoToModel(RegionUpdateDto dto, Long id);

    public abstract Region referencedDtoToModel(ReferencedRegionDto dto);
}
