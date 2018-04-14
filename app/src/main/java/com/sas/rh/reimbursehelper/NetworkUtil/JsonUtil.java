package com.sas.rh.reimbursehelper.NetworkUtil;

import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtils;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.TestAddress;

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
                Log.e("uploadJson--", "jsonStr" + jsonObject.toJSONString() + "url=" + url);

                postMethod.setRequestBody(new NameValuePair[]{message});
                client.getHttpConnectionManager().getParams()

                        .setConnectionTimeout(5000);
                int status = client.executeMethod(postMethod);
                Log.e("uploadJson", "res=" + postMethod.getResponseBodyAsString());
                responseMsg = JSONObject.parseObject(postMethod.getResponseBodyAsString());
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
            responseMsg.put("error", "上传的json为空");
        }
        return responseMsg;
    }

    public static JSONArray uploadJsonGetJsonArray(String url, JSONObject jsonObject) {
//        PostMethod postMethod = new PostMethod(url);
        PostMethod postMethod = new UTF8PostMethod(url);
        JSONArray responseMsg = new JSONArray();
        Log.e("JsonRequestStr--", "" + jsonObject.toJSONString() + " url=" + url);

        HttpClient client = new HttpClient();
        if (!jsonObject.isEmpty()) {

            try {
                NameValuePair message = new NameValuePair("json", jsonObject.toJSONString());

                postMethod.setRequestBody(new NameValuePair[]{message});
                client.getHttpConnectionManager().getParams()
                        .setConnectionTimeout(5000);
                int status = client.executeMethod(postMethod);

                Log.e("----JsonUtils-", "response=" + postMethod.getResponseBodyAsString());
                responseMsg = JSONArray.parseArray(postMethod.getResponseBodyAsString());
                // Log.e("----JsonUtils-", "response=" + postMethod.getResponseBodyAsString());
                if (status == HttpStatus.SC_OK) {
                    System.out.println(postMethod.getResponseBodyAsString());
                } else {
                    System.out.println("fail");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("approvalUtils", "connection time out");
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
        String url = TestAddress + "yuanshensystem/form/add";
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
