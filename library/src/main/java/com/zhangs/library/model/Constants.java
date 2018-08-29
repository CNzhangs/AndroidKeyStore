package com.zhangs.library.model;

public interface Constants {
    interface Error {
        int ERROR_UNDEFINED = -100;//未定义错误
        int ERROR_FINGER_AUTH = -101; //指纹验证错误
        int ERROR_ENCRYPT = -102;
        int ERROR_DECRYPT = -103;
    }
}
