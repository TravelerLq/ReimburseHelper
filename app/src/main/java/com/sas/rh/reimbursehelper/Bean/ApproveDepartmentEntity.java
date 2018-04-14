package com.sas.rh.reimbursehelper.Bean;

/**
 * Created by dh on 17-11-24.
 */

public class ApproveDepartmentEntity {

    String depatment_ID;
    String department_Name;
    String department_IsActived;

    public ApproveDepartmentEntity() {
    }

    public ApproveDepartmentEntity(String depatment_ID, String department_Name) {
        this.depatment_ID = depatment_ID;
        this.department_Name = department_Name;
    }

    public ApproveDepartmentEntity(String depatment_ID, String department_Name, String department_IsActived) {
        this.depatment_ID = depatment_ID;
        this.department_Name = department_Name;
        this.department_IsActived = department_IsActived;
    }

    public String getDepatment_ID() {
        return depatment_ID;
    }

    public void setDepatment_ID(String depatment_ID) {
        this.depatment_ID = depatment_ID;
    }

    public String getDepartment_Name() {
        return department_Name;
    }

    public void setDepartment_Name(String department_Name) {
        this.department_Name = department_Name;
    }

    public String getDepartment_IsActived() {
        return department_IsActived;
    }

    public void setDepartment_IsActived(String department_IsActived) {
        this.department_IsActived = department_IsActived;
    }
}
