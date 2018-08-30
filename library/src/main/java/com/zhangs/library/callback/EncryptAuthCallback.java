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

public class EncryptAuthCallback extends FingerprintManagerCompat.AuthenticationCallback {
    private String key, value;
    private EncryptCallback callBack;
    public EncryptAuthCallback(String key, String value, EncryptCallback callBack) {
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
        callBack.onFail(ErrorMsg.create(helpMsgId, helpString.toString()));
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        String data = "";
        try {
            Cipher cipher = result.getCryptoObject().getCipher();
            data = Base64.encodeToString(cipher.doFinal(value.getBytes()), Base64.DEFAULT);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
            callBack.onFail(ErrorMsg.create(Constants.Error.ERROR_ENCRYPT, "Encrypt data catch exception -> BadPaddingException | IllegalBlockSizeException."));
        }
        if (TextUtils.isEmpty(data)) {
            callBack.onFail(ErrorMsg.create(Constants.Error.ERROR_ENCRYPT, "Encrypt data failed"));
        } else {
            PreferencesHelper.save(key, data);
            callBack.onSuccess(data);
        }
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        callBack.onFail(ErrorMsg.create(Constants.Error.ERROR_FINGER_AUTH, "Encrypt data : onAuthenticationFailed"));
    }
}
