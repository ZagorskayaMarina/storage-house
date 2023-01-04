package com.incorparation.service.impl;

import com.incorparation.dao.StorageDAO;
import com.incorparation.dto.StorageObject;
import com.incorparation.entity.Storage;
import com.incorparation.exception.CommonException;
import com.incorparation.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageServiceImpl implements StorageService {
    @Autowired
    protected StorageDAO storageDAO;

    @Override
    public StorageObject.StorageDTO getStorageById(Integer id) {
        return storageDAO.findById(id)
                .map(storage -> convertStorageToDTO(storage))
                .orElseThrow(() -> {
                    throw new CommonException("Invalid storage");
                });
    }


    protected StorageObject.StorageDTO convertStorageToDTO(Storage storage) {
        return StorageObject.StorageDTO.builder()
                .name(storage.getName())
                .domain(storage.getDomain())
                .owner(storage.getOwner())
                .email(storage.getEmail())
                .country(storage.getCountry())
                .province(storage.getProvince())
                .city(storage.getCity())
                .address(storage.getAddress())
                .zip(storage.getZip())
                .currency(storage.getCurrency())
                .status(storage.getStatus())
                .build();
    }
}
