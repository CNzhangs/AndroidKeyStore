package com.zhangs.library;

import com.zhangs.library.model.Config;

import java.security.KeyPair;
import java.security.KeyStore;

public abstract class BaseKeyStoreService {
    static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    
    KeyStore keyStore;
    String defaultAlias ="";
    Config config;
    KeyPair keyPair;

    BaseKeyStoreService() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    void authFinger(){}
}
