package com.zhangs.library.factory;

public interface ISecurityHandler {
    /**
     * 加密数据
     * @param data
     * @return
     */
    String encrypt(String data);

    /**
     * 解密数据
     * @param data
     * @return
     */
    String decrypt(String data);
}
