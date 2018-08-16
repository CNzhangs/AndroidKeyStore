package com.zhangs.library.model;

import android.content.Context;

public class Config {
    public Context context;
    public String alias = "";
    public boolean authRequried = true;
    public long expiredTime = 30*1000L;
}
