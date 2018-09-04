package com.zhangs.library.model;

import android.content.Context;

public class Config {
    public Config(Context context) {
        this.context = context;
    }

    public Config(Context context, String alias, boolean authRequired, long expiredTime) {
        this.context = context;
        this.alias = alias;
        this.authRequired = authRequired;
        this.expiredTime = expiredTime;
    }

    public Context context;
    public String alias = "";
    public boolean authRequired = false;
    public long expiredTime = 30*1000L;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isAuthRequired() {
        return authRequired;
    }

    public void setAuthRequired(boolean authRequired) {
        this.authRequired = authRequired;
    }

    public long getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
