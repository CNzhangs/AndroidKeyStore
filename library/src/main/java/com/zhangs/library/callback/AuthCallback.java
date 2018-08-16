package com.zhangs.library.callback;

import com.zhangs.library.model.ErrorMsg;

public interface AuthCallback {
    void onStart();
    void onSuccess(Object obj);
    void onFail(ErrorMsg errMsg);
}
