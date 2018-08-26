package com.zhangs.library;

import android.annotation.TargetApi;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;

import java.io.IOException;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class KeyStoreAboveApi23Compat extends BaseKeyStoreService {


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public boolean createKey(String alias) {
        this.alias = alias;
        try {
            keyStore.load(null);
            if (keyStore.containsAlias(this.alias)) {
                return true;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        if (config == null || config.context == null) {
            return false;
        }
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_RSA, BaseKeyStoreService.KEYSTORE_PROVIDER);
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec
                    .Builder(this.alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .setUserAuthenticationRequired(config.authRequried)
                    .build();
            keyPairGenerator.initialize(keyGenParameterSpec);
            keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }



    @Override
    public void encrypt(String key, String value, EncryptCallback callback) {
        if (config.authRequried){

        }else {
            super.encrypt(key,value,callback);
        }
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {
        if (config.authRequried){

        }else {
            super.decrypt(key,callback);
        }
    }
}
