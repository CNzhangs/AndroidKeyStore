package com.zhangs.library.factory;

import java.security.KeyPair;
import java.security.spec.AlgorithmParameterSpec;

public class RSAHandler extends AbsSecurityHandler {
    public RSAHandler(String alias) throws Exception{
        super(alias);
    }
    @Override
    protected String getAlgorithm() {
        return null;
    }

    @Override
    protected String getTransformation() {
        return null;
    }

    @Override
    protected KeyPair getExistKeyPair() {
        return null;
    }

    @Override
    protected AlgorithmParameterSpec getKeyGenSpec() {
        return null;
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
