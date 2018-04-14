package cn.unitid.spark.cm.sdk.business;

import android.util.Log;

import org.spongycastle.asn1.x509.Extension;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.syan.jcee.cm.exception.JCEECMException;
import cn.com.syan.jcee.cm.impl.CertificateStore;
import cn.com.syan.jcee.cm.impl.ICertificate;
import cn.com.syan.jcee.cm.impl.PKCS10CertificationRequest;
import cn.com.syan.jcee.cm.impl.PublicKeyAliasUtil;
import cn.com.syan.jcee.cm.impl.X509CertificateArrays;
import cn.com.syan.jcee.common.impl.SparkSignature;
import cn.com.syan.jcee.common.impl.pkcs7.PKCS7Signature;
import cn.com.syan.jcee.common.impl.utils.CertificateConverter;
import cn.com.syan.jcee.common.sdk.key.PKCS5PBES2;
import cn.com.syan.jcee.common.sdk.utils.CertificateStandardizedUtil;
import cn.com.syan.jcee.utils.StringConverter;
import cn.com.syan.jcee.utils.codec.binary.Base64;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.com.syan.spark.client.sdk.data.response.Response;
import cn.com.syan.spark.client.sdk.service.BaseService;
import cn.unitid.spark.cm.sdk.common.CertificateFilterCaConstrain;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.data.response.ObjectResponse;

/**
 * 证书管理
 */
public class CBSCertificateStore {
    private static final String TAG = CBSCertificateStore.class.getName();
    private CertificateStore store = null;
    private MyComparator comparator = new MyComparator();

    private static  CBSCertificateStore cbsStore=null;
    /**
     * 构造器使用前请先初始化Application  SparkApplication.init(Activity.getApplication());
     */
    private CBSCertificateStore() {
        File path = SparkApplication.getExampleApplicationContext().getFilesDir();
        String certpath = path.getAbsolutePath() + "/certificate.store";

        System.out.println("=CBSCertificateStore============="+certpath);
        store = CertificateStore.getInstance(certpath);
    }

    public static  CBSCertificateStore getInstance(){
                if(cbsStore==null){
                    cbsStore=new CBSCertificateStore();
                }
        return cbsStore;
    }

    public ObjectResponse getCertificate(java.security.cert.X509Certificate x509Certificate) {
        ObjectResponse response = null;
        try {
            store.open();
            ICertificate x509 = store.getCertificate(x509Certificate);
            Certificate certificate = new Certificate(x509);
            response = new ObjectResponse(BaseService.MSG_SUCCESS, "", certificate);
        } catch (Exception e) {
            response = new ObjectResponse(BaseService.MSG_FAILURE, e.getMessage(), null);
        } finally {
            store.close();
        }
        return response;
    }
    /**
     * 根据id 获取证书信息 {@link cn.unitid.spark.cm.sdk.data.entity.Certificate}
     *
     * @param certificateId  证书ID;
     * @return  {@link cn.unitid.spark.cm.sdk.data.response.ObjectResponse}
     */
    public ObjectResponse getCertificate(String certificateId) {
        ObjectResponse response = null;
        try {
            store.open();
            ICertificate x509Certificate = store.getCertificate(certificateId);
            Certificate certificate = new Certificate(x509Certificate);
            response = new ObjectResponse(BaseService.MSG_SUCCESS, "", certificate);
        } catch (Exception e) {
            response = new ObjectResponse(BaseService.MSG_FAILURE, e.getMessage(), null);
        } finally {
            store.close();
        }
        return response;
    }

