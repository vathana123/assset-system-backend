package com.bnkc.assetsystembackend.util;

import com.bnkc.assetsystembackend.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public class Entity {

    public static <E, ID> E getById(
            JpaRepository<E, ID> repository,
            ID id,
            Class<E> entityClass
    ) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityClass, id));
    }
}