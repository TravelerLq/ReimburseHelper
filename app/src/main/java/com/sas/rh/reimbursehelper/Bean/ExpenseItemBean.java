package com.sas.rh.reimbursehelper.Bean;

/**
 * Created by liqing on 18/3/20.
 */

public class ExpenseItemBean extends BaseBean {
    String title;
    String fee;
    String remark;
    String picStr;
    public String getPicStr() {
        return picStr;
    }

    public void setPicStr(String picStr) {
        this.picStr = picStr;
    }


    public ExpenseItemBean(String title, String fee, String remark, String picStr) {
        this.title = title;
        this.fee = fee;
        this.remark = remark;
        this.picStr = picStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
