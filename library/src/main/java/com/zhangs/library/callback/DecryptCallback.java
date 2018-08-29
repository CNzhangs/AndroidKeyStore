package com.zhangs.library.callback;

import com.zhangs.library.model.ErrorMsg;

public interface DecryptCallback {

    /**
     * 解密成功回调
     * @param data
     */
    void onSuccess(String data);

    /**
     * 解密失败回调
     * @param msg
     */
    void onFail(ErrorMsg msg);
}
