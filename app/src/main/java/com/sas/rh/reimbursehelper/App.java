package com.sas.rh.reimbursehelper;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sas.rh.reimbursehelper.service.NoticeMsgService;


/**
 * Description：GriddingSys
 * Created by：Kyle
 * Date：2017/4/24
 */

public class App extends Application {
    public static boolean bLog = true;
    public Gson gson;
    private static App instance;
    public static final long ONE_KB = 1024L;
    public static final long ONE_MB = ONE_KB * 1024L;
    public static final long CACHE_DATA_MAX_SIZE = ONE_MB * 3L;
    private Intent serviceIntenta;

    public static App getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        Logger.init();
        initGson();

         initReservoir();
//        PushManager.getInstance().initialize(getApplicationContext(), GPushService.class);
//        PushManager.getInstance().registerPushIntentService(getApplicationContext(), GIntentService.class);
    }

    private void initReservoir() {
        try {
            Reservoir.init(this, CACHE_DATA_MAX_SIZE, gson);
        } catch (Exception e) {
            //failure
            e.printStackTrace();
        }
    }

    private void initGson() {
        gson = new GsonBuilder().create();
    }
//    private void initReservoir() {
//        try {
//            Reservoir.init(this, CACHE_DATA_MAX_SIZE, gson);
//        } catch (Exception e) {
//            //failure
//            e.printStackTrace();
//        }
//    }
//    public static void d(String s){
//        if(bLog)
//            Logger.d(s);
//    }
//    public static void e(String s){
//        if(bLog)
//            Logger.e(s);
//    }

    /**
     * 显示行数，方法名，类名
     *
     * @param s
     */
    public static void d_loc(String s) {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final StackTraceElement ste = stack[1];
//
//        if (bLog) {
//
//            Logger.d(String.format("[%s][%s] \n [%d]%s", ste.getClassName(),ste.getMethodName(),
//                    ste.getLineNumber(),s));
//        }
    }
}
