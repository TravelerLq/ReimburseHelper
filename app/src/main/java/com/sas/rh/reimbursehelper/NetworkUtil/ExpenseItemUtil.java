package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.ArrayUtils;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

public class ExpenseItemUtil {
    public static void main(String[] args) {
        // select();

    }

    public static JSONArray getThirdCategory(int userId, Byte expenseCategoryId) {
//        Integer userId = 1;
        String url = RootAddress + "yuanshensystem/expenseitem/select";
        JSONObject jsonObject = new JSONObject();
//        Byte expenseCategoryId = 23;
        jsonObject.put("expenseCategoryId", expenseCategoryId);
        jsonObject.put("userId", userId);
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        return jsonArray;
        // System.out.println(ArrayUtils.toString(jsonArray));

    }
}
