package edu.bbte.idde.paim1949.backend.mapper;

import edu.bbte.idde.paim1949.backend.dto.incoming.TourAddToRegionDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.TourCreationDto;
import edu.bbte.idde.paim1949.backend.dto.incoming.TourUpdateDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.TourDetailsDto;
import edu.bbte.idde.paim1949.backend.dto.outgoing.TourReducedDto;
import edu.bbte.idde.paim1949.backend.model.Tour;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Collection;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class TourMapper {

    @IterableMapping(elementTargetType = TourReducedDto.class)
    public abstract Collection<TourReducedDto> modelsToReducedDtos(Iterable<Tour> model);

    public abstract TourReducedDto modelToReducedDto(Tour model);

    public abstract TourDetailsDto modelToDetailsDto(Tour model);

    public abstract Tour creationDtoToModel(TourCreationDto dto);

    public abstract Tour addToRegionDtoToModel(TourAddToRegionDto dto);

    public abstract Tour updateDtoToModel(TourUpdateDto dto);

}