    /**
     * 根据Id 删除库里证书
     * @param certificateId 证书ID
     * @return {@link cn.unitid.spark.cm.sdk.data.response.ObjectResponse}
     */
    public ObjectResponse deleteCertificate(String certificateId) {
        ObjectResponse objectResponse = getCertificate(certificateId);
        if (objectResponse.getRet() == BaseService.MSG_SUCCESS) {
            Certificate certificate = (Certificate) objectResponse.getObject();
            Response response;
            try {
                store.open();
                store.deleteCertificate(certificate.getX509Certificate());
                objectResponse.setRet(0);
            } catch (Exception e) {
                objectResponse.setRet(BaseService.MSG_FAILURE);
                objectResponse.setMessage(e.getMessage());

            } finally {
                store.close();
            }
            return objectResponse;
        } else {
            return objectResponse;
        }
    }

    /**
     * 导入证书
     * @param issueResponse 签发返回对象 {@link cn.com.syan.spark.client.sdk.data.response.MyIssueResponse}
     * @param pin  pin码
     * @return {@link cn.unitid.spark.cm.sdk.data.response.ObjectResponse}
     */
     public ObjectResponse importCertificate(OnlineIssueResponse issueResponse, String pin) {
         ObjectResponse response = null;
        try {
            store.open();
            java.security.cert.X509Certificate x509Certificate = getX509Certificate(issueResponse.getSignCert());
            String id = PublicKeyAliasUtil.getAlias(x509Certificate.getPublicKey());

            if (x509Certificate != null) {
                store.importCertificate(x509Certificate);
                store.save();
                store.open();
                ICertificate xx509Cert = store.getCertificate(x509Certificate);
                if (issueResponse.getSignCert() != null && issueResponse.getEncKey() != null && pin != null) {
                    java.security.cert.X509Certificate encX509Certificate = CertificateConverter.fromBase64(issueResponse.getEncCert());
                    byte[] encBlobKey = Base64.decodeBase64(issueResponse.getEncKey());
                    store.importCertificateAndEnvelopedPrivateKey(encX509Certificate, encBlobKey, xx509Cert.getPrivateKey(), pin, new Date());
                }
                response = new ObjectResponse(BaseService.MSG_SUCCESS,"",new Certificate(xx509Cert));
            }
            if (response == null) {
                response =new ObjectResponse(BaseService.MSG_SUCCESS, "didn't import",null);
            }
        } catch (Exception e) {
            Log.e("importCertificate", "importCertificate " + e.getMessage(), e);
            response =new ObjectResponse(BaseService.MSG_SUCCESS,  e.getMessage(),null);

        } finally {
            store.save();
            store.close();
        }
        return response;
    }

    /**
     * 导入Base64编码证书
     * @param certificateStr Base64编码证书字符串
     * @return {@link cn.unitid.spark.cm.sdk.data.response.ObjectResponse}
     */
    public ObjectResponse importCertificate(String certificateStr) {
        ObjectResponse response = null;
        try {
            store.open();
            java.security.cert.X509Certificate x509Certificate = getX509Certificate(certificateStr);
            if (x509Certificate != null) {
                store.importCertificate(x509Certificate);

                response = new ObjectResponse(BaseService.MSG_SUCCESS,"",null);
            }
            if (response == null) {
                response =new ObjectResponse(BaseService.MSG_SUCCESS, "didn't import",null);
            }
        } catch (Exception e) {
            response =new ObjectResponse(BaseService.MSG_SUCCESS,  e.getMessage(),null);
        } finally {
            store.save();
            store.close();
        }
        return response;
    }

    /**
     * 查询本地库中所有证书
     * @return   ArrayList<Certificate> 证书集合
     */
  public ArrayList<Certificate> getAllCertificateList(){
      ArrayList<Certificate> result = new ArrayList<Certificate>();

      ObjectResponse objectResponse= getCertificateList(null,null,null,null);

      List<Certificate> list=(ArrayList<Certificate>)objectResponse.getObject();
      Map<String,Certificate> encMap=new HashMap<String,Certificate>();

      for (Certificate certificate : list) {
          if(!certificate.getX509Certificate().hasKeyUsage(new int[]{128}) && certificate.getX509Certificate().hasKeyUsage(new int[]{16})) {
              encMap.put(certificate.getSubject(),certificate);
          }else{
              result.add(certificate);
          }
      }
      for (Certificate certificate : result) {
          Certificate c = encMap.get(certificate.getSubject());
          if (c != null) {
              certificate.setDouble(true);
              certificate.setEncCertId(c.getId());
          }
      }
      return result;
  }


