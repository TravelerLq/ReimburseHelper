package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class APITest {

    public static void main(String[] args) throws IOException {
        //报销人id
        Integer userId = 48;
        //报销的二级科目id
//        Byte expenseCategoryId = 42;
        Byte expenseCategoryId = 2;

        String url = "http://localhost:8080/yuanshensystem/form/add";
        JSONObject jsonObject = new JSONObject();
        //即用户id
        jsonObject.put("userId", userId);
        //报销的二级科目id
        jsonObject.put("expenseCategoryId", expenseCategoryId);

        String result = APITest.API(url, jsonObject.toJSONString());
        System.out.println(result);
    }

    /**
     * @param
     * @param
     * @return 接口返回的数据
     * @throws IOException
     */
    public static String API(String url, String parameters) throws IOException {
        System.out.println("参数：" + parameters);
        HttpClient httpclient = new DefaultHttpClient();
        //新建Http  post请求
        HttpPost httppost = new HttpPost(url);
        StringEntity stringEntity = new StringEntity(parameters, Charset.forName("UTF-8"));
        stringEntity.setContentType("text/html");
        httppost.setEntity(stringEntity);
        httppost.addHeader("Content-type", "application/json; charset=utf-8");
        httppost.setHeader("Accept", "application/json");
        //处理请求，得到响应
        HttpResponse response = httpclient.execute(httppost);

        //打印返回的结果
        HttpEntity entity = response.getEntity();

        StringBuilder result = new StringBuilder();
        if (entity != null) {
            InputStream instream = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(instream));
            String temp = "";
            while ((temp = br.readLine()) != null) {
                String str = new String(temp.getBytes(), "utf-8");
                result.append(str).append("\r\n");
            }
        }
        return result.toString();
    }
}
