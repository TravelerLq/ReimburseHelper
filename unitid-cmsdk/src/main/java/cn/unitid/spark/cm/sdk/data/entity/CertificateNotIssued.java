package cn.unitid.spark.cm.sdk.data.entity;

import cn.com.syan.spark.client.sdk.data.entity.AppBrief;
import cn.com.syan.spark.client.sdk.data.entity.MyCsr;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by brochexu on 2/5/15.
 */
public class CertificateNotIssued {
    private String rawSubject = null;
    private Map<String, String> subject = new HashMap<String, String>();
    private String serialNumber = null;
    private String nonce = null;
    private String openId = null;
    private String p10 = null;
    private String  algorithm;
    private  Integer status;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        if (subject.containsKey("CN")) {
            return subject.get("CN");
        } else {
            return "未命名";
        }
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Map<String, String> getSubject() {
        return subject;
    }

    public String getRawSubject() {
        return rawSubject;
    }

    public void setSubject(String subject) {
        this.rawSubject = subject;
        this.subject.clear();
        this.subject.putAll(parseSubject(subject));
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getP10() {
        return p10;
    }

    public void setP10(String p10) {
        this.p10 = p10;
    }



    public MyCsr getMyCsr() {
        MyCsr myCsr = new MyCsr();
        myCsr.setOpenid(openId);
        myCsr.setSerialNumber(serialNumber);
        myCsr.setNonce(nonce);
        myCsr.setP10(p10);
        myCsr.setSubject(rawSubject);
        myCsr.setAlgorithm(algorithm);
        myCsr.setStatus(status);
        return myCsr;
    }

    public CertificateNotIssued() {

    }

    public CertificateNotIssued(MyCsr myCsr, AppBrief appBrief) {
        this.serialNumber = myCsr.getSerialNumber();
        this.nonce = myCsr.getNonce();

        this.openId = myCsr.getOpenid();
        this.p10 = myCsr.getP10();
        this.rawSubject = myCsr.getSubject();
        this.subject.putAll(parseSubject(this.rawSubject));
        this.algorithm=myCsr.getAlgorithm();
        this.status=myCsr.getStatus();
    }

    public static Map<String, String> parseSubject(String subject) {
        Map<String, String> mSubject = new Hashtable<String, String>();
        String[] aSubject = subject.split(",");
        for (String item : aSubject) {
            String[] kv = item.split("=");
            if (kv.length == 2) {
                mSubject.put(kv[0].trim(), kv[1].trim());
            }
        }
        return mSubject;
    }
}
