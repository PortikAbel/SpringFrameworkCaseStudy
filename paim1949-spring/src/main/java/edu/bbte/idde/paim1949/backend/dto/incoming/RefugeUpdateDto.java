package edu.bbte.idde.paim1949.backend.dto.incoming;

import jdk.jfr.BooleanFlag;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;

@Data
public class RefugeUpdateDto implements Serializable {

    @Positive
    private Integer nrOfRooms;

    @PositiveOrZero
    private Integer nrOfBeds;

    @BooleanFlag
    private Boolean isOpenAtWinter;

    @Valid
    private ReferencedRegionDto region;
}
