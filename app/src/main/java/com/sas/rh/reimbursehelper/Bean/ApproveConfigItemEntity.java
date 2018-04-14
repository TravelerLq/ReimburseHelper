package com.sas.rh.reimbursehelper.Bean;

/**
 * Created by dh on 17-11-29.
 */

public class ApproveConfigItemEntity {

    private String approver_Name_tv;
    private String approver_ID_tv;
    private String job_Name_tv;
    private int approve_num;

    public ApproveConfigItemEntity() {
    }

    public ApproveConfigItemEntity(String approver_Name_tv, String approver_ID_tv, String job_Name_tv) {
        this.approver_Name_tv = approver_Name_tv;
        this.approver_ID_tv = approver_ID_tv;
        this.job_Name_tv = job_Name_tv;
    }

    public String getApprover_Name_tv() {
        return approver_Name_tv;
    }

    public void setApprover_Name_tv(String approver_Name_tv) {
        this.approver_Name_tv = approver_Name_tv;
    }

    public String getJob_Name_tv() {
        return job_Name_tv;
    }

    public void setJob_Name_tv(String job_Name_tv) {
        this.job_Name_tv = job_Name_tv;
    }

    public String getApprover_ID_tv() {
        return approver_ID_tv;
    }

    public void setApprover_ID_tv(String approver_ID_tv) {
        this.approver_ID_tv = approver_ID_tv;
    }

    public int getApprove_num() {
        return approve_num;
    }

    public void setApprove_num(int approve_num) {
        this.approve_num = approve_num;
    }
}
