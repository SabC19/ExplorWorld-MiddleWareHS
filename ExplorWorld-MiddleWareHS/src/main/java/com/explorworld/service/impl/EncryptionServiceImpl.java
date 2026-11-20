package com.explorworld.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.explorworld.service.EncryptionService;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    public EncryptionServiceImpl() {
    }

    @Override
    public String encrypt(String data) {
        if (data == null) {
            return null;
        }
        return BCrypt.hashpw(data, BCrypt.gensalt());
    }

    @Override
    public boolean check(String clearData, String encryptedData) {
        if (clearData == null || encryptedData == null) {
            return false;
        }
        return BCrypt.checkpw(clearData, encryptedData);
    }
}