package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import edu.bbte.idde.paim1949.backend.model.Tour;

import java.util.Collection;

public interface Dao<T extends BaseEntity>{
    Collection<T> findAll();

    T findById(Long id);

    T create(T t);

    T update(Long id, T t);

    T delete(Long id);
}
