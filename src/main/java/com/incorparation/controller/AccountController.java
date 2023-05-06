package com.incorparation.controller;

import com.incorparation.dto.StorageObject;
import com.incorparation.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {
    private final StorageService storageService;

    @Autowired
    public AccountController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createStorage(@Valid @RequestBody StorageObject.StorageDTO storageDTO) {
        return ResponseEntity.ok(storageService.createStorage(storageDTO));
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody StorageObject.StorageLoginDTO storageLogin) {
        return ResponseEntity.ok(storageService.login(storageLogin));
    }
}
