package com.zhangs.library.model;

public class ErrorMsg {
    private ErrorMsg() {
    }

    int code;
    String msg;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ErrorMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }

    public static ErrorMsg create(int code,String msg){
        ErrorMsg errorMsg = new ErrorMsg();
        errorMsg.setCode(code);
        errorMsg.setMsg(msg);
        return errorMsg;
    }
}
