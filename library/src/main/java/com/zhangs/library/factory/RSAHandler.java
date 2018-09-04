package com.zhangs.library.factory;

import android.security.keystore.KeyProperties;

import com.zhangs.library.model.Config;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class RSAHandler extends AbsSecurityHandler {
    public RSAHandler(Config config) throws Exception {
        super(config);
    }

    @Override
    protected String getAlgorithm() {
        return isBelowApi23() ? "RSA" : KeyProperties.KEY_ALGORITHM_RSA;
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
    public String encrypt(String data) {
        return null;
    }

    @Override
    public String decrypt(String data) {
        return null;
    }
}
