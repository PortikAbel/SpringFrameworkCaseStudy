package edu.bbte.idde.paim1949.backend.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Data
public class ReferencedRegionDto implements Serializable {

    @NotNull
    @PositiveOrZero
    Long id;
}