package edu.bbte.idde.paim1949.backend.dao;

import edu.bbte.idde.paim1949.backend.model.BaseEntity;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T extends BaseEntity> {
    Collection<T> findAll();

    Optional<T> findById(Long id);

    T save(T value);

    boolean existsById(Long id);

    void deleteById(Long id);
}
