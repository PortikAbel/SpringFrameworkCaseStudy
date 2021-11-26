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
    protected final Map<Long, T> dataBase = new ConcurrentHashMap<>();
    private static final AtomicLong NEXT_ID = new AtomicLong();

    @Override
    public Collection<T> findAll() {
        log.info("All models requested");
        return dataBase.values();
    }

    @Override
    public T findById(Long id) {
        T value = dataBase.get(id);
        log.info("Model requested: " + value);
        return value;
    }

    @Override
    public T create(T value) {
        Long nextId = NEXT_ID.getAndIncrement();
        value.setId(nextId);
        dataBase.put(nextId, value);

        log.info("Model created: " + value);
        return value;
    }

    @Override
    public T update(Long id, T value) {
        T oldT = dataBase.get(id);
        dataBase.replace(id, value);

        log.info("Model being updated."
                + "\n\told value: " + oldT
                + "\n\tnew value: " + value
        );
        return oldT;
    }

    @Override
    public T delete(Long id) {
        T value = dataBase.remove(id);

        log.info("Model removed: " + value);
        return value;
    }
}
