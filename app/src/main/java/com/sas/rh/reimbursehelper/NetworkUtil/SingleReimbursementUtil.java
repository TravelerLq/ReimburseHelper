package com.sas.rh.reimbursehelper.NetworkUtil;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Entity.SingleReimbursement;

import java.util.List;

public class SingleReimbursementUtil {
//    public static void main(String[] args) {
//        addSingleReimbursement();
//    }



    //新增一条单项报销
    public static JSONObject addSingleReimbursement(Byte expenseItem,Byte expenseCategory,Integer formId,Double amount,String remark) {
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
        jsonObject.put("expenseItem", expenseItem);
        jsonObject.put("expenseCategory", expenseCategory);
        jsonObject.put("formId", formId);
        jsonObject.put("amount", amount);
        jsonObject.put("remark", remark);
        String url = "http://192.168.1.114:8080/yuanshensystem/singlereim/add";
        Log.e("addSingleReimb","params"+jsonObject.toJSONString());
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
    public static void selectbyformId() {
        //单项报销id
        Integer expenseId = 3;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expenseId", expenseId);
        String url = "http://localhost:8080/yuanshensystem/singlereim/selectbyformid";
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        List<SingleReimbursement> singleReimbursementList = JSONArray.parseArray(jsonArray.toJSONString(), SingleReimbursement.class);
        System.out.println(singleReimbursementList.get(0).getRemark());
    }


}
