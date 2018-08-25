package com.zhangs.library.model;

public class ErrorMsg {
    int code;
    String msg;

    @Override
    public String toString() {
        return "ErrorMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
