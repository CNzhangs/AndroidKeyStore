package com.zhangs.library.factory;

import com.zhangs.library.model.Config;

public class AESHandler extends AbsSecurityHandler {

    public AESHandler(Config config) throws Exception {
        super(config);
    }

    @Override
    protected String getAlgorithm() {
        return "AES";
    }


    @Override
    protected void getExistKeyPair() throws Exception {
            checkKeyStore();
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
