package edu.bbte.idde.paim1949.backend.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RegionCreationDto implements Serializable {

    @NotNull
    @NotEmpty
    String name;
}
