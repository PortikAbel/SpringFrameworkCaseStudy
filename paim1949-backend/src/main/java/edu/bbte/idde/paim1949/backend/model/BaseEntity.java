package edu.bbte.idde.paim1949.backend.model;

import java.io.Serializable;

public class BaseEntity implements Serializable {

    protected Long id;

    public BaseEntity() {
    }

    public BaseEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
