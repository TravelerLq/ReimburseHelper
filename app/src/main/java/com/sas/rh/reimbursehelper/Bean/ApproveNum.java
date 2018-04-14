package com.sas.rh.reimbursehelper.Bean;

import java.util.Date;

public class ApproveNum {
    private Integer approveNumId;

    private String approveNumName;

    private Integer approverId;

    private Byte approveNum;

    private Integer companyId;

    private Integer createPersonId;

    private Date createTime;

    private Integer updatePersonId;

    private Date updateTime;

    public Integer getApproveNumId() {
        return approveNumId;
    }

    public void setApproveNumId(Integer approveNumId) {
        this.approveNumId = approveNumId;
    }

    public String getApproveNumName() {
        return approveNumName;
    }

    public void setApproveNumName(String approveNumName) {
        this.approveNumName = approveNumName;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public Byte getApproveNum() {
        return approveNum;
    }

    public void setApproveNum(Byte approveNum) {
        this.approveNum = approveNum;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(Integer createPersonId) {
        this.createPersonId = createPersonId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(Integer updatePersonId) {
        this.updatePersonId = updatePersonId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}