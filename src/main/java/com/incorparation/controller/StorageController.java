package com.incorparation.controller;

import com.incorparation.dto.StorageObject;
import com.incorparation.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/storages")
public class StorageController {
    private final StorageService storageService;

    @Autowired
    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/{storageId}")
    public ResponseEntity<StorageObject.StorageDTO> getStorage(@PathVariable("storageId") Integer storageId) {
        return ResponseEntity.ok(storageService.getStorageById(storageId));
    }
}
