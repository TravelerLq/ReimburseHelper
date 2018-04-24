package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

/**
 * @author tuzhengsong
 */
public class DepartmentUtil {

    public static void main(String[] args) {
//        addDepartment();
//        updateDepartment();
//        selectDepartment();
//        deleteDepartment();
//        selectSingle();

    }

    //新增一个部门
    public static JSONObject addDepartment(String departmentName, Byte reimbursementRightId, Double departmentQuota, Integer userId) {
//        //公司id
//        Integer companyId = 3;
//        //部门名字
//        String departmentName = "财务管理";
//        //部门的报销权限id
//        Byte reimbursementRightId = 1;
//        //部门限额
//        Double departmentQuota = 1000044.00;
//        //操作者
//        Integer userId = 1;

        String url = RootAddress + "yuanshensystem/department/add";
        JSONObject jsonObject = new JSONObject();
        // jsonObject.put("userId", companyId);
        jsonObject.put("departmentName", departmentName);
        jsonObject.put("reimbursementRightId", reimbursementRightId);
        jsonObject.put("departmentQuota", departmentQuota);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;
        //System.out.println(reJson);
    }

    //更新一个部门
    public static JSONObject updateDepartment(Integer departmentId, String departmentName) {
//        Integer departmentId = 13;
//        String departmentName = "车间管理";
        String url = RootAddress + "yuanshensystem/department/update";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("departmentId", departmentId);
        jsonObject.put("departmentName", departmentName);

        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;
    }

    //删除一个部门
    public static JSONObject deleteDepartment(Integer departmentId, Integer userId) {
//        Integer departmentId = 10;
//        Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("departmentId", departmentId);
        jsonObject.put("userId", userId);
        String url = RootAddress + "yuanshensystem/department/delete";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;
        //System.out.println(reJson);
    }

    //查询单个部门
    public static JSONObject selectSingle(Integer departmentId, Integer userId) {
//        Integer departmentId = 11;
//        Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("departmentId", departmentId);
        jsonObject.put("userId", userId);
        String url = RootAddress + "yuanshensystem/department/select";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
//        String reimbursementDepartmentJson = reJson.getString("reimbursementDepartment");
//       ReimbursementDepartment reimbursementDepartment = JSON.parseObject(reimbursementDepartmentJson, ReimbursementDepartment.class);

//        System.out.println(reimbursementDepartment.getDepartmentName());
        return reJson;
    }

    //查询公司的所有部门
    public static JSONArray selectDepartment(Integer userId) {
//        Integer companyId = 3;
//        Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        String url = RootAddress + "yuanshensystem/department/selectdeptbycompanyid";
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        return jsonArray;
//        List<ReimbursementDepartment> reimbursementDepartmentList = JSONArray.parseArray(jsonArray.toJSONString(), ReimbursementDepartment.class);
//        System.out.println(reimbursementDepartmentList.get(1).getDepartmentName());

    }

    public static JSONObject joinDept(int userId, int departmentId) {
//        Integer userId = 28;
//        Integer departmentId = 2;
        String url = RootAddress + "yuanshensystem/public/joindept";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("departmentId", departmentId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;

    }

    //查询部门的报销权限
    public static JSONArray getDeptRight(int userId) {
        // Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        String url = RootAddress + "yuanshensystem/department/selectdeptright";
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        return jsonArray;

        //   List<ReimbursementRight> reimbursementRightList = JSONArray.parseArray(jsonArray.toJSONString(), ReimbursementRight.class);

    }

}
