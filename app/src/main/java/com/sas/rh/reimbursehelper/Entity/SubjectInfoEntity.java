package com.sas.rh.reimbursehelper.Entity;

/**
 * Created by dh86 on 2017/12/31.
 */

public class SubjectInfoEntity {

    private String subject_id;
    private String subject_name;
    private String subject_state;
    private String subject_gsid;

    public SubjectInfoEntity() {
    }

    public SubjectInfoEntity(String subject_id, String subject_name, String subject_state, String subject_gsid) {
        this.subject_id = subject_id;
        this.subject_name = subject_name;
        this.subject_state = subject_state;
        this.subject_gsid = subject_gsid;
    }

    public String getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(String subject_id) {
        this.subject_id = subject_id;
    }

    public String getSubject_name() {
        return subject_name;
    }

    public void setSubject_name(String subject_name) {
        this.subject_name = subject_name;
    }

    public String getSubject_state() {
        return subject_state;
    }

    public void setSubject_state(String subject_state) {
        this.subject_state = subject_state;
    }

    public String getSubject_gsid() {
        return subject_gsid;
    }

    public void setSubject_gsid(String subject_gsid) {
        this.subject_gsid = subject_gsid;
    }
}
