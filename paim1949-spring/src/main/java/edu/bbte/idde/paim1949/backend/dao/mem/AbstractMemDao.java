package edu.bbte.idde.paim1949.backend.dao.mem;

import edu.bbte.idde.paim1949.backend.annotation.IgnoreColumn;
import edu.bbte.idde.paim1949.backend.dao.Dao;
import edu.bbte.idde.paim1949.backend.exception.ReflectionException;
import edu.bbte.idde.paim1949.backend.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
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
        T oldValue = dataBase.get(id);
        if (oldValue == null) {
            value.setId(id);
            dataBase.put(id, value);
            return value;
        }

        dataBase.replace(id, value);

        log.info("Model being updated."
                + "\n\told value: " + oldValue
                + "\n\tnew value: " + value
        );
        return value;
    }

    @Override
    public T merge(Long id, T value) {
        T oldValue = dataBase.get(id);
        if (oldValue == null) {
            return null;
        }

        Class<? extends BaseEntity> modelClass = value.getClass();
        Arrays.stream(modelClass.getDeclaredFields())
                .filter(field -> field.getAnnotation(IgnoreColumn.class) == null)
                .forEach(field -> {
                    try {
                        String suffixGetterSetter = field.getName().substring(0, 1).toUpperCase(Locale.getDefault())
                                + field.getName().substring(1);
                        Method getter = modelClass.getDeclaredMethod("get" + suffixGetterSetter);
                        Object getterResult = getter.invoke(value);
                        if (getterResult == null) {
                            Method setter = modelClass.getDeclaredMethod("set" + suffixGetterSetter,
                                    field.getType().isEnum() ? String.class : field.getType());
                            setter.invoke(value, getter.invoke(oldValue));
                        }
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        log.error("Could not instantiate from model class. " + e + " " + e.getMessage());
                        throw new ReflectionException();
                    }
                });
        value.setId(id);
        dataBase.replace(id, value);

        log.info("Model being updated."
                + "\n\told value: " + oldValue
                + "\n\tnew value: " + value
        );
        return value;
    }

    @Override
    public boolean delete(Long id) {
        if (!dataBase.containsKey(id)) {
            log.debug("Model with id {} not found.", id);
            return false;
        }

        dataBase.remove(id);
        log.info("Model with id {} removed.", id);

        return true;
    }
}
