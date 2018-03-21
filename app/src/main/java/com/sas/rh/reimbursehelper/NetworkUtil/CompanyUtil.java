package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Entity.Company;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

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

        String url = RootAddress+"yuanshensystem/company/add";
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
    public static JSONObject updateCompany(Integer companyId,String companyName,Integer updatePersonId) {
//        //公司id
//        Integer companyId = 1;
//        //公司名字
//        String companyName = "南京苏宁云商集团";
//        //操作者id
//        Integer updatePersonId = 1;
        String url = RootAddress+"yuanshensystem/company/update";
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
    public static void selectCompany() {
        //公司id
        Integer companyId = 1;
        String url = "http://localhost:8080/yuanshensystem/company/select";
        Integer userId = 1;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("companyId", companyId);
        jsonObject.put("userId", userId);

        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String companyJson = reJson.getString("company");
        Company company = JSON.parseObject(companyJson, Company.class);
        System.out.println(company.getCompanyName());

    }

}
