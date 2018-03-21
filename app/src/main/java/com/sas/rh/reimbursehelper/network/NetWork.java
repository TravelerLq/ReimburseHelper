package com.sas.rh.reimbursehelper.network;

import com.squareup.okhttp.OkHttpClient;

/**
 * Created by liqing on 18/3/19.
 */

public class NetWork {
    public static NetWork instance;
    private NetApi netApi;

    //单例
    private static NetWork getInstance() {
        if (instance == null) {
            instance = new NetWork();
        }
        return instance;
    }
OkHttpClient okHttpClient =new OkHttpClient();

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
