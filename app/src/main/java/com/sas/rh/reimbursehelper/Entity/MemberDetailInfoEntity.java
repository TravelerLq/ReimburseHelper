package com.sas.rh.reimbursehelper.Entity;

/**
 * Created by dh86 on 2018/1/4.
 */

public class MemberDetailInfoEntity {
    private String member_id;
    private String member_name;
    private String member_sex;
    private String member_telnum;
    private String member_mail;
    private String member_birthday;
    private String member_enterday;
    private String member_gsid;
    private String SortLetters;

    public MemberDetailInfoEntity() {
    }

    public MemberDetailInfoEntity(String member_id, String member_name, String member_sex, String member_telnum, String member_mail, String member_birthday, String member_enterday, String member_gsid) {
        this.member_id = member_id;
        this.member_name = member_name;
        this.member_sex = member_sex;
        this.member_telnum = member_telnum;
        this.member_mail = member_mail;
        this.member_birthday = member_birthday;
        this.member_enterday = member_enterday;
        this.member_gsid = member_gsid;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_sex() {
        return member_sex;
    }

    public void setMember_sex(String member_sex) {
        this.member_sex = member_sex;
    }

    public String getMember_telnum() {
        return member_telnum;
    }

    public void setMember_telnum(String member_telnum) {
        this.member_telnum = member_telnum;
    }

    public String getMember_mail() {
        return member_mail;
    }

    public void setMember_mail(String member_mail) {
        this.member_mail = member_mail;
    }

    public String getMember_birthday() {
        return member_birthday;
    }

    public void setMember_birthday(String member_birthday) {
        this.member_birthday = member_birthday;
    }

    public String getMember_enterday() {
        return member_enterday;
    }

    public void setMember_enterday(String member_enterday) {
        this.member_enterday = member_enterday;
    }

    public String getMember_gsid() {
        return member_gsid;
    }

    public void setMember_gsid(String member_gsid) {
        this.member_gsid = member_gsid;
    }

    public String getSortLetters() {
        return SortLetters;
    }

    public void setSortLetters(String sortLetters) {
        SortLetters = sortLetters;
    }
}
