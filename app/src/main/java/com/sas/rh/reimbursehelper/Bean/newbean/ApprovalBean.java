package com.sas.rh.reimbursehelper.Bean.newbean;

import com.sas.rh.reimbursehelper.Bean.BaseBean;

/**
 * Created by liqing on 18/3/20.
 * 审批Bean
 */

public class ApprovalBean extends BaseBean {


    /**
     * approvalId : 450
     * count : 1
     * date : 2018-04-26
     * formId : 1373
     * money : 23.66
     * process : 部门审批
     * userName : 王朕
     */

    private int approvalId;
    private int count;
    private String date;
    private int formId;
    private String money;
    private String process;
    private String userName;

    public int getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(int approvalId) {
        this.approvalId = approvalId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
