package com.zhangs.library.callback;

import com.zhangs.library.model.ErrorMsg;

public interface DecryptCallback {
    void onStart();
    void onSuccess(String data);
    void onFail(ErrorMsg msg);
}
