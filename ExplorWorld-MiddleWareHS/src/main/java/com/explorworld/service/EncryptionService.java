package com.explorworld.service;

public interface EncryptionService {

    public String encrypt(String data);

    public boolean check(String clearData, String encryptedData);
}