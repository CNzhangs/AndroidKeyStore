package com.zhangs.library;

import android.util.Base64;

import com.zhangs.library.model.Config;

import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public abstract class BaseKeyStoreService {
    static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    static final String RSA_MODE = "RSA/ECB/PKCS1Padding";
    
    KeyStore keyStore;
    String alias ="";
    Config config;
    KeyPair keyPair;

    BaseKeyStoreService() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String encryptRSA(byte[] plainText) throws Exception {
        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();
        Cipher cipher = Cipher.getInstance(RSA_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedByte = cipher.doFinal(plainText);
        return Base64.encodeToString(encryptedByte, Base64.DEFAULT);
    }
    protected byte[] decryptRSA(String encryptedText) throws Exception {
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);
        Cipher cipher = Cipher.getInstance(RSA_MODE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        return cipher.doFinal(encryptedBytes);
    }
}
