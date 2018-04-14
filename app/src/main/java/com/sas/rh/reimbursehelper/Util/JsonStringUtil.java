package com.sas.rh.reimbursehelper.Util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.httpclient.NameValuePair;


/**
 * Created by liqing on 18/3/29.
 * 生成 jsonStr
 */

public class JsonStringUtil {

    //   NameValuePair message = new NameValuePair("json", jsonObject.toJSONString());

//    JSONObject jsonObject = new JSONObject();
//        jsonObject.put("formId", formId);
//        jsonObject.put("userId", userId);
//    JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);

    public void getJsonString(String data, String cert, String key, int index,int expenseId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("doc", data);
            jsonObject.put("key", key);
            jsonObject.put("cert", cert);
            jsonObject.put("index", index);
            jsonObject.put("expenseId",expenseId);
            //返回jsonString
            NameValuePair message = new NameValuePair("json", jsonObject.toJSONString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
