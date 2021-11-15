package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.dao.Dao;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
public class AbstractMemDao<T extends BaseEntity> implements Dao<T> {
    private final Map<Long, T> DATABASE = new ConcurrentHashMap<>();
    private static final AtomicLong NEXT_ID = new AtomicLong();

    @Override
    public Collection<T> findAll() {
        log.info("All models requested");
        return DATABASE.values();
    }

    @Override
    public T findById(Long id) {
        T t = DATABASE.get(id);
        log.info("Model requested: " + t);
        return t;
    }

    @Override
    public T create(T t) {
        Long nextId = NEXT_ID.getAndIncrement();
        t.setId(nextId);
        DATABASE.put(nextId, t);

        log.info("Model created: " + t);
        return t;
    }

    @Override
    public T update(Long id, T t) {
        T oldT = DATABASE.get(id);
        DATABASE.replace(id, t);

        log.info("Model being updated."
                + "\n\told value: " + oldT
                + "\n\tnew value: " + t
        );
        return oldT;
    }

    @Override
    public T delete(Long id) {
        T t = DATABASE.remove(id);

        log.info("Model removed: " + t);
        return t;
    }
}
