package com.sas.rh.reimbursehelper.NetworkUtil;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author tuzhengsong
 */
public class JsonUtil extends PostMethod {

    public static JSONObject uploadJson(String url, JSONObject jsonObject) {
        PostMethod postMethod = new UTF8PostMethod(url);
        JSONObject responseMsg = new JSONObject();
        HttpClient client = new HttpClient();
        if (!jsonObject.isEmpty()) {

            try {
                NameValuePair message = new NameValuePair("json", jsonObject.toJSONString());
                postMethod.setRequestBody(new NameValuePair[]{message});
                client.getHttpConnectionManager().getParams()
                        .setConnectionTimeout(5000);
                int status = client.executeMethod(postMethod);
                responseMsg = JSONObject.parseObject(postMethod.getResponseBodyAsString());
                Log.e("JsonUtil---uploadJson--",""+postMethod.getResponseBodyAsString());
                if (status == HttpStatus.SC_OK) {
                    Log.e("","ok="+postMethod.getResponseBodyAsString());
                } else {
                    System.out.println("fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //释放链接
                postMethod.releaseConnection();
            }
        } else {
            responseMsg.put("error","上传的json为空");
        }
        return responseMsg;
    }

    public static JSONArray uploadJsonGetJsonArray(String url, JSONObject jsonObject) {
//        PostMethod postMethod = new PostMethod(url);
        System.out.println("++++++++++"+url);
        System.out.println("++++++++++jsonObject.toString()＝"+jsonObject.toString());
        PostMethod postMethod = new UTF8PostMethod(url);
        JSONArray responseMsg = new JSONArray();
        HttpClient client = new HttpClient();
        if (!jsonObject.isEmpty()) {

            try {
                NameValuePair message = new NameValuePair("json", jsonObject.toJSONString());
                postMethod.setRequestBody(new NameValuePair[]{message});
                client.getHttpConnectionManager().getParams()
                        .setConnectionTimeout(5000);
                int status = client.executeMethod(postMethod);
                Log.e("++++++++++" ,"getResponseBodyAsString"+ postMethod.getResponseBodyAsString());
                responseMsg = JSONArray.parseArray(postMethod.getResponseBodyAsString());
                if (status == HttpStatus.SC_OK) {
                    System.out.println(postMethod.getResponseBodyAsString());
                } else {
                    System.out.println("fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //释放链接
                postMethod.releaseConnection();
            }
        } else {
            responseMsg.add(0, "上传的json为空");
        }
        return responseMsg;
    }

    public static void main(String[] args) {
        String url = "http://192.168.1.114:8080/YuAnShenSystem/form/add";
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("test中文", 1234);
//        jsonObject.put("sha", "wergf");
//        jsonObject.put("branch", false);
//        jsonObject.put("中文测试", System.currentTimeMillis());

//        JsonUtil.uploadJson(url, jsonObject);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);
    }

}
