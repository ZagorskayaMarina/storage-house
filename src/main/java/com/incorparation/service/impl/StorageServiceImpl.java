package com.incorparation.service.impl;

import com.incorparation.dao.StorageDAO;
import com.incorparation.dto.StorageObject;
import com.incorparation.exception.CommonException;
import com.incorparation.mapper.StorageMapper;
import com.incorparation.model.Storage;
import com.incorparation.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StorageServiceImpl implements StorageService {
    protected StorageDAO storageDAO;
    protected StorageMapper storageMapper;
    protected AuthenticationService authenticationService;

    @Autowired
    public StorageServiceImpl(StorageDAO storageDAO,
                              StorageMapper storageMapper,
                              AuthenticationService authenticationService) {
        this.storageDAO = storageDAO;
        this.storageMapper = storageMapper;
        this.authenticationService = authenticationService;
    }

    @Override
    public StorageObject.StorageDTO getStorageById(Integer id) {
        return storageDAO.findById(id)
                .map(storage -> storageMapper.storageToDto(storage))
                .orElseThrow(() -> {
                    throw new CommonException("Invalid storage");
                });
    }

    @Override
    public String createStorage(StorageObject.StorageDTO storageDTO) {
        if (storageDAO.findByEmail(storageDTO.getEmail()).isPresent()) {
            throw new CommonException("The storage with email " + storageDTO.getEmail() + "exists");
        }

        Storage storage = buildStorage(storageDTO);
        //storageDAO.save(storage);

        //return storageMapper.storageToDto(storage);
        return authenticationService.createToken(storage);
    }

    private Storage buildStorage(StorageObject.StorageDTO storageDTO) {
        Storage storage = storageMapper.DtoToStorage(storageDTO);
        storage.setCreatedAt(LocalDateTime.now());

        return storage;
    }
}
