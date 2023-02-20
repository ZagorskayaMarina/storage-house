package com.incorparation.service.impl;

import com.incorparation.dao.StorageDAO;
import com.incorparation.dao.TokenDAO;
import com.incorparation.exception.CommonException;
import com.incorparation.model.Storage;
import com.incorparation.model.Token;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {
    static final String STORAGE_ID = "storageId";
    static final String ACCESS_TOKEN = "accessToken";

    private final TokenGenerationService tokenGenerationService;
    private final CipherService cipherService;
    private final TokenDAO tokenDAO;
    private final StorageDAO storageDAO;

    @Autowired
    public AuthenticationService(TokenGenerationService tokenGenerationService, CipherService cipherService,
                                 TokenDAO tokenDAO, StorageDAO storageDAO) {
        this.tokenGenerationService = tokenGenerationService;
        this.cipherService = cipherService;
        this.tokenDAO = tokenDAO;
        this.storageDAO = storageDAO;
    }

    public String createToken(Storage storage) {
        var accessToken = UUID.randomUUID()
                .toString()
                .replace("-", StringUtils.EMPTY);

        Token token = Token.builder()
                .storage(storage)
                .accessToken(accessToken)
                .createdDate(LocalDateTime.now())
                .build();


        tokenDAO.save(token);

        return generateToken(storage, accessToken);
    }

    public Storage validateAccessToken(String token) {
        var payload = decodeToken(token);

        if (payload.isEmpty()) {
            throw new CommonException("unauthorized");
        }

        var storageId = (Integer) payload.get(STORAGE_ID);
        String savedInDBAccessToken = tokenDAO.findTokenByStorage(storageId).getAccessToken();

        var inputtedAccessToken = (String) payload.get(ACCESS_TOKEN);

        if (!savedInDBAccessToken.equals(inputtedAccessToken)) {
            throw new CommonException("Invalid token");
        }

        Optional<Storage> storage = storageDAO.findById(storageId);
        return storage.orElse(null);

    }

    private String generateToken(Storage storage, String accessToken) {
        var payload = createPayload(storage, accessToken);
        return encodePayload(payload);
    }

    private Map<String, Object> createPayload(Storage storage, String accessToken) {
        Map<String, Object> payload = new HashMap<>();

        payload.put(STORAGE_ID, storage.getId());
        payload.put(ACCESS_TOKEN, accessToken);

        return payload;
    }

    private Map<String, Object> decodeToken(String token) {
        try {
            var decryptAES = cipherService.decryptAES(token);
            return tokenGenerationService.decodePayload(decryptAES);
        } catch (GeneralSecurityException ex) {
            throw new CommonException("Invalid token");
        }
    }

    private String encodePayload(Map<String, Object> payload) {
        try {
            String encodePayload = tokenGenerationService.encodePayload(payload);
            return cipherService.encryptAES(encodePayload);
        } catch (GeneralSecurityException e) {
            throw new CommonException("Internal server error");
        }
    }
}
