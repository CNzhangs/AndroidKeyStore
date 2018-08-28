package com.zhangs.library.callback;

import com.zhangs.library.model.ErrorMsg;

public interface EncryptCallback {
    void onStart();

    /**
     * 加密成功回调
     * @param result
     */
    void onSuccess(String result);

    /**
     * 加密失败回调
     * @param msg
     */
    void onFail(ErrorMsg msg);
}
