package com.incorparation.dao;

import com.incorparation.model.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenDAO extends CrudRepository<Token, Integer> {
    Token findTokenByStorage(Integer storageId);
}
