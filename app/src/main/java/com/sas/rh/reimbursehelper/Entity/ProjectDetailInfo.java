package com.sas.rh.reimbursehelper.Entity;

/**
 * Created by dh86 on 2018/1/1.
 */

public class ProjectDetailInfo {
    private String project_id;
    private String project_name;
    private String project_bumenid;
    private String project_state;
    private String project_gsid;

    public ProjectDetailInfo() {
    }

    public ProjectDetailInfo(String project_id, String project_name, String project_bumenid, String project_state, String project_gsid) {
        this.project_id = project_id;
        this.project_name = project_name;
        this.project_bumenid = project_bumenid;
        this.project_state = project_state;
        this.project_gsid = project_gsid;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getProject_bumenid() {
        return project_bumenid;
    }

    public void setProject_bumenid(String project_bumenid) {
        this.project_bumenid = project_bumenid;
    }

    public String getProject_state() {
        return project_state;
    }

    public void setProject_state(String project_state) {
        this.project_state = project_state;
    }

    public String getProject_gsid() {
        return project_gsid;
    }

    public void setProject_gsid(String project_gsid) {
        this.project_gsid = project_gsid;
    }
}
