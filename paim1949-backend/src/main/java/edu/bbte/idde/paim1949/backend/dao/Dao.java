package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.BaseEntity;

import java.util.Collection;

public interface Dao<T extends BaseEntity> {
    Collection<T> findAll();

    T findById(Long id);

    T create(T value);

    T update(Long id, T value);

    T delete(Long id);
}
