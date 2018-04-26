package com.sas.rh.reimbursehelper.Bean.newbean;

import com.sas.rh.reimbursehelper.Bean.BaseBean;

/**
 * Created by liqing on 18/3/20.
 * 审批Bean
 */

public class ApprovalBean extends BaseBean {
    int id;

    String fee;
    String name;
    String remark;
    String date;
    String expenseNum;
    String departMent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpenseNum() {
        return expenseNum;
    }

    public void setExpenseNum(String expenseNum) {
        this.expenseNum = expenseNum;
    }

    public String getDepartMent() {
        return departMent;
    }

    public void setDepartMent(String departMent) {
        this.departMent = departMent;
    }
}
