package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.constant.Constant;

import java.util.List;

import static com.sas.rh.reimbursehelper.constant.Constant.RootAddress;
import static com.sas.rh.reimbursehelper.constant.Constant.TestAddress;

public class UserUtil {

    //新增一条用户信息
    public static JSONObject addUser(String userPhone, String name, String userMail, String idCardNumber,
                                     String userName, String userPwd) {
        //公司id
//        Integer companyId = 3;
//        //部门id
//        Integer departmentId = 4;
        //手机号
        //String userPhone = "1234567849";
        //名字
//        String name = "鲁道夫";
//        //邮箱
//        String userMail = "testtdsdst@qq.com";
        //角色id
//        Byte roleId = 2;

        String url = RootAddress + "yuanshensystem/user/add";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userPhone", userPhone);
        jsonObject.put("name", name);
        jsonObject.put("userMail", userMail);
        jsonObject.put("idCardNumber", idCardNumber);
        jsonObject.put("userName", userName);
        jsonObject.put("userPwd", userPwd);


        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);
        return reJson;
    }


    //更新一条用户信息
    public static void updateUser() {
        Integer userId = 3;
        Byte roleId = 3;
        String name = "里斯";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("roleId", roleId);
        jsonObject.put("name", name);
        String url = "http://localhost:8080/yuanshensystem/user/update";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);
    }

    //删除用户信息
    public static void deleteUser(int userId) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        String url = RootAddress + "yuanshensystem/user/delete";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);
    }

    //根据 身份证ID，查询单条用户信息
    public static JSONObject selectbyidcardnumber(String idCardNumber) {
        //String idCardNumber = "320113199310156418";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idCardNumber", idCardNumber);
        String url = RootAddress + "yuanshensystem/user/selectbyidcardnumber";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
//        String userJson = reJson.getString("user");
//        User user = JSON.parseObject(userJson, User.class);
        return reJson;
    }

    //查询单条用户信息
    public static void selectUser() {
        Integer userId = 3;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        String url = "http://localhost:8080/yuanshensystem/user/select";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String userJson = reJson.getString("user");
        UserBean user = JSON.parseObject(userJson, UserBean.class);
        System.out.println(user.getName());
    }

    //根据公司id获得所有用户信息
    public static JSONArray getALlUser(int userId) {
//        Integer companyId = 3;
//        Integer userId = 1;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        String url = RootAddress + "yuanshensystem/user/selectbycompanyid";
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        //这里得到一个用户信息的list集合
        List<UserBean> userList = JSONArray.parseArray(jsonArray.toJSONString(), UserBean.class);
        System.out.println(userList.toString());
        return jsonArray;
    }

    //根据部门id获得部门员工信息
    public static void getDepartmentUser() {
        Integer companyId = 3;
        Integer departmentId = 4;
        Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("companyId", companyId);
        jsonObject.put("departmentId", departmentId);
        String url = RootAddress + "yuanshensystem/user/selectbydeptid";
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        //这里得到一个用户信息的list集合
        List<UserBean> userList = JSONArray.parseArray(jsonArray.toJSONString(), UserBean.class);
        System.out.println(userList.get(1).getName());
    }
}
