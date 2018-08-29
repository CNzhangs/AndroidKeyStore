package com.zhangs.library;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.RequiresApi;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.TextUtils;
import android.util.Base64;

import com.zhangs.library.callback.DecryptCallback;
import com.zhangs.library.callback.EncryptCallback;
import com.zhangs.library.model.Constants;
import com.zhangs.library.model.ErrorMsg;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class KeyStoreAboveApi23Compat extends BaseKeyStoreService {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    AlgorithmParameterSpec getKeyGenSpec() {
        return  new KeyGenParameterSpec
                .Builder(this.alias, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                .setUserAuthenticationRequired(config.authRequired)
                .build();
    }

    private class EncryptAuthCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        String key, value;
        EncryptCallback callBack;

        public EncryptAuthCallBack(String key, String value, EncryptCallback callBack) {
            this.key = key;
            this.value = value;
            this.callBack = callBack;
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            callBack.onFail(ErrorMsg.create(Constants.Error.ERROR_FINGER_AUTH, errString.toString()));
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            try {
                Cipher cipher = result.getCryptoObject().getCipher();
                String data = Base64.encodeToString(cipher.doFinal(value.getBytes()), Base64.DEFAULT);
                if (TextUtils.isEmpty(data)) {
                    callBack.onFail(ErrorMsg.create(Constants.Error.ERROR_ENCRYPT, "Encrypt data failed"));
                } else {
                    PreferencesHelper.save(key, data);
                    callBack.onSuccess(data);
                }
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                callBack.onFail(ErrorMsg.create(Constants.Error.ERROR_ENCRYPT, "Encrypt data catch exception -> BadPaddingException | IllegalBlockSizeException."));
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            callBack.onFail(ErrorMsg.create(Constants.Error.ERROR_FINGER_AUTH, "Encrypt data : onAuthenticationFailed"));
        }
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
            FingerprintManagerCompat.CryptoObject object =
                    new FingerprintManagerCompat.CryptoObject(cipher);
            FingerprintManagerCompat.from(config.context)
                    .authenticate(object, 0, null,
                            new EncryptAuthCallBack(key, value, callback), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class DecryptAuthCallBack extends FingerprintManagerCompat.AuthenticationCallback {
        String key;
        private DecryptCallback callback;

        public DecryptAuthCallBack(String key, DecryptCallback callback) {
            this.key = key;
            this.callback = callback;
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            callback.onFail(ErrorMsg.create(Constants.Error.ERROR_FINGER_AUTH, errString.toString()));
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            String encryptedText = PreferencesHelper.get(key);
            if (TextUtils.isEmpty(encryptedText)) {
                callback.onFail(ErrorMsg.create(Constants.Error.ERROR_UNDEFINED, "can not find the encrypted data."));
                return;
            }
            byte[] encryptedBytes = Base64.decode(encryptedText, Base64.DEFAULT);
            try {
                Cipher cipher = result.getCryptoObject().getCipher();
                String data = new String(cipher.doFinal(encryptedBytes));
                if (TextUtils.isEmpty(data)) {
                    callback.onFail(ErrorMsg.create(Constants.Error.ERROR_DECRYPT, "Decrypt data failed."));
                } else {
                    callback.onSuccess(data);
                }
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                callback.onFail(ErrorMsg.create(Constants.Error.ERROR_DECRYPT, "Decrypt data failed,catch exception."));
            }
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            callback.onFail(ErrorMsg.create(Constants.Error.ERROR_FINGER_AUTH, "Decrypt data failed:onAuthenticationFailed"));
        }
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
                .authenticate(object, 0, null, new DecryptAuthCallBack(key, callback), null);
    }
}
