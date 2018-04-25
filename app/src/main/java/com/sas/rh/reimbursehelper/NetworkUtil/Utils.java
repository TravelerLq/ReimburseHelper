package com.sas.rh.reimbursehelper.NetworkUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liqing on 18/3/27.
 */

public class Utils {

    //byte 与 int 的相互转换
    public static byte intToByte(int x) {
        return (byte) x;
    }

    public static byte strToByte(String str) {
        return Byte.parseByte(str);
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


//    1.string格式转化为Date对象：
//
//    //把string转化为date
//    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
//
//    Date date = fmt.parse(szBeginTime);
//test.setStartTime(date);
//byte 与 int 的相互转换
public static Date strToDate(String str) {
    //把string转化为date
    DateFormat fmt =new SimpleDateFormat("yyyy-MM-dd");
    Date date = null;
    try {
        date = fmt.parse(str);
    } catch (ParseException e) {
        e.printStackTrace();
    }

    return date;
}
}
