package com.zhangs.library;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;

import com.zhangs.library.callback.DecryptAuthCallback;
import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptAuthCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Constants;
import com.zhangs.library.model.ErrorMsg;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;

public class KeyStoreAboveApi23Compat extends BaseKeyStoreService {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    AlgorithmParameterSpec getKeyGenSpec() {
        return new KeyGenParameterSpec
                .Builder(this.alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .setUserAuthenticationRequired(config.authRequired)
                .build();
    }

    @Override
    public void encrypt(String key, String value, EncryptCallback callback) {
        if (config.authRequired) {
            encryptByFingerPrint(key, value, callback);
        } else {
            super.encrypt(key, value, callback);
        }
    }

    private void encryptByFingerPrint(String key, String value, EncryptCallback callback) {
        try {
            checkKeyPair();
            cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
        } catch (Exception e) {
            callback.onFail(ErrorMsg.create(Constants.Error.ERROR_ENCRYPT, "Cipher init failed."));
            e.printStackTrace();
        }
        FingerprintManagerCompat.CryptoObject object =
                new FingerprintManagerCompat.CryptoObject(cipher);
        FingerprintManagerCompat.from(config.context)
                .authenticate(object, 0, null,
                        new EncryptAuthCallback(key, value, callback), null);
    }

    @Override
    public void decrypt(String key, DecryptCallback callback) {
        if (config.authRequired) {
            try {
                decryptByFingerPrint(key, callback);
            } catch (Exception e) {
                callback.onFail(ErrorMsg.create(Constants.Error.ERROR_UNDEFINED, "Decrypt data failed,catch exception:"));
            }
        } else {
            super.decrypt(key, callback);
        }
    }

    private void decryptByFingerPrint(String key, DecryptCallback callback) throws Exception {
        checkKeyPair();
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
        FingerprintManagerCompat.CryptoObject object =
                new FingerprintManagerCompat.CryptoObject(cipher);
        FingerprintManagerCompat.from(config.context)
                .authenticate(object, 0, null, new DecryptAuthCallback(key, callback), null);
    }
}
