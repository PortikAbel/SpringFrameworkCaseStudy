package edu.bbte.idde.paim1949.backend.dto.outgoing;

import edu.bbte.idde.paim1949.backend.model.Refuge;
import edu.bbte.idde.paim1949.backend.model.Tour;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RegionDetailsDto extends RegionReducedDto {
    Collection<Tour> tours;
    Collection<Refuge> refuges;
}
