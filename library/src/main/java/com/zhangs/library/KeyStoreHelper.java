package com.zhangs.library;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;

public class KeyStoreHelper implements IKeyStoreService{
    IKeyStoreService keyStoreService;

    @Override
    public void decrypt(String key, DecryptCallback callback) {

    }

    @Override
    public boolean encrypt(String key, String value, EncryptCallback callback) {
        return false;
    }


}
