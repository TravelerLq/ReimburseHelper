package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;

public class ExpenseItemUtil {
    public static void main(String[] args) {
        select();

    }

    public static void select() {
        Integer userId = 1;
        String url = "http://localhost:8080/yuanshensystem/expenseitem/select";
        JSONObject jsonObject = new JSONObject();
        Byte expenseCategoryId = 23;
        jsonObject.put("expenseCategoryId", expenseCategoryId);
        jsonObject.put("userId", userId);
        JSONArray jsonArray = JsonUtil.uploadJsonGetJsonArray(url, jsonObject);
        System.out.println(ArrayUtils.toString(jsonArray));

    }
}
