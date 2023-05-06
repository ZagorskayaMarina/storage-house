package com.incorparation.service;

import com.incorparation.dto.StorageObject;

public interface StorageService {
    String createStorage(StorageObject.StorageDTO storageDTO);
    String login(StorageObject.StorageLoginDTO storageEmail);
}
