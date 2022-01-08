package edu.bbte.idde.paim1949.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class BaseEntity implements Serializable {

    @Id
    protected Long id;

}
