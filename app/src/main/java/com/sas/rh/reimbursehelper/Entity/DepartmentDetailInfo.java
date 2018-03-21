package com.sas.rh.reimbursehelper.Entity;

/**
 * Created by dh86 on 2018/1/1.
 */

public class DepartmentDetailInfo {
    private String department_id;
    private String department_name;
    private String department_gsid;
    private String department_masterid;
    private String department_state;
    private String department_createPersonid;

    public DepartmentDetailInfo() {
    }

    public DepartmentDetailInfo(String department_id, String department_name, String department_gsid, String department_masterid, String department_createTime, String department_createPersonid) {
        this.department_id = department_id;
        this.department_name = department_name;
        this.department_gsid = department_gsid;
        this.department_masterid = department_masterid;
        this.department_state = department_createTime;
        this.department_createPersonid = department_createPersonid;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getDepartment_name() {
        return department_name;
    }

    public void setDepartment_name(String department_name) {
        this.department_name = department_name;
    }

    public String getDepartment_gsid() {
        return department_gsid;
    }

    public void setDepartment_gsid(String department_gsid) {
        this.department_gsid = department_gsid;
    }

    public String getDepartment_masterid() {
        return department_masterid;
    }

    public void setDepartment_masterid(String department_masterid) {
        this.department_masterid = department_masterid;
    }

    public String getDepartment_state() {
        return department_state;
    }

    public void setDepartment_state(String department_state) {
        this.department_state = department_state;
    }

    public String getDepartment_createPersonid() {
        return department_createPersonid;
    }

    public void setDepartment_createPersonid(String department_createPersonid) {
        this.department_createPersonid = department_createPersonid;
    }
}
