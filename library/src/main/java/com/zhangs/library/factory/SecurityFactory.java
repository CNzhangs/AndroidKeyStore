package com.zhangs.library.factory;

import com.zhangs.library.model.Config;

public class SecurityFactory {
    public static ISecurityHandler createRSAHanlder(Config config) throws Exception{
        return new RSAHandler(config);
    }
    public static ISecurityHandler createAESHanlder(Config config) throws Exception{
        return new AESHandler(config);
    }
}
