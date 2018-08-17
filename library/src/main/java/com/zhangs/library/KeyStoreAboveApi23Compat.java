package com.zhangs.library;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class KeyStoreAboveApi23Compat extends BaseKeyStoreService implements IKeyStoreService{
    
    
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_RSA, BaseKeyStoreService.KEYSTORE_PROVIDER);
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec
                    .Builder(keyStoreAlias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .setUserAuthenticationRequired(false)
                    .build();
            keyPairGenerator.initialize(keyGenParameterSpec);
            keyPairGenerator.generateKeyPair();
    }


    @Override
    public void authFinger() {
    }

    @Override
    public boolean encrypt(String key, String value, EncryptCallback callback) {
        return false;
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {

    }
}
