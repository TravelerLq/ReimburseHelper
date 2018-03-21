package com.sas.rh.reimbursehelper.NetUtil;

import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dh86 on 2017/12/28.
 */

public class AppErrorCode {

    private static Map<String,String> map;

    public static  Map<String,String> getErrorCodeList(){
        map = new HashMap<String,String>();
        map.put("00","成功!");
        map.put("01","请求错误!");
        map.put("02","服务器异常!");
        map.put("03","内容已存在!");
        return  map;
    }
}
