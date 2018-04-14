package com.sas.rh.reimbursehelper.NetworkUtil;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Bean.Approval;

public class ApprovalUtil {

  //  private static String urlStr=AddressConfig.TestAddress ;
    private static String urlStr=AddressConfig.RootAddress ;


    public static void main(String[] args) {
//        addApprove();
//        getMyApproval();
 //       getPendApproval();
//        updateApproveNum();
//        deleteApproveNum();
    }

    //发起一项审批审批
    public static void addApprove() {
        //表单id
        Integer formId = 232;
        //用户id
        Integer userId = 1;
        String url = urlStr+"yuanshensystem/approval/add";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formId", formId);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        Integer approvalId = reJson.getInteger("approvalId");
        System.out.println(approvalId);
    }


    //更新一项审批
    public static JSONObject updateApproveNum(int approvalId,Byte approveResultId,String rejectReason ,int userId  ) {
//        //审核实体id
//        Integer approvalId = 24;
//        //审核结果
//        Byte approveResultId = 2;
//        //驳回原因
//        String rejectReason = "审核不通过，票面信息不完整";
//        //用户id
//        Integer userId = 3;
        String url = urlStr+"yuanshensystem/approval/update";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("approvalId", approvalId);
        jsonObject.put("approveResultId", approveResultId);
        jsonObject.put("rejectReason", rejectReason);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);

        Log.e("approval","jsonResponse"+reJson);
        return reJson;
    }


    //查看一项审批
    public static void selectApprove() {
        //审核实体id
        Integer approvalId = 1;
        String url = urlStr+"yuanshensystem/approval/select";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("approvalId", approvalId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String approvalJson = reJson.getString("approval");
        Approval approval = JSON.parseObject(approvalJson, Approval.class);
        System.out.println(approval.getApprovalName());
    }

    //删除一项审批审批
    public static void deleteApproveNum() {
        //必填
        //公司id
        Integer approvalId = 5;
        //用户id
        Integer userId = 1;
        String url = urlStr+"yuanshensystem/approval/delete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("approvalId", approvalId);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);
    }


    //报销者查看自己的审批进程
    public static JSONArray getMyApproval(int userId) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
       String url =  urlStr+"yuanshensystem/approval/getmyapproval";
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
//        List<Approval> approvalList = JSONArray.parseArray(jsonArray.toJSONString(), Approval.class);
        return jsonArray;
    }


    //审核者查看待自己审批的
    public static JSONArray getPendApproval(int userId) {
        //Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        String url =urlStr + "yuanshensystem/approval/getpendapproval";
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);

        return jsonArray;
    }

    //  get pic
    public static JSONObject selectSignFile(int expenseId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("expenseId", expenseId);
        String url = urlStr + "yuanshensystem/sign/selectsignfile";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return  reJson;
    }



}
