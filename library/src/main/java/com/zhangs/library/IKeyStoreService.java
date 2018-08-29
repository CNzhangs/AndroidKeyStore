package com.zhangs.library;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Config;

public interface IKeyStoreService {
    /**
     * 创建一个key
     *
     * @param alias
     * @return false则创建失败.true成功或已存在.
     */
    void createKey(String alias) throws Exception;

    void setConfig(Config config);
    /**
     * 加密数据
     *
     * @param key      数据存储时的Key值
     * @param value    需要加密的数据
     * @param callback 加密回调
     * @return
     */
    void encrypt(String key, String value, EncryptCallback callback);

    /**
     * 解密数据
     * @param key 加密时保存的key值
     * @param callback 解密回调
     */
    void decrypt(String key, DecryptCallback callback);
}
