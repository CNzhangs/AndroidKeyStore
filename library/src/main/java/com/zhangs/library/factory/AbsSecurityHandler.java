package com.zhangs.library.factory;

import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import com.zhangs.library.model.Config;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

public abstract class AbsSecurityHandler implements ISecurityHandler {
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    Config config;
    KeyStore keyStore;
    Cipher cipher;
    KeyPair keyPair;

    protected abstract String getAlgorithm();

    protected abstract void getExistKeyPair() throws Exception;

    public AbsSecurityHandler(Config config) throws Exception {
        this.config = config;
        init(config.getAlias());
    }

    private void init(String alias) throws Exception {
        keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        String transformation = getAlgorithm() + "/ECB/PKCS1Padding";
        cipher = Cipher.getInstance(transformation);
        keyStore.load(null);
        if (keyStore.containsAlias(alias)) {
            getExistKeyPair();
        }
        if (keyPair != null) {
            return;
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(getAlgorithm(), KEYSTORE_PROVIDER);
        keyPairGenerator.initialize(getKeyGenSpec());
        keyPair = keyPairGenerator.generateKeyPair();
    }

    private AlgorithmParameterSpec getKeyGenSpec() {
        if (isBelowApi23()) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 30);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(config.getContext())
                    .setAlias(config.getAlias())
                    .setSubject(new X500Principal("CN=" + config.getAlias()))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            return spec;
        } else {
            return new KeyGenParameterSpec
                    .Builder(config.getAlias(), KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                    .setUserAuthenticationRequired(config.authRequired)
                    .build();
        }
    }

    boolean isBelowApi23() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M;
    }
    void checkKeyStore(){
        if (keyStore==null){
            throw  new NullPointerException("keyStore is null,please check the code.");
        }
    }
}
