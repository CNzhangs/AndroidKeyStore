package com.zhangs.library.callback;

import com.zhangs.library.model.ErrorMsg;

public interface EncryptCallback {
    void onStart();
    void onSuccess(String result);
    void onFail(ErrorMsg msg);
}
