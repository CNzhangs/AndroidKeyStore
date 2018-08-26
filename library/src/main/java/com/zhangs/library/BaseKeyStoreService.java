package com.zhangs.library;

import android.text.TextUtils;
import android.util.Base64;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Config;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public abstract class BaseKeyStoreService  implements IKeyStoreService {
    static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    static final String RSA_MODE = "RSA/ECB/PKCS1Padding";

    KeyStore keyStore;
    String alias ="";
    Config config;

    BaseKeyStoreService() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            LogUtils.e("create keystore.");
        } catch (Exception e) {
            LogUtils.e("create keystore failed.");
            e.printStackTrace();
        }
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public void encrypt(String key, String value, EncryptCallback callback) {
        callback.onStart();
        if (TextUtils.isEmpty(key)||TextUtils.isEmpty(value)){
            callback.onFail(null);
            return;
        }
        byte[] data = value.getBytes();
        try {
            String encryptResult = encryptRSA(data);
            if (TextUtils.isEmpty(encryptResult)){
                callback.onFail(null);
                return;
            }
            PreferencesHelper.save(key,encryptResult);
            callback.onSuccess(encryptResult);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(null);
        }
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {
        callback.onStart();
        String decryptData = PreferencesHelper.get(key);
        if (TextUtils.isEmpty(decryptData)){
            callback.onFail(null);
            return;
        }
        try {
            byte[] decryptResult = decryptRSA(decryptData);
            if (decryptResult!=null&&decryptResult.length>0){
                callback.onSuccess(new String(decryptResult));
            }else {
                callback.onFail(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(null);
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
