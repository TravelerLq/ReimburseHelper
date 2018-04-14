package com.sas.rh.reimbursehelper.Bean;

import java.io.Serializable;

/**
 * Created by IMAC86 on 2017/11/1.
 */

public class PersonnameAndHeadimageEntity implements Serializable {
    String pname;
    String imagepath;
    String StaffID;


    public PersonnameAndHeadimageEntity() {
    }

    public PersonnameAndHeadimageEntity(String pname, String imagepath) {
        this.pname = pname;
        this.imagepath = imagepath;
    }

    public PersonnameAndHeadimageEntity(String pname, String imagepath, String staffID) {
        this.pname = pname;
        this.imagepath = imagepath;
        StaffID = staffID;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getStaffID() {
        return StaffID;
    }

    public void setStaffID(String staffID) {
        StaffID = staffID;
    }
}
