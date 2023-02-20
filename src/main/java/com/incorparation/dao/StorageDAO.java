package com.incorparation.dao;

import com.incorparation.model.Storage;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface StorageDAO extends PagingAndSortingRepository<Storage, Integer> {
    Optional<Storage> findByEmail(String email);
}