    /**
     *  根据条件设定查询库中符合条件证书列表
     * @param isCa  是否CA证书
     * @param isPrivateKeyAccessible 是否有私钥
     * @param cas    CA根证书列表
     * @param isSign 是签名证书  否  加密证书
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.ObjectResponse}
     */

    public ObjectResponse getCertificateList(Boolean isCa, Boolean isPrivateKeyAccessible, List<String> cas, Boolean isSign) {
        int ret = BaseService.MSG_SUCCESS;
        String message = "";
        List<Certificate> result = new ArrayList<Certificate>();

        try {
            store.open();

            List<ICertificate> list = store.getCertificate();

            if (isCa != null) {
                list = X509CertificateArrays.filter(list, isCa ? CertificateFilterCaConstrain.CA.value() : CertificateFilterCaConstrain.NotCA.value());
            }
            if (isPrivateKeyAccessible != null) {
                list = X509CertificateArrays.filter(list, isPrivateKeyAccessible);

                //蓝牙
                Map<String, Object> cond = new HashMap<String, Object>();
                cond.put("keyType", "1");
                List<ICertificate> blueList = X509CertificateArrays.filter(store.getCertificate(), cond);
                list.addAll(blueList);
            }
            if (cas != null && cas.size() > 0) {
                List<String> principals = new ArrayList<String>();
                for (String ca : cas) {
                    java.security.cert.X509Certificate x509ca = getX509Certificate(ca);
                    if (x509ca != null) {
                        principals.add(CertificateStandardizedUtil.getSubject(x509ca));
                    }
                }
                if (principals.size() > 0) {
                    list = X509CertificateArrays.filter(list, principals.toArray(new String[0]));
                }
            }

            if (isSign != null) {
                if (isSign) {
                    list = X509CertificateArrays.filter(list, new int[]{128});
                } else {
                    list = X509CertificateArrays.filter(list, new int[]{16});
                }
            }

            for (ICertificate x509 : list) {
                result.add(new Certificate(x509));
            }


        } catch (Exception e) {

            ret = BaseService.MSG_FAILURE;
            message = "certificate store failed: " + e.getMessage();
        } finally {
            store.close();
        }
        Collections.sort(result, comparator);
        ObjectResponse response = new ObjectResponse(ret, message, result);
        return response;
    }

    /**
     * 公钥证书加密
     * @param certificate 选中证书 {@link cn.unitid.spark.cm.sdk.data.entity.Certificate}
     * @param plainText  加密原文
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */

