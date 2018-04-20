package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Bean.ExpenseReimbursementForm;

import java.util.List;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;


public class FormUtil {
    //    public static void main(String[] args) {
//        addForm();
////          updateForm();
////        selectForm();
////        deleteForm();
////        selectDeptByComid();
////        getFormPdf();
//    }
    private static String bxdid;
    private static final String urlStr=AddressConfig.RootAddress;

    //增加一张新的报销单
    public static JSONArray addForm(Integer userId, Byte expenseCategoryId) {
        //报销人id
        //Integer reimbursementPersonId = 1;
        //报销的二级科目id
        //      Byte expenseCategoryId = 1;
//        Byte expenseCategoryId = 5;

        String url =urlStr+ "yuanshensystem/form/add";
        // String url = "http://101.200.85.207:8080/yuanshensystem/form/add";
        //String url = RootAddress+"yuanshensystem/form/add";
        JSONObject jsonObject = new JSONObject();
        //即用户id
        jsonObject.put("userId", userId);
        //报销的二级科目id
        jsonObject.put("expenseCategoryId", expenseCategoryId);
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        JSONObject reJson = jsonArray.getJSONObject(0);
        bxdid = reJson.get("formId").toString();
        return jsonArray;
//        List<DeptCategoryItemVo> deptCategoryItemVoList =
//                JSONArray.parseArray(jsonArray.toJSONString(), DeptCategoryItemVo.class);
//        JSONObject reJson = jsonArray.getJSONObject(0);
//        System.out.println(reJson);

    }

    public static String getBxdid() {
        return bxdid;
    }

    //更新报销单
    public static void updateForm() {
        //报销人id
        Integer formId = 44;
        String formName = "XXX的外出报销";
        String url = "http://localhost:8080/yuanshensystem/form/update";
        JSONObject jsonObject = new JSONObject();
        //即用户id
        jsonObject.put("formId", formId);
        //报销的二级科目id
        jsonObject.put("formName", formName);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson.toString());
    }

    //查询报销单
    public static void selectForm() {
        //报销人id
        Integer formId = 44;
        String url = "http://localhost:8080/yuanshensystem/form/select";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formId", formId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String formJson = reJson.getString("form");
        ExpenseReimbursementForm expenseReimbursementForm = JSON.parseObject(formJson, ExpenseReimbursementForm.class);
        System.out.println(expenseReimbursementForm.getFormName());
    }

    //删除报销单
    public static void deleteForm() {
        //报销人id
        Integer formId = 44;
        String url = "http://localhost:8080/yuanshensystem/form/delete";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formId", formId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);
    }


    //根据公司id查询报销单
    public static void selectDeptByComid() {
        //报销人id
        Integer companyId = 3;
        String url = "http://localhost:8080/yuanshensystem/form/selectformbycompanyid";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyId", companyId);
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        List<ExpenseReimbursementForm> expenseReimbursementFormList =
                JSONArray.parseArray(jsonArray.toJSONString(), ExpenseReimbursementForm.class);
        System.out.println(expenseReimbursementFormList.get(0).getCompanyId());
    }

    //获取报销单的pdf
    public static JSONObject getFormPdf(int formid,int userId) {
        //报销人id (42)
        Integer formId = formid;
        String url =urlStr + "yuanshensystem/form/getpdf";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formId", formId);
        jsonObject.put("userId",userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        //这里得到的是报销单的附件id，根据这个附件id，调用下载文件的方法去下载pdf文件
        Integer annexId = reJson.getInteger("annexId");
        System.out.println(annexId);
        return reJson;
    }
}
