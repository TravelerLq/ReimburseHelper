package com.sas.rh.reimbursehelper.Bean;

/**
 * Created by liqing on 18/3/30.
 */

public class ResponseApprovalDetailBean extends BaseBean {


    /**
     * amount : 3000.0
     * expenseCategory : 0
     * expenseId : 5
     * expenseItem : 4
     * formId : 232
     * remark :  采购时间:2018.02.26、采购地点:福中科技园、采购明细：测试
     */

    private double amount;
    private int expenseCategory;
    private int expenseId;
    private int expenseItem;
    private int formId;
    private String remark;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(int expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public int getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(int expenseId) {
        this.expenseId = expenseId;
    }

    public int getExpenseItem() {
        return expenseItem;
    }

    public void setExpenseItem(int expenseItem) {
        this.expenseItem = expenseItem;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
