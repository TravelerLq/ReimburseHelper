package com.sas.rh.reimbursehelper.Bean;

public class SingleReimbursement {
    private Integer expenseId;

    private Byte expenseItem;

    private Byte expenseCategory;

    private Double amount;

    private Byte formTemplateId;

    private Integer formId;

    private Byte accountingPolicyId;

    private String remark;

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Byte getExpenseItem() {
        return expenseItem;
    }

    public void setExpenseItem(Byte expenseItem) {
        this.expenseItem = expenseItem;
    }

    public Byte getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(Byte expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Byte getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(Byte formTemplateId) {
        this.formTemplateId = formTemplateId;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public Byte getAccountingPolicyId() {
        return accountingPolicyId;
    }

    public void setAccountingPolicyId(Byte accountingPolicyId) {
        this.accountingPolicyId = accountingPolicyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}