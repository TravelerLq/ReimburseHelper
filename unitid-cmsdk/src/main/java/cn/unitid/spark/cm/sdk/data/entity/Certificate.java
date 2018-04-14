package cn.unitid.spark.cm.sdk.data.entity;


import java.security.cert.CertificateEncodingException;
import java.util.Date;

import cn.com.syan.jcee.cm.impl.AbstractX509Certificate;
import cn.com.syan.jcee.cm.impl.ICertificate;
import cn.com.syan.jcee.cm.impl.PublicKeyAliasUtil;
import cn.com.syan.jcee.common.sdk.utils.CertificateStandardizedUtil;

/**
 * 证书信息
 */
public class Certificate {
    private static final String TAG = Certificate.class.getName();

    private String id = null;
    private AbstractX509Certificate x509Certificate = null;
    private String name = null;
    private String alias = null;
    private Date notAfter = null;
    private Date notBefore = null;
    private String serialNumber = null;
    private String subject = null;
    private String issuer = null;
    private String fingerPrint = null;
    private boolean isCa = false;
    private boolean isPrivateKeyAccessible = false;

    private boolean isDouble = false;
    private String encCertId=null;

    /**
     * 是否双证书
     * @return boolean
     */
    public boolean isDouble() {
        return isDouble;
    }

    public void setDouble(boolean isDouble) {
        this.isDouble = isDouble;
    }

    /**
     * 获取加密证书ID
     * @return String
     */
    public String getEncCertId() {
        return encCertId;
    }

    public void setEncCertId(String encCertId) {
        this.encCertId = encCertId;
    }

    public String getId() {
        return id;
    }

    /**
     * 获取X509证书对象
     * @return AbstractX509Certificate
     */
    public AbstractX509Certificate getX509Certificate() {
        return x509Certificate;
    }

    /**
     * 是否CA证书
     * @return boolean
     */
    public boolean isCa() {
        return isCa;
    }

    /**
     * 私钥是否可以访问
     * @return boolean
     */
    public boolean isPrivateKeyAccessible() {
        return isPrivateKeyAccessible;
    }

    /**
     * 证书名称
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * 证书别名
     * @return String 别名字符串
     */
    public String getAlias() {
        return alias;
    }

    /**
     * 证书有效截止日期
     * @return Date
     */
    public Date getNotAfter() {
        return notAfter;
    }

    /**
     * 证书有效开始日期
     * @return Date
     */
    public Date getNotBefore() {
        return notBefore;
    }

    /**
     * 证书序列号
     * @return String
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * 证书主题项
     * @return String
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 证书签发者
     * @return String
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * 证书指纹
     * @return String
     */
    public String getFingerPrint() {
        return fingerPrint;
    }

    /**
     * 构造器
     * @param certificate
     */
    public Certificate(ICertificate certificate) {
        if(certificate!=null) {
            this.x509Certificate =(AbstractX509Certificate) certificate;
            this.id = PublicKeyAliasUtil.getAlias(certificate.getX509Certificate());
            this.name = certificate.getCertificateName();
            this.alias = certificate.getAlias();
            java.security.cert.X509Certificate x509certificate = certificate.getX509Certificate();
            this.notAfter = x509certificate.getNotAfter();
            this.notBefore = x509certificate.getNotBefore();
            this.serialNumber = x509certificate.getSerialNumber().toString(16);
            try {
                this.subject = CertificateStandardizedUtil.getSubject(x509Certificate.getX509Certificate());
            } catch (CertificateEncodingException e) {

                this.subject = "";
            }
            try {
                this.issuer = CertificateStandardizedUtil.getIssuer(x509Certificate.getX509Certificate());
            } catch (CertificateEncodingException e) {

                this.issuer = "";
            }
            try {
                this.fingerPrint = x509Certificate.getFingerprint(null);
            } catch (Exception e) {
                this.fingerPrint = "";
            }
            this.isCa = x509Certificate.isCA();
            this.isPrivateKeyAccessible = certificate.isPrivateKeyAccessible();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
