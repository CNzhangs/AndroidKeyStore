package com.zhangs.library;

import android.os.Build;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Config;
import com.zhangs.library.model.Constants;
import com.zhangs.library.model.ErrorMsg;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;

public abstract class BaseKeyStoreService  implements IKeyStoreService {
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String RSA_MODE = "RSA/ECB/PKCS1Padding";

    private KeyStore keyStore;
    Cipher cipher;
    KeyPair keyPair;
    String alias ="";
    Config config;
    private String algorithm;
    abstract AlgorithmParameterSpec getKeyGenSpec();
    BaseKeyStoreService() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            cipher = Cipher.getInstance(RSA_MODE);
            LogUtils.e("create keystore.");
        } catch (Exception e) {
            LogUtils.e("create keystore failed.");
            e.printStackTrace();
        }
    }

    @Override
    public void createKey(String alias) throws Exception {
        if (config == null || config.context == null) {
            throw  new NullPointerException("config or context is null");
        }
        this.alias = alias;
        keyStore.load(null);
        if (keyStore.containsAlias(this.alias)) {
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, null);
            Certificate cert = keyStore.getCertificate(alias);
            PublicKey publicKey = cert.getPublicKey();
            keyPair = new KeyPair(publicKey, privateKey);
        }
        if (keyPair !=null){
            return;
        }
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            algorithm = "RSA";
        }else {
            algorithm = KeyProperties.KEY_ALGORITHM_RSA;
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(algorithm, KEYSTORE_PROVIDER);
        keyPairGenerator.initialize(getKeyGenSpec());
        keyPair = keyPairGenerator.generateKeyPair();
        if (keyPair == null) {
            LogUtils.e("key pair is null");
        } else {
            LogUtils.e("key pair is not null");
        }
    }

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public void encrypt(String key, String value, EncryptCallback callback) {
        if (TextUtils.isEmpty(key)||TextUtils.isEmpty(value)){
            callback.onFail(ErrorMsg.create(Constants.Error.ERROR_UNDEFINED,"Decrypt failed."));
            return;
        }
        byte[] data = value.getBytes();
        try {
            String encryptResult = encryptRSA(data);
            if (TextUtils.isEmpty(encryptResult)){
                callback.onFail(ErrorMsg.create(Constants.Error.ERROR_UNDEFINED,"Decrypt failed."));
                return;
            }
            PreferencesHelper.save(key,encryptResult);
            callback.onSuccess(encryptResult);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(ErrorMsg.create(Constants.Error.ERROR_UNDEFINED,"Decrypt failed."));
        }
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {
        String decryptData = PreferencesHelper.get(key);
        if (TextUtils.isEmpty(decryptData)){
            callback.onFail(ErrorMsg.create(Constants.Error.ERROR_UNDEFINED,"Decrypt failed."));
            return;
        }
        try {
            byte[] decryptResult = decryptRSA(decryptData);
            if (decryptResult!=null&&decryptResult.length>0){
                callback.onSuccess(new String(decryptResult));
            }else {
                callback.onFail(ErrorMsg.create(Constants.Error.ERROR_UNDEFINED,"Decrypt failed."));
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(ErrorMsg.create(Constants.Error.ERROR_UNDEFINED,"Decrypt failed."));
        }
    }


    protected String encryptRSA(byte[] plainText) throws Exception {
        checkKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedByte = cipher.doFinal(plainText);
        return Base64.encodeToString(encryptedByte, Base64.DEFAULT);
    }
    protected byte[] decryptRSA(String encryptedText) throws Exception {
        checkKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        return cipher.doFinal(encryptedBytes);
    }
    protected void checkKeyPair() throws Exception {
        if (keyPair ==null){
            throw new Exception("you must call createKey() method before");
        }
    }
}
