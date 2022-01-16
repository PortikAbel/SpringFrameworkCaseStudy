package edu.bbte.idde.paim1949.backend.dto.outgoing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RegionDetailsDto extends RegionReducedDto {
    Collection<TourCollectableDto> tours;
    Collection<RefugeReducedDto> refuges;
}
