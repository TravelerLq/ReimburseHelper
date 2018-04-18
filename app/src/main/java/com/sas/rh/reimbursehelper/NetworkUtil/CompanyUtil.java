package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Bean.Company;

import java.util.List;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;
import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.TestAddress;

public class CompanyUtil {
//    public static void main(String[] args) {
////        addCompany();
////        updateCompany();
//        selectCompany();
//    }


    //增加一条公司信息
    public static JSONObject addCompany(String companyName,
                                        String companyNature,
                                        String vatCollectionMethods,
                                        String incomeTaxCollectionMethods,
                                        String taxId,
                                        String openingBank,
                                        String bankAccount,
                                        String address,
                                        String telephone,
                                        String invoiceMethod,
                                        String legalName,
                                        String legalIdNumber,
                                        Integer createPersonId) {
//        //公司名字
//        String companyName = "南京御安神科技有限公司";
//        //公司性质
//        String companyNature = "股份制公司";
//        //增值税增收方式
//        String vatCollectionMethods = "普通";
//        //所得税增收方式
//        String incomeTaxCollectionMethods = "一般";
//        //公司税号
//        String taxId = "123457654567";
//        //开户银行
//        String openingBank = "上海浦东发展银行";
//        //银行账号
//        String bankAccount = "00000002333333";
//        //公司地址
//        String address = "南京市栖霞区苏宁大道福中集团";
//        //公司电话
//        String telephone = "12345678900";
//        //开票方式
//        String invoiceMethod = "电子发票";
//        //法人名字
//        String legalName = "屠正松";
//        //法人身份证号
//        String legalIdNumber = "320113199309876890";
//        //操作者id，即提交者id
//        Integer createPersonId = 1;

        String url = RootAddress + "yuanshensystem/company/add";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyName", companyName);
        jsonObject.put("companyNature", companyNature);
        jsonObject.put("vatCollectionMethods", vatCollectionMethods);
        jsonObject.put("incomeTaxCollectionMethods", incomeTaxCollectionMethods);
        jsonObject.put("taxId", taxId);
        jsonObject.put("openingBank", openingBank);
        jsonObject.put("bankAccount", bankAccount);
        jsonObject.put("address", address);
        jsonObject.put("telephone", telephone);
        jsonObject.put("invoiceMethod", invoiceMethod);
        jsonObject.put("legalName", legalName);
        jsonObject.put("legalIdNumber", legalIdNumber);
        jsonObject.put("createPersonId", createPersonId);

        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;
        //System.out.println(reJson);
    }


    //更新一条公司信息
    public static JSONObject updateCompany(Integer companyId, String companyName, Integer updatePersonId) {
//        //公司id
//        Integer companyId = 1;
//        //公司名字
//        String companyName = "南京苏宁云商集团";
//        //操作者id
//        Integer updatePersonId = 1;
        String url = RootAddress + "yuanshensystem/company/update";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyId", companyId);
        jsonObject.put("companyName", companyName);
        jsonObject.put("updatePersonId", updatePersonId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;
        //System.out.println(reJson);

    }


    //删除一条公司信息
    public static void deleteCompany() {
        //公司id
        Integer companyId = 1;
        String url = "http://localhost:8080/yuanshensystem/company/delete";
        Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyId", companyId);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        System.out.println(reJson);

    }

    //查看一条公司信息

    public static JSONObject selectCompany(int companyId, int userId) {
        //公司id
        // Integer companyId = 1;
        String url = RootAddress + "yuanshensystem/company/select";
        // Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyId", companyId);
        jsonObject.put("userId", userId);

        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String companyJson = reJson.getString("company");
        Company company = JSON.parseObject(companyJson, Company.class);
        // System.out.println(company.getCompanyName());
        return reJson;

    }


    public static JSONArray selectLike(String companyName, int userId) {
        //公司id
//        String companyName = "御安神";
        String url = RootAddress + "yuanshensystem/company/selectlikebyname";
//        Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyName", companyName);
        jsonObject.put("userId", userId);
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        //  List<Company> companyList = JSONArray.parseArray(jsonArray.toJSONString(), Company.class);
        return jsonArray;

    }

    //申请加入公司
    public static JSONObject joinCompany(int companyId, int userId) {
//        //公司id
//        Integer companyId = 3;
//        //用户ID
//        Integer userId = 6;
        String url = RootAddress + "yuanshensystem/company/joincompany";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyId", companyId);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;

    }

    //对申请人员做操作
    public static JSONObject applicantApproval(int userId, int result) {
//        //公司id
//        Integer companyId = 3;
//        //用户ID
//        Integer userId = 6;
        String url = RootAddress + "yuanshensystem/company/joinprocess";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;

    }

    public static JSONObject joinProcess(int userId, int result, int noticeId) {
//         Integer userId = 1;
//        Integer noticeId = 7;
//        Integer result = 1;

        String url = RootAddress + "yuanshensystem/user/dealwithjoincompany";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("noticeId", noticeId);
        jsonObject.put("result", result);

        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;

    }

    //  获取消息
    public static JSONArray getMsg(int userId) {
//        //公司id
//        Integer companyId = 3;
//        //用户ID
//        Integer userId = 6;
        String url = RootAddress + "yuanshensystem/notice/select";
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("userId", userId);
        JSONArray reJson = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        return reJson;

    }

    public static JSONObject joinCompany(int userId, String shareCode) {
//        Integer userId = 20;
//        String shareCode = "weyq7yabx";
        String url = RootAddress + "yuanshensystem/public/joincompany";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        jsonObject.put("shareCode", shareCode);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;


    }

    public static JSONObject getShareCode(int userId) {
       // Integer userId = 1;
        String url = RootAddress+"yuanshensystem/public/getsharecode";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        return reJson;

    }

}
