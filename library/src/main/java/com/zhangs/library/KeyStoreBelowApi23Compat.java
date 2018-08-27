package com.zhangs.library;

import android.security.KeyPairGeneratorSpec;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

public class KeyStoreBelowApi23Compat extends BaseKeyStoreService {
    @Override
    public boolean createKey(String alias)  {
        this.alias = alias;
        try {
            keyStore.load(null);
            if (keyStore.containsAlias(this.alias)){
                return true;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        if (config==null||config.context==null){
            return false;
        }
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 30);
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance("RSA", KEYSTORE_PROVIDER);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(config.context)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=" + alias))
                    .setSerialNumber(BigInteger.TEN)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();
            keyPairGenerator.initialize(spec);
            keyPair = keyPairGenerator.generateKeyPair();
            if (keyPair==null){
                LogUtils.e("key pair is null");
            }else {
                LogUtils.e("key pair is not null");
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
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
