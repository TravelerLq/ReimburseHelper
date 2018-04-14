package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Bean.ApproveNum;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.constant.Constant;

import java.util.List;


public class ApproveNumUtil {
    private static String urlStr = AddressConfig.RootAddress;

    //private static String urlStr = AddressConfig.TestAddress;

    //新增审批流程
    public static JSONObject addApproveNum(String approveNumName, int approverId, Byte approveNum, int userId) {
//        //审核名称
//        String approveNumName = "经理审核";
//        //审核人id,即要指定的审核人的id
//        Integer approverId = 3;
//        //所处的审核序号
//        Byte approveNum = 3;
//        //创建人id
//        Integer userId = 3;

        String url = urlStr + "yuanshensystem/approvenum/add";
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("companyId", companyId);
        jsonObject.put("approveNumName", approveNumName);
        jsonObject.put("approverId", approverId);
        jsonObject.put("approveNum", approveNum);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;
    }


    //更新审批流程
    public static void updateApproveNum() {
        //审批流程id 必填
        Integer approveNumId = 1;
        //以下选填
        //公司id
        Integer companyId = 2;
        //审核名称
        String approveNumName = "财务审核";
        //审核人id
        Integer approverId = 2;
        //所处的审核序号
        Byte approveNum = 4;
        //更新者id
        Integer userId = 5;
        String url = "http://localhost:8080/yuanshensystem/approvenum/update";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("approveNumId", approveNumId);
        jsonObject.put("companyId", companyId);
        jsonObject.put("approveNumName", approveNumName);
        jsonObject.put("approverId", approverId);
        jsonObject.put("approveNum", approveNum);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);
    }


    //删除审批流程
    public static void deleteApproveNum() {
        //审批流程id 必填
        Integer approveNumId = 1;
        String url = "http://localhost:8080/yuanshensystem/approvenum/delete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("approveNumId", approveNumId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);
    }

    //查看审批流程
    public static void selectApproveNum() {
        //审批流程id 必填
        Integer approveNumId = 2;
        String url = "http://localhost:8080/yuanshensystem/approvenum/select";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("approveNumId", approveNumId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String approveNumJson = reJson.getString("approveNum");
        ApproveNum approveNum = JSON.parseObject(approveNumJson, ApproveNum.class);
        System.out.println(approveNum.getApproveNumName());
    }

    //查看审批流程
    public static JSONArray selectAllApproveNum(int userId) {
        //审批流程id 必填
//        Integer companyId = 3;
        String url = urlStr+ "yuanshensystem/approvenum/selectbycompanyid";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
//        List<ApproveNum> approveNumList = JSONArray.parseArray(jsonArray.toJSONString(), ApproveNum.class);
//        System.out.println(approveNumList.get(0).getApproveNumName());
        return jsonArray;
    }

}
