package com.zhangs.library.factory;

import android.security.keystore.KeyProperties;
import android.util.Base64;

import com.zhangs.library.model.Config;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import javax.crypto.Cipher;

public class RSAHandler extends AbsSecurityHandler {
    KeyPair keyPair;
    public RSAHandler(Config config) throws Exception {
        super(config);
    }

    @Override
    protected String getAlgorithm() {
        return isBelowApi23() ? "RSA" : KeyProperties.KEY_ALGORITHM_RSA;
    }

    @Override
    protected void createKey() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        if (keyPair!=null){
            return;
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(getAlgorithm(), KEYSTORE_PROVIDER);
        keyPairGenerator.initialize(getKeyGenSpec());
        keyPair = keyPairGenerator.generateKeyPair();
    }

    @Override
    protected  void getExistKeyPair() throws  Exception{
        checkKeyStore();
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(config.getAlias(), null);
        Certificate cert = keyStore.getCertificate(config.getAlias());
        PublicKey publicKey = cert.getPublicKey();
        keyPair = new KeyPair(publicKey, privateKey);
    }


    @Override
    public String encrypt(String data) throws Exception {
        checkKeyStore();
        PublicKey publicKey = keyPair.getPublic();
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedByte = cipher.doFinal(data.getBytes());
        return Base64.encodeToString(encryptedByte, Base64.DEFAULT);
    }
    @Override
    public String decrypt(String data) throws  Exception{
        checkKeyStore();
        PrivateKey privateKey = keyPair.getPrivate();
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] encryptedBytes = Base64.decode(data, Base64.DEFAULT);
        byte[] result =  cipher.doFinal(encryptedBytes);
        return new String(result);
    }
}
