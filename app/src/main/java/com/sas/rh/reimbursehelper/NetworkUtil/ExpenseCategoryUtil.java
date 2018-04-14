package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.ArrayUtils;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;
import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.TestAddress;
//获取二级科目类别
public class ExpenseCategoryUtil {
//    public static void main(String[] args) {
//        select();
//    }

    public static JSONArray select(Integer userId) {
        String url = "http://101.200.85.207:8080/yuanshensystem/expensecategory/select";
        String testUrl = RootAddress+"yuanshensystem/expensecategory/select";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", userId);
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(testUrl, jsonObject);
        return jsonArray;
        //System.out.println(ArrayUtils.toString(jsonArray));

    }
}
