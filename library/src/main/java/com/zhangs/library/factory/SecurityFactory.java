package com.zhangs.library.factory;

public class SecurityFactory {
    public static ISecurityHandler createRSAHanlder(String alias) throws Exception{
        return new RSAHandler(alias);
    }
    public static ISecurityHandler createAESHanlder(String alias) throws Exception{
        return new AESHandler(alias);
    }
}
