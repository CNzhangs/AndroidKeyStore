package com.zhangs.library.callback;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.text.TextUtils;
import android.util.Base64;

import com.zhangs.library.PreferencesHelper;
import com.zhangs.library.model.Constants;
import com.zhangs.library.model.ErrorMsg;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class DecryptAuthCallback extends FingerprintManagerCompat.AuthenticationCallback {

    private String key;
    private DecryptCallback callback;
    public DecryptAuthCallback(String key, DecryptCallback callback) {
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
        callback.onFail(ErrorMsg.create(helpMsgId, helpString.toString()));
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
