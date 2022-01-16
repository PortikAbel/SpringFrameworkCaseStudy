package edu.bbte.idde.paim1949.backend.dto.incoming;

import jdk.jfr.BooleanFlag;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Data
public class RefugeCreationDto implements Serializable {

    @Positive
    @NotNull
    private Integer nrOfRooms;

    @PositiveOrZero
    @NotNull
    private Integer nrOfBeds;

    @BooleanFlag
    @NotNull
    private Boolean isOpenAtWinter;

    @Valid
    @NotNull
    private ReferencedRegionDto region;
}
