package com.zhangs.library;

import com.zhangs.library.callback.DecryptCallback;

public class KeyStoreHelper implements IKeyStoreService{
    IKeyStoreService keyStoreService;
    

    @Override
    public boolean encrypt(String key, String value) {
        return false;
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {

    }
}
