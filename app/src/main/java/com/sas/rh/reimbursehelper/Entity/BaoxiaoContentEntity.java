package com.sas.rh.reimbursehelper.Entity;

/**
 * Created by win on 2017/10/9.
 */

public class BaoxiaoContentEntity {

    private String bxtype;
    private String bxnum;
    private String bxdate;
    public boolean isSelect;

    public BaoxiaoContentEntity() {
    }

    public BaoxiaoContentEntity(String bxtype, String bxnum, String bxdate) {
        this.bxtype = bxtype;
        this.bxnum = bxnum;
        this.bxdate = bxdate;
    }

    public String getBxtype() {
        return bxtype;
    }

    public void setBxtype(String bxtype) {
        this.bxtype = bxtype;
    }

    public String getBxnum() {
        return bxnum;
    }

    public void setBxnum(String bxnum) {
        this.bxnum = bxnum;
    }

    public String getBxdate() {
        return bxdate;
    }

    public void setBxdate(String bxdate) {
        this.bxdate = bxdate;
    }


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }
}
