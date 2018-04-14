package com.sas.rh.reimbursehelper.Bean;

/**
 * Created by dh86 on 2017/12/28.
 */

public class EnterpriseDetailInfoEntity {
    private String cname;
    private String taxnum;
    private String gsxz;
    private String zztax;
    private String sdtax;
    private String kpfs;
    private String khbankname;
    private String khbankid;
    private String caddr;
    private String ctel;
    private String ccid;
    private String climit;

    public EnterpriseDetailInfoEntity() {
    }

    public EnterpriseDetailInfoEntity(String cname, String taxnum, String gsxz, String zztax, String sdtax, String kpfs, String khbankname, String khbankid, String caddr, String ctel, String ccid, String climit) {
        this.cname = cname;
        this.taxnum = taxnum;
        this.gsxz = gsxz;
        this.zztax = zztax;
        this.sdtax = sdtax;
        this.kpfs = kpfs;
        this.khbankname = khbankname;
        this.khbankid = khbankid;
        this.caddr = caddr;
        this.ctel = ctel;
        this.ccid = ccid;
        this.climit = climit;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getTaxnum() {
        return taxnum;
    }

    public void setTaxnum(String taxnum) {
        this.taxnum = taxnum;
    }

    public String getGsxz() {
        return gsxz;
    }

    public void setGsxz(String gsxz) {
        this.gsxz = gsxz;
    }

    public String getZztax() {
        return zztax;
    }

    public void setZztax(String zztax) {
        this.zztax = zztax;
    }

    public String getSdtax() {
        return sdtax;
    }

    public void setSdtax(String sdtax) {
        this.sdtax = sdtax;
    }

    public String getKpfs() {
        return kpfs;
    }

    public void setKpfs(String kpfs) {
        this.kpfs = kpfs;
    }

    public String getKhbankname() {
        return khbankname;
    }

    public void setKhbankname(String khbankname) {
        this.khbankname = khbankname;
    }

    public String getKhbankid() {
        return khbankid;
    }

    public void setKhbankid(String khbankid) {
        this.khbankid = khbankid;
    }

    public String getCaddr() {
        return caddr;
    }

    public void setCaddr(String caddr) {
        this.caddr = caddr;
    }

    public String getCtel() {
        return ctel;
    }

    public void setCtel(String ctel) {
        this.ctel = ctel;
    }

    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }

    public String getClimit() {
        return climit;
    }

    public void setClimit(String climit) {
        this.climit = climit;
    }
}
