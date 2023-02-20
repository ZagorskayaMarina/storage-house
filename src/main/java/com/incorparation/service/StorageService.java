package com.incorparation.service;

import com.incorparation.dto.StorageObject;

public interface StorageService {
    StorageObject.StorageDTO getStorageById(Integer id);
    String createStorage(StorageObject.StorageDTO storageDTO);
}
