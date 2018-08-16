package com.zhangs.library;

import android.security.KeyPairGeneratorSpec;

import com.zhangs.library.callback.AuthCallback;
import com.zhangs.library.callback.DecryptCallback;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

public class KeyStoreBelowApi23Compat extends BaseKeyStoreService implements IKeyStoreService{
    
    
    @Override
    public void createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);

        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(config.context)
                .setAlias(keyStoreAlias)
                .setSubject(new X500Principal("CN=" + keyStoreAlias))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();

        KeyPairGenerator keyPairGenerator = KeyPairGenerator
                .getInstance("RSA", KEYSTORE_PROVIDER);

        keyPairGenerator.initialize(spec);
        keyPairGenerator.generateKeyPair();
    }

    @Override
    public boolean encrypt(String key, String value) {
        return false;
    }

    @Override
    public void auth(AuthCallback callback) {

    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {

    }
}
