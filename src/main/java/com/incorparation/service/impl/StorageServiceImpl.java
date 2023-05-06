package com.incorparation.service.impl;

import com.incorparation.dao.StorageDAO;
import com.incorparation.dto.StorageObject;
import com.incorparation.exception.CommonException;
import com.incorparation.mapper.StorageMapper;
import com.incorparation.model.Storage;
import com.incorparation.service.StorageService;
import com.incorparation.util.EncryptDecrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

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
    public String createStorage(StorageObject.StorageDTO storageDTO) {
        if (storageDAO.findByEmail(storageDTO.getEmail()).isPresent()) {
            throw new CommonException("storage.already.exist");
        }

        Storage storage = buildStorage(storageDTO);
        storageDAO.save(storage);

        //return storageMapper.storageToDto(storage);
        return authenticationService.createToken(storage);
    }

    @Override
    public String login(StorageObject.StorageLoginDTO login) {
        Optional<Storage> optionalStorage = storageDAO.findByEmail(login.getEmail());

        if (optionalStorage.isPresent()) {
            var salt = optionalStorage.get().getSalt();

            String passwordFromDB = optionalStorage.get().getPassword();

            String inputtedEncryptedAndHashedPassword = EncryptDecrypt.byteToBase64(EncryptDecrypt.getHash(
                    EncryptDecrypt.encrypt(login.getPassword()),
                    EncryptDecrypt.stringToByteBase64(salt)
            ));

            if (inputtedEncryptedAndHashedPassword.equals(passwordFromDB)) {
                return authenticationService.createToken(optionalStorage.get());
            } else {
                throw new CommonException("invalid.password");
            }

        } else {
            throw new CommonException("storage.not.exist");
        }
    }

    private Storage buildStorage(StorageObject.StorageDTO storageDTO) {
        Storage storage = storageMapper.DtoToStorage(storageDTO);
        storage.setCreatedAt(LocalDateTime.now());

        encryptStoragePassword(storage);

        return storage;
    }

    private void encryptStoragePassword(Storage storage) {
        var salt = EncryptDecrypt.getSalt();

        storage.setSalt(EncryptDecrypt.byteToBase64(salt));
        storage.setPassword(EncryptDecrypt.byteToBase64(
                        EncryptDecrypt.getHash(
                                EncryptDecrypt.encrypt(storage.getPassword()),
                                salt
                        )
                )
        );
    }
}
