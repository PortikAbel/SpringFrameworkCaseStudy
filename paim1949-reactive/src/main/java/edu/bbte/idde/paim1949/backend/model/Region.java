package edu.bbte.idde.paim1949.backend.model;

import lombok.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Region extends BaseEntity {

    private String name;

    private Collection<Tour> tours;

    private Collection<Refuge> refuges;
}
