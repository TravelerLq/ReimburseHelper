/**
 * This document and its contents are protected by copyright 2012 and owned by
 * Melot Inc.
 * The copying and reproduction of this document and/or its content (whether
 * wholly or partly) or any
 * incorporation of the same into any other material in any media or format of
 * any kind is strictly prohibited.
 * All rights are reserved.
 * Copyright (c) Melot Inc. 2015
 */
package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;

import java.io.File;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;
import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.TestAddress;


/**
 * @author tuzhengsong
 * @version
 */
public class UploadFileUtil {
    public static JSONObject uploadPdfInvoice(File file, String url, JSONObject jsonObject) {
        if (!file.exists()) {
            return null;
        }
        System.out.println(file.length());
//        PostMethod postMethod = new PostMethod(url);
        PostMethod postMethod = new UTF8PostMethod(url);
        try {
            //传文件
            FilePart fp = new FilePart("file", file);
            //传数据
            StringPart sp = new StringPart("data", jsonObject.toJSONString());
            Part[] parts = {fp,sp};
            MultipartRequestEntity mre = new MultipartRequestEntity(parts,
                    postMethod.getParams());

            postMethod.setRequestEntity(mre);
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams()
                    .setConnectionTimeout(5000);
            int status = client.executeMethod(postMethod);
            JSONObject responseMsg = JSONObject.parseObject(postMethod.getResponseBodyAsString());

            System.out.println(responseMsg.toString());
            System.out.println(status);
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
        return jsonObject;
    }


    public static JSONObject upload(String expenseId,String path) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expenseId", expenseId);
        String url = RootAddress+"yuanshensystem/file/up";
        return UploadFileUtil.uploadPdfInvoice(new File(path), url, jsonObject);
    }
}
