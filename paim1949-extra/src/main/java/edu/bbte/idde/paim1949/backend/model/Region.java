package edu.bbte.idde.paim1949.backend.model;

import lombok.*;
import org.springframework.stereotype.Repository;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;

@Repository
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Region extends BaseEntity {

    private String name;

    @OneToMany(
            mappedBy = "region",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Collection<Tour> tours;

    @OneToMany(
            mappedBy = "region",
            fetch = FetchType.LAZY,
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private Collection<Refuge> refuges;
}
