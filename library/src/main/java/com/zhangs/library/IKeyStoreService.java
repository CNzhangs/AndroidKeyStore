package com.zhangs.library;

import android.Manifest;
import android.support.annotation.RequiresPermission;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Config;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface IKeyStoreService {
    /**
     * 创建一个key
     *
     * @param alias
     * @return false则创建失败.true成功或已存在.
     */
    boolean createKey(String alias) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException;

    void setConfig(Config config);
    /**
     * 加密数据
     *
     * @param key      数据存储时的Key值
     * @param value    需要加密的数据
     * @param callback 加密回调
     * @return
     */
    @RequiresPermission(Manifest.permission.USE_FINGERPRINT)
    void encrypt(String key, String value, EncryptCallback callback);

    @RequiresPermission(Manifest.permission.USE_FINGERPRINT)
    void decrypt(String key, DecryptCallback callback);
}
