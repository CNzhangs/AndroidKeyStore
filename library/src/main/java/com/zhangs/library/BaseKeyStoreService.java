package com.zhangs.library;

import com.zhangs.library.model.Config;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public abstract class BaseKeyStoreService {
    static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    
    KeyStore mStore;
    String keyStoreAlias="";
    Config config;
    
    abstract void createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException;
    
    BaseKeyStoreService() {
        try {
            mStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            createKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
