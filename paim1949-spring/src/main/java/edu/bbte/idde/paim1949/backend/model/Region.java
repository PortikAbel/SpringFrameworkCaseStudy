package edu.bbte.idde.paim1949.backend.model;

import edu.bbte.idde.paim1949.backend.annotation.IgnoreColumn;
import edu.bbte.idde.paim1949.backend.annotation.RefByMany;
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

    @IgnoreColumn
    @RefByMany(refTableName = "Tour")
    @OneToMany(
            mappedBy = "region",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private Collection<Tour> tours;

    @IgnoreColumn
    @RefByMany(refTableName = "Refuge")
    @OneToMany(
            mappedBy = "region",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    private Collection<Refuge> refuges;
}
