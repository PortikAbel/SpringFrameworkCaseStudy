package edu.bbte.idde.paim1949.backend.dto.outgoing;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class RefugeDetailsDto extends RefugeReducedDto {
    RegionReducedDto region;
}
