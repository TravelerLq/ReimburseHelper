package com.sas.rh.reimbursehelper.Bean.newbean;

import com.sas.rh.reimbursehelper.Bean.BaseBean;

/**
 * Created by liqing on 18/5/3.
 */

public class DepartmentBean extends BaseBean {

    /**
     * departmentId : 52
     * departmentName : 销售部
     * deptLeaderId : 54
     * deptLeaderName : 王朕
     * numberOfEmployees : 1
     */

    private int departmentId;
    private String departmentName;
    private int deptLeaderId;
    private String deptLeaderName;
    private int numberOfEmployees;

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public int getDeptLeaderId() {
        return deptLeaderId;
    }

    public void setDeptLeaderId(int deptLeaderId) {
        this.deptLeaderId = deptLeaderId;
    }

    public String getDeptLeaderName() {
        return deptLeaderName;
    }

    public void setDeptLeaderName(String deptLeaderName) {
        this.deptLeaderName = deptLeaderName;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }
}
