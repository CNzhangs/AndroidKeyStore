package com.zhangs.library;

import android.security.KeyPairGeneratorSpec;
import android.text.TextUtils;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;

import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

public class KeyStoreBelowApi23Compat extends BaseKeyStoreService implements IKeyStoreService{

    @Override
    public boolean createKey(String alias)  {
        this.alias = alias;
        try {
            if (keyStore.containsAlias(this.alias)){
                return true;
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
            return false;
        }
        if (config==null||config.context==null){
            return false;
        }
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
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator
                    .getInstance("RSA", KEYSTORE_PROVIDER);
            keyPairGenerator.initialize(spec);
            keyPairGenerator.generateKeyPair();
            keyPair = keyPairGenerator.genKeyPair();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    public void encrypt(String key, String value, EncryptCallback callback) {
        callback.onStart();
        if (TextUtils.isEmpty(key)||TextUtils.isEmpty(value)){
            callback.onFail(null);
            return;
        }
        byte[] data = value.getBytes();
        try {
            String encryptResult = encryptRSA(data);
            if (TextUtils.isEmpty(encryptResult)){
                callback.onFail(null);
                return;
            }
            PreferencesHelper.save(key,encryptResult);
            callback.onSuccess(encryptResult);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(null);
        }
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {
        callback.onStart();
        String decryptData = PreferencesHelper.get(key);
        if (TextUtils.isEmpty(decryptData)){
            callback.onFail(null);
            return;
        }
        try {
            byte[] decryptResult = decryptRSA(decryptData);
            if (decryptResult!=null&&decryptResult.length>0){
                callback.onSuccess(new String(decryptResult));
            }else {
                callback.onFail(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFail(null);
        }
    }

}
