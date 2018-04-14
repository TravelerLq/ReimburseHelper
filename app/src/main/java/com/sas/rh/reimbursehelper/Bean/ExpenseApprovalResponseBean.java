package com.sas.rh.reimbursehelper.Bean;

import java.util.Date;

/**
 * Created by liqing on 18/3/26.
 * 查看报销单信息返回
 */

public class ExpenseApprovalResponseBean extends BaseBean {

    /**
     * approvalId : 22
     * approvalName : 屠正松招待费
     * approveNumId : 6
     * approveProcessId : 1
     * approveResultId : 1
     * companyId : 3
     * createTime : 1519899141000
     * formId : 232
     * updateTime : 1519899141000
     * userId : 1
     * finallyResultId : 2
     * rejectReason : 审核不通过，票面信息不完整
     */

    private Integer approvalId;

    private String approvalName;

    private Integer userId;

    private Integer formId;

    private Integer approveNumId;

    private Byte approveProcessId;

    private Byte approveResultId;

    private String rejectReason;

    private Date createTime;

    private Date updateTime;

    private Integer companyId;

    private Byte finallyResultId;

    public Integer getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public Integer getApproveNumId() {
        return approveNumId;
    }

    public void setApproveNumId(Integer approveNumId) {
        this.approveNumId = approveNumId;
    }

    public Byte getApproveProcessId() {
        return approveProcessId;
    }

    public void setApproveProcessId(Byte approveProcessId) {
        this.approveProcessId = approveProcessId;
    }

    public Byte getApproveResultId() {
        return approveResultId;
    }

    public void setApproveResultId(Byte approveResultId) {
        this.approveResultId = approveResultId;
    }

    public String getRejectReason() {
        return rejectReason;
    }

    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Byte getFinallyResultId() {
        return finallyResultId;
    }

    public void setFinallyResultId(Byte finallyResultId) {
        this.finallyResultId = finallyResultId;
    }
}
