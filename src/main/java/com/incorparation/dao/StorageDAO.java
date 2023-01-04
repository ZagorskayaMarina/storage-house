package com.incorparation.dao;

import com.incorparation.entity.Storage;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StorageDAO extends CrudRepository<Storage, Integer> {
    Optional<Storage> findById(Integer id);
}
