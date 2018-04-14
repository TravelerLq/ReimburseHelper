package com.sas.rh.reimbursehelper.NetworkUtil;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Bean.SingleReimbursement;

import java.io.IOException;
import java.util.List;

public class SingleReimbursementUtil {
    //    public static void main(String[] args) {
//        addSingleReimbursement();
//    }
    private static final String urlStr = AddressConfig.RootAddress;

    //新增一条单项报销
    public static JSONObject addSingleReimbursement(int userId,Byte expenseItem, Byte expenseCategory, Integer formId, Double amount, String remark) {
//        //报销科目三级id
//        Byte expenseItem = 0;
//        //报销科目二级id
//        Byte expenseCategory = 1;
//        //对应的报销单id
//        Integer formId = 52;
//        //报销金额
//        Double amount = 400.55;
//        //事由摘要
//        String remark = "无三级报销科目测试";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("expenseItem", expenseItem);
        jsonObject.put("expenseCategory", expenseCategory);
        jsonObject.put("formId", formId);
        jsonObject.put("amount", amount);
        jsonObject.put("remark", remark);
        String url = urlStr+"/yuanshensystem/singlereim/add";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;
        //Integer expenseId = reJson.getInteger("expenseId");
        //System.out.println(expenseId);

    }


    //更新一条单项报销
    public static void updateSingleReimbursement() {
        //单项报销id
        Integer expenseId = 3;
        //事由摘要
        String remark = "事由更新测试";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expenseId", expenseId);
        jsonObject.put("remark", remark);
        String url = "http://localhost:8080/yuanshensystem/singlereim/update";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson.toString());
    }

    //更新一条单项报销
    public static void deleteSingleReimbursement() {
        //单项报销id
        Integer expenseId = 3;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expenseId", expenseId);
        String url = "http://localhost:8080/yuanshensystem/singlereim/delete";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson.toString());
    }

    //更新一条单项报销
    public static void selectSingleReimbursement() {
        //单项报销id
        Integer expenseId = 3;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expenseId", expenseId);
        String url = "http://localhost:8080/yuanshensystem/singlereim/select";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String singleReimbursementJson = reJson.getString("singleReimbursement");
        SingleReimbursement singleReimbursement = JSON.parseObject(singleReimbursementJson, SingleReimbursement.class);
        System.out.println(singleReimbursement.getRemark());
    }

    //根据报销单id查询报销项
    public static JSONArray selectbyformId(int formId) {
        //单项报销id

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formId", formId);
        String url = urlStr + "yuanshensystem/singlereim/selectbyformid";
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        List<SingleReimbursement> singleReimbursementList = JSONArray.parseArray(jsonArray.toJSONString(), SingleReimbursement.class);
        System.out.println(singleReimbursementList.get(0).getRemark());
        return jsonArray;
    }

    //签名证书 以json传入
    public static JSONObject signJsonString(String data, String cert, String key, String name, int index, int expendId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("doc", data);
        jsonObject.put("key", key);
        jsonObject.put("name", name);
        jsonObject.put("cert", cert);
        jsonObject.put("index", index);
        jsonObject.put("expenseId", expendId);
        String url = AddressConfig.RootAddress + "yuanshensystem/sign/verify";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);

        String json = reJson.getString("json");

        return reJson;

    }

    public static String jsontoStr(String data, String cert, String key, String name, int index, int expendId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("doc", data);
        jsonObject.put("key", key);
        jsonObject.put("name", name);
        jsonObject.put("cert", cert);
        jsonObject.put("index", index);
        jsonObject.put("expenseId", expendId);
        String url = AddressConfig.RootAddress + "yuanshensystem/sign/verify";
        String jsonStr = jsonObject.toJSONString();
//        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
//
//        String json = reJson.getString("json");

        return jsonStr;

    }

    //签名证书 以json传入
    public static JSONObject signJsonStringNew(String data, String cert, String key, String name, int index, int expendId) {
        JSONObject signText = new JSONObject();
        //待签名的文档内容
        String doc = data;

        signText.put("doc", doc);
        signText.put("expenseId", expendId);
        signText.put("name", name);
        JSONArray signatures = new JSONArray();
        //第一个签名需要存储的内容
        JSONObject firstSign = new JSONObject();
        firstSign.put("index", index);
        firstSign.put("signature", key);
        // firstSign.put("originalFilename", name);
        firstSign.put("certificate", cert);

        signatures.add(firstSign);
        signText.put("signatures", signatures);
        String jsonStr = signText.toJSONString();
        Log.e("---signJsonStringNew", "---jsonStr=" + jsonStr);
        String url = AddressConfig.RootAddress + "yuanshensystem/sign/verify";
        JSONObject reJson = JsonUtil.uploadJson(url, signText);
        String json = reJson.getString("json");

        return reJson;

    }

    //签名证书 以json传入
    public static JSONObject signJsonStringPdf(String data, String cert, String key, String name, int index, int formId) {
        JSONObject signText = new JSONObject();
        //待签名的文档内容
        String doc = data;

        signText.put("doc", doc);
        signText.put("formId", formId);
        signText.put("name", name);
        JSONArray signatures = new JSONArray();
        //第一个签名需要存储的内容
        JSONObject firstSign = new JSONObject();
        firstSign.put("index", index);
        firstSign.put("signature", key);
        // firstSign.put("originalFilename", name);
        firstSign.put("certificate", cert);

        signatures.add(firstSign);
        signText.put("signatures", signatures);
        String jsonStr = signText.toJSONString();
        Log.e("---signJsonStringNew", "---jsonStr=" + jsonStr);
        String url = AddressConfig.RootAddress + "yuanshensystem/sign/verifypdfform";
        JSONObject reJson = JsonUtil.uploadJson(url, signText);
        String json = reJson.getString("json");

        return reJson;

    }


    public static JSONObject dgetPdfByFormId(Integer formId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formId", formId);
        String url = urlStr + "yuanshensystem/filedown/getpdfbyformid";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String fileJson = reJson.getString("file");
        String originalFilename = reJson.getString("originalFilename");
//        String filePath = fileFolder + originalFilename;
//        System.out.println(fileJson);
       // base64ToImage(fileJson, filePath);
        return reJson;
    }


}
