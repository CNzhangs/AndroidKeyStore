package com.zhangs.library.factory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;

public abstract class AbsSecurityHandler implements ISecurityHandler {
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private KeyStore keyStore;
    private Cipher cipher;
    private KeyPair keyPair;
    private String alias;

    protected abstract String getAlgorithm();
    protected abstract String getTransformation();
    protected abstract KeyPair getExistKeyPair();
    protected abstract AlgorithmParameterSpec getKeyGenSpec();
    public AbsSecurityHandler(String alias) throws Exception{
        this.alias = alias;
        init(alias);
    }

    private void init(String alias) throws Exception {
        keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        cipher = Cipher.getInstance(getTransformation());
        keyStore.load(null);
        if (keyStore.containsAlias(alias)) {
            keyPair = getExistKeyPair();
        }
        if (keyPair != null) {
            return;
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance(getAlgorithm(), KEYSTORE_PROVIDER);
        keyPairGenerator.initialize(getKeyGenSpec());
        keyPair = keyPairGenerator.generateKeyPair();
    }
}
