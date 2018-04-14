package com.sas.rh.reimbursehelper.NetworkUtil;

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
}
