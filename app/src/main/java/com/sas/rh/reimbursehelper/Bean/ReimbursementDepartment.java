package com.sas.rh.reimbursehelper.Bean;

public class ReimbursementDepartment {
    private Integer departmentId;

    private String departmentName;

    private Byte reimbursementRightId;

    private Integer companyId;

    private Double departmentQuota;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Byte getReimbursementRightId() {
        return reimbursementRightId;
    }

    public void setReimbursementRightId(Byte reimbursementRightId) {
        this.reimbursementRightId = reimbursementRightId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Double getDepartmentQuota() {
        return departmentQuota;
    }

    public void setDepartmentQuota(Double departmentQuota) {
        this.departmentQuota = departmentQuota;
    }
}