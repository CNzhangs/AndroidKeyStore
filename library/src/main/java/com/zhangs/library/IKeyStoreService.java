package com.zhangs.library;

import com.zhangs.library.callback.AuthCallback;
import com.zhangs.library.callback.DecryptCallback;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface IKeyStoreService {
    void createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException;
    boolean encrypt(String key,String value);
    void auth(AuthCallback callback);
    void decrypt(String key, DecryptCallback callback);
}
