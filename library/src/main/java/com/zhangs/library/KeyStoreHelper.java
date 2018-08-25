package com.zhangs.library;

import android.os.Build;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Config;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class KeyStoreHelper implements IKeyStoreService{
    IKeyStoreService keyStoreService;
    public KeyStoreHelper(){
        if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            keyStoreService = new KeyStoreBelowApi23Compat();
        }else {
            keyStoreService = new KeyStoreAboveApi23Compat();
        }
    }

    @Override
    public boolean createKey(String alias) throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException {
        return keyStoreService.createKey(alias);
    }

    @Override
    public void setConfig(Config config) {
        keyStoreService.setConfig(config);
    }

    @Override
    public void encrypt(String key, String value, EncryptCallback callback) {
        keyStoreService.encrypt(key,value,callback);
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {
        keyStoreService.decrypt(key,callback);
    }
}
