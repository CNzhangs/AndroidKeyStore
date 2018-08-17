package com.zhangs.library;

import android.security.KeyPairGeneratorSpec;
import android.text.TextUtils;
import android.util.Base64;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

public class KeyStoreBelowApi23Compat extends BaseKeyStoreService implements IKeyStoreService{

    @Override
    public boolean createKey(String alias)  {
        try {
            if (keyStore.containsAlias(alias)){
                return true;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        }
        if (config==null||config.context==null){
            return false;
        }
        defaultAlias = alias;
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);
        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(config.context)
                .setAlias(alias)
                .setSubject(new X500Principal("CN=" + alias))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance("RSA", KEYSTORE_PROVIDER);
            keyPairGenerator.initialize(spec);
            keyPairGenerator.generateKeyPair();
            keyPair = keyPairGenerator.genKeyPair();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public void authFinger() {

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

    private String encryptRSA(byte[] plainText) throws Exception {
        PublicKey publicKey = keyStore.getCertificate(defaultAlias).getPublicKey();
        Cipher cipher = Cipher.getInstance(RSA_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedByte = cipher.doFinal(plainText);
        return Base64.encodeToString(encryptedByte, Base64.DEFAULT);
    }
    private byte[] decryptRSA(String encryptedText) throws Exception {
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(defaultAlias, null);
        Cipher cipher = Cipher.getInstance(RSA_MODE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
        return cipher.doFinal(encryptedBytes);
    }
}
