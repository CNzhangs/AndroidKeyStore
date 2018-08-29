package com.zhangs.library;

import android.security.KeyPairGeneratorSpec;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

public class KeyStoreBelowApi23Compat extends BaseKeyStoreService {
    @Override
    AlgorithmParameterSpec getKeyGenSpec() {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);
        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(config.context)
                .setAlias(alias)
                .setSubject(new X500Principal("CN=" + alias))
                .setSerialNumber(BigInteger.TEN)
                .setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();
        return spec;
    }

    @Override
    public void encrypt(String key, String value, EncryptCallback callback) {
        super.encrypt(key, value, callback);
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {
        super.decrypt(key, callback);
    }
}
