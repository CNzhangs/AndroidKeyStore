package com.zhangs.library.factory;

public interface ISecurityHandler {
    /**
     * 加密数据
     * @param data
     * @return
     */
    String encrypt(String data) throws Exception;

    /**
     * 解密数据
     * @param data
     * @return
     */
    String decrypt(String data) throws Exception;
}
