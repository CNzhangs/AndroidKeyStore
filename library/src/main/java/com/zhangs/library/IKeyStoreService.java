package com.zhangs.library;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;

public interface IKeyStoreService {
    /**
     * 加密数据
     * @param key   数据存储时的Key值
     * @param value 需要加密的数据
     * @param callback 加密回调
     * @return
     */
    boolean encrypt(String key, String value, EncryptCallback callback);
    void    decrypt(String key, DecryptCallback callback);
}
