package com.sas.rh.reimbursehelper.Entity;

/**
 * Created by liqing on 18/3/20.
 */

public class TestBean {
    //二级 报销类型

    /**
     * expenseCategoryId : 1
     * expenseCategoryName : 会议费
     */

    private int expenseCategoryId;
    private String expenseCategoryName;

    public int getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(int expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }

    public String getExpenseCategoryName() {
        return expenseCategoryName;
    }

    public void setExpenseCategoryName(String expenseCategoryName) {
        this.expenseCategoryName = expenseCategoryName;
    }
}