    public DataProcessResponse publicEncrypt(Certificate certificate, String plainText) {
        return publicEncrypt(certificate.getId(), plainText);
    }
    /**
     * 公钥证书加密
     * @param certificateId 选中证书ID
     * @param plainText  加密原文
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */
    public DataProcessResponse publicEncrypt(String certificateId, String plainText) {
        ObjectResponse objectResponse = getCertificate(certificateId);
        if (objectResponse.getRet() == BaseService.MSG_SUCCESS) {
            Certificate certificate = (Certificate) objectResponse.getObject();
            DataProcessResponse response;
            try {
                if (certificate.getX509Certificate().getAlgorithm().equals("RSA")) {
                    int limit = certificate.getX509Certificate().getKeyLength() / 8 - 11;
                    int curLen = plainText.getBytes().length;
                    if (curLen > limit) {
                        response = new DataProcessResponse(BaseService.MSG_FAILURE, "data length is too long", "");
                    } else {
                        String result = Base64.encodeBase64String(certificate.getX509Certificate().publicEncrypt(plainText));
                        response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
                    }
                } else {
                    String result = Base64.encodeBase64String(certificate.getX509Certificate().publicEncrypt(plainText));
                    response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
                }
            } catch (Exception e) {

                response = new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(), "");
            }
            return response;
        } else {
            return new DataProcessResponse(objectResponse.getRet(), objectResponse.getMessage(), "");
        }
    }

    /**
     * 私钥解密
     * @param certificate 选择的证书 {@link cn.unitid.spark.cm.sdk.data.entity.Certificate}
     * @param plainText 密文
     * @param pin  pin码
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */
    public DataProcessResponse privateDecrypt(Certificate certificate, String plainText, String pin) {
        DataProcessResponse response = privateDecrypt(certificate.getId(), plainText, pin);
        return response;

    }
    /**
     * 私钥解密
     * @param certificateId 选择的证书Id
     * @param plainText 密文
     * @param pin  pin码
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */
    public DataProcessResponse privateDecrypt(String certificateId, String plainText, String pin) {
        ObjectResponse objectResponse = getCertificate(certificateId);
        if (objectResponse.getRet() == BaseService.MSG_SUCCESS) {
            Certificate certificate = (Certificate) objectResponse.getObject();
            DataProcessResponse response;
            try {
                String result = new String(certificate.getX509Certificate().privateDecrypt(plainText, pin));
                response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
            } catch (Exception e) {
                Log.e("decrypt", "decrypt", e);
                response = new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(), "");
            }
            return response;
        } else {
            return new DataProcessResponse(objectResponse.getRet(), objectResponse.getMessage(), "");
        }
    }

    /**
     *P7签名
     * @param certificate 选择的证书 {@link cn.unitid.spark.cm.sdk.data.entity.Certificate}
     * @param plainText 待签名原文
     * @param pin  PIN码
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */
    public DataProcessResponse p7Sign(Certificate certificate, String plainText, String pin) {
        DataProcessResponse response;
        try {
            String result = Base64.encodeBase64String(certificate.getX509Certificate().pkcs7(plainText, true, pin));
            response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
        } catch (Exception e) {

            response = new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(), "");
        }
        return response;
    }
    /**
     * 对摘要p1签名
     * @param certificate
     * @param data
     * @param algorithm 摘要算法
     * @param pin
     * @return
     */

    public DataProcessResponse p1SignDigest(Certificate certificate, byte[] data,String algorithm, String pin) {
        DataProcessResponse response;
        try {
            String result = Base64.encodeBase64String(certificate.getX509Certificate().pkcs1Digest(data, algorithm, pin));
            response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
        } catch (Exception e) {
            response = new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(), "");
        }
        return response;
    }
    /**
     * p1签名
     * @param certificate
     * @param data
     * @param pin
     * @return
     */

    public DataProcessResponse p1Sign(Certificate certificate, byte[] data, String pin) {
        DataProcessResponse response;
        try {
            String result = Base64.encodeBase64String(certificate.getX509Certificate().pkcs1(data, pin));
            response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
        } catch (Exception e) {
            response = new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(), "");
        }
        return response;
    }
    /**
     *P1签名
     * @param certificate  选择的证书 {@link cn.unitid.spark.cm.sdk.data.entity.Certificate}
     * @param plainText 待签名原文
     * @param pin pin码
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */
    public DataProcessResponse p1Sign(Certificate certificate, String plainText, String pin) {
        DataProcessResponse response;
        try {
            String result = Base64.encodeBase64String(certificate.getX509Certificate().pkcs1(plainText, pin));
            response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
        } catch (Exception e) {

            response = new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(), "");
        }
        return response;
    }

    /**
     *P1签名
     * @param certificateId  选择的证书Id
     * @param plainText 待签名原文
     * @param pin
     * @return  {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */
    public DataProcessResponse p1Sign(String certificateId, String plainText, String pin) {
        ObjectResponse objectResponse = getCertificate(certificateId);
        if (objectResponse.getRet() == BaseService.MSG_SUCCESS) {
            Certificate certificate = (Certificate) objectResponse.getObject();
            DataProcessResponse response;
            try {
                String result = Base64.encodeBase64String(certificate.getX509Certificate().pkcs1(plainText, pin));
                response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
            } catch (Exception e) {

                response = new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(), "");
            }
            return response;
        } else {
            return new DataProcessResponse(objectResponse.getRet(), objectResponse.getMessage(), "");
        }
    }

    /**
     * 生成P10请求
     * @param subject   证书主题项
     * @param pin  PIN码
     * @param alias　　别名
     * @param algorithm　算法
     * @return ObjectResponse  {@link cn.unitid.spark.cm.sdk.data.response.ObjectResponse}
     */
    public ObjectResponse createPKCS10(String subject, List<Extension> extensions, String pin, String alias, String algorithm) {
        ObjectResponse response = null;
        try {
            store.open();
            PKCS10CertificationRequest request =  algorithm.equalsIgnoreCase("SM2")?store.createSM2PKCS10(subject,extensions, pin, alias):store.createRSAPKCS10(subject,extensions, pin, alias,2048);
            response = new ObjectResponse(BaseService.MSG_SUCCESS, "", request);
        } catch (Exception e) {

            response = new ObjectResponse(BaseService.MSG_FAILURE, e.getMessage(), null);
        } finally {
            store.save();
            store.close();
        }
        return response;
    }



    private java.security.cert.X509Certificate getX509Certificate(String certificateStr) {
        java.security.cert.X509Certificate x509Certificate = null;
        try {
            if (!certificateStr.startsWith("-----BEGIN CERTIFICATE-----")) {
                x509Certificate = CertificateConverter.fromBase64(certificateStr);
            } else {
                x509Certificate = CertificateConverter.fromPEMString(certificateStr);
            }
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
        return x509Certificate;
    }

    private class MyComparator implements Comparator<Certificate> {
        @Override
        public int compare(Certificate lhs, Certificate rhs) {
            int flag;
            if (lhs.isPrivateKeyAccessible() ^ rhs.isPrivateKeyAccessible()) {
                if (lhs.isPrivateKeyAccessible()) {
                    flag = -1;
                } else {
                    flag = 1;
                }
            } else {
                flag = 0;
            }
            if (flag == 0) {
                if (lhs.getNotAfter().after(rhs.getNotAfter())) {
                    return 1;
                } else {
                    return -1;
                }
            } else {
                return flag;
            }
        }
    }

    /**
     * p1验签
     * @param sign  BASE64编码签名值
     * @param data  签名原文
     * @param certValue  BASE64编码证书字符串
     * @return boolean 验签结果
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public boolean p1Verify(String sign, String data, String certValue) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        try {
            java.security.cert.X509Certificate x509 = CertificateConverter.fromBase64(certValue);
            String algoOID = x509.getSigAlgOID();
            String algorithm = cn.com.syan.jcee.common.sdk.identifier.SparkAlgorithmIdentifier.SHA1_WITH_RSA;
            if (x509.getSigAlgOID().equals("1.2.156.10197.1.501")) {
                algorithm = cn.com.syan.jcee.common.sdk.identifier.SparkAlgorithmIdentifier.getAlgorithmName(x509.getSigAlgOID());
            }
            SparkSignature sparkSignature = SparkSignature.getInstance(algorithm);
            sparkSignature.initVerify(x509.getPublicKey());
            sparkSignature.update(data.getBytes());
            boolean result = sparkSignature.verify(Base64.decodeBase64(sign.getBytes()));
            return result;
        } catch (Exception e) {
            Log.e(TAG, "", e);
            return false;
        }
    }

    /**
     * P7验签
     * @param sign 签名值
     * @return boolean 验签结果
     * @throws SignatureException
     */
    public boolean p7Verify(String sign) throws SignatureException {
        PKCS7Signature p7Signature = new PKCS7Signature();
        boolean result = p7Signature.verify(sign);

        return result;
    }

    /**
     * 数字信封 封信
     * @param certificate 选中的证书
     * @param data 原文
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */
    public DataProcessResponse  sealEnvelope(Certificate certificate,String data){
        DataProcessResponse response = null;
        try {
            String result = Base64.encodeBase64String(certificate.getX509Certificate().envelopeSeal(data.getBytes()));
            response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
        } catch (Exception e) {
            response =  new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(),"");
        }
        return response;
    }

    /**
     * 数字信封拆封
     * @param certificate 选中的证书
     * @param data 密文
     * @param pin  PIN码
     * @return   {@link cn.unitid.spark.cm.sdk.data.response.DataProcessResponse}
     */
    public DataProcessResponse  openEnvelope(Certificate certificate,String data,String pin){
        DataProcessResponse response = null;
        try {
            byte[] envelope =certificate.getX509Certificate().envelopeOpen(Base64.decodeBase64(data), pin);
            String result=new String(envelope,"UTF-8");
            response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
        } catch (Exception e) {
            e.printStackTrace();
            response =  new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(),"");
        }
        return response;
    }
    public DataProcessResponse privateKeyEncrypt(String certificateId,String pin, String plainText,String transformation,boolean isBase64) {
        ObjectResponse objectResponse = getCertificate(certificateId);
        if (objectResponse.getRet() == BaseService.MSG_SUCCESS) {
            Certificate certificate = (Certificate) objectResponse.getObject();
            DataProcessResponse response;
            try {
                byte[] data=null;
                if(isBase64){
                    data=Base64.decodeBase64(plainText);
                }else{
                    data=plainText.getBytes();
                }
                String result = Base64.encodeBase64String(certificate.getX509Certificate().privateEncrypt(data,pin,transformation));
                response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", result);
            } catch (Exception e) {
                Log.e(TAG, "", e);
                response = new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(), "");
            }
            return response;
        } else {
            return new DataProcessResponse(objectResponse.getRet(), objectResponse.getMessage(), "");
        }
    }
    public DataProcessResponse importPfx(String pkcs12, String pkcs12PIN, String newPIN, boolean withChain){

        DataProcessResponse response = null;
        try {
            store.open();
            store.importPrivateKey(pkcs12,pkcs12PIN,newPIN,withChain);


            store.open();
            response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", "");
        } catch (Exception e) {
            e.printStackTrace();
            response =  new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(),"");
        }finally {
            store.save();
            store.close();
        }
        return response;

    }

    public DataProcessResponse importPfx(byte[] p12, String pkcs12PIN, String newPIN, boolean withChain){

        DataProcessResponse response = null;
        try {
            store.open();
//            store.importPrivateKey(pkcs12,pkcs12PIN,newPIN,withChain);
//            byte[] p12= Base64.decodeBase64(pkcs12);
            savepfx(store,new PKCS12Service(p12,pkcs12PIN.toCharArray()),newPIN);
            store.open();
            response = new DataProcessResponse(BaseService.MSG_SUCCESS, "", "");
        } catch (Exception e) {
            e.printStackTrace();
            response =  new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage(),"");
        }finally {
            store.save();
            store.close();
        }
        return response;

    }

    private void savepfx(CertificateStore store ,PKCS12Service pkcs12Service,String newPIN) throws JCEECMException {
        try {
            PKCS5PBES2 pkcs5PBES2 = new PKCS5PBES2();
            String ppKey = StringConverter.toHexadecimal(pkcs5PBES2.encryptKey(pkcs12Service.getPrivateKey(), newPIN.toCharArray()));
            store.importCertificateAndPrivateKey(pkcs12Service.getX509Certificate(), ppKey, new Date());
        } catch (Exception var12) {
            throw new JCEECMException("fail to import pkcs12. Error Message: " + var12.getMessage());
        }
    }


}