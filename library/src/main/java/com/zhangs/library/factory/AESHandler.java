package com.zhangs.library.factory;

import android.util.Base64;

import com.zhangs.library.model.Config;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESHandler extends AbsSecurityHandler {
    SecretKey secretKey;
    public AESHandler(Config config) throws Exception {
        super(config);
    }

    @Override
    protected String getAlgorithm() {
        return "AES";
    }

    @Override
    protected void createKey() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        if (secretKey!=null){
            return;
        }
        KeyGenerator keyGenerator = KeyGenerator
                .getInstance(getAlgorithm(), KEYSTORE_PROVIDER);
        keyGenerator.init(getKeyGenSpec());
        secretKey = keyGenerator.generateKey();
    }

    @Override
    protected void getExistKeyPair() throws Exception {
        checkKeyStore();
        secretKey = (SecretKey) keyStore.getKey(config.alias,null);
    }


    @Override
    public String encrypt(String data) throws  Exception{
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedByte = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedByte, Base64.DEFAULT);
    }

    @Override
    public String decrypt(String data) throws  Exception{
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] encryptedBytes = Base64.decode(data, Base64.DEFAULT);
        byte[] result =  cipher.doFinal(encryptedBytes);
        return new String(result);
    }
}
