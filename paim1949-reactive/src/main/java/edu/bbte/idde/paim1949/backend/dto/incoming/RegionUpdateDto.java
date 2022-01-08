package edu.bbte.idde.paim1949.backend.dto.incoming;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class RegionUpdateDto implements Serializable {

    @NotEmpty
    String name;
}
