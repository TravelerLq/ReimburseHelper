package cn.unitid.spark.cm.sdk.business;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

import cn.com.syan.online.sdk.OnlineApplyResponse;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.RegProperty;
import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.com.syan.spark.client.sdk.exception.SparkClientException;
import cn.unitid.spark.cm.sdk.common.IdCardValidator;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 * 注册证书
 * Created by lyb on 2016/6/30.
 */
public class CertificateRegisterService  {
    private static final String TAG = CertificateRegisterService.class.getName();
    private Activity mainProcessActivity;
    private CBSCertificateStore store =null;
    private Handler handler;
    private OnlineClient onlineClient;
    public static  String caRegTemplate[][] = {

            {"commonName",  "text", "名称",   "CN"},
            {"country",  "text", "国家",   "C"},
            {"province",   "text", "省",    "ST"},
            {"locality",    "text", "市",    "L"},
            {"orgName","text",  "组织", "O"},
            {"orgUnitName", "text", "组织机构",  "OU"},
            {"email",   "text", "电子邮件",  "E"},
            {"address", "text", "地址",   "STREET"},
            {"postalCode",  "text", "邮编",   "PostalCode"},
            {"telephone",   "text", "电话",   "TelephoneNumber"},
            {"fax", "text", "传真",   "FAX"},
            {"paperType",   "text", "证件类型", "PT"},
            {"paperNo", "text", "证件号",  "PNO"}
    };
    /**
     * 构造证书注册服务对象
     * @param processListener    回调方法实现
     */
    public CertificateRegisterService(FragmentActivity activity, OnlineClient onlineClient, final ProcessListener<OnlineApplyResponse> processListener){
        this.onlineClient=onlineClient;
        this.mainProcessActivity=activity;
        SparkApplication.init(mainProcessActivity.getApplication());
        this.store =CBSCertificateStore.getInstance();
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case -1:
                        SparkClientException e = (SparkClientException)msg.obj;
                        processListener.doException(new CmSdkException(e.getMessage()));
                        break;
                    case 0:
                        OnlineApplyResponse response=(OnlineApplyResponse)msg.obj;
                        processListener.doFinish(response, null);
                }
            }
        };

    }
//    public void register(final String templateId,final List<RegProperty> propertyList){
//        (new Thread() {
//            public void run() {
//                try {
//                    OnlineApplyResponse onlineApplyResponse=onlineClient.apply(templateId,propertyList);
//                    handler.obtainMessage(0,onlineApplyResponse).sendToTarget();
//                } catch (Exception var2) {
//                    handler.obtainMessage(-1,new SparkClientException(var2.getMessage(), var2)).sendToTarget();
//                }
//            }
//        }).start();
//    }

    /**
     * 实名注册申请证书
     * @param realname 姓名  必填
     * @param idno 身份证号 必填
     * @param telephone 手机号码 必填
     * @param vocde 验证码 必填
     * @param email  可选
     */

    public void register(final String realname,final String idno,final String telephone,final String vocde,final String email){

        if(store.getAllCertificateList().size()>0){
            throw new RuntimeException("cert exists:"+store.getAllCertificateList().size());
        }
        if(realname==null || "".equals(realname)){
            throw new RuntimeException("realname is required ");
        }
        if(idno==null || "".equals(idno)){
            throw new RuntimeException("idno is required ");
        }
        IdCardValidator idCardValidator=new IdCardValidator();
        if(!idCardValidator.isValidate18Idcard(idno)){
            throw new RuntimeException("idno is invalid ");
        }
        if(telephone==null || "".equals(telephone)){
            throw new RuntimeException("telephone is required ");
        }
        if(telephone.length()!=11){
            throw new RuntimeException("telephone is invalid ");
        }
        if(vocde==null || "".equals(vocde)){
            throw new RuntimeException("vocde is required ");
        }
        (new Thread() {
            public void run() {
                try {
                    List<RegProperty> regProperties=new ArrayList<RegProperty>();        //C
                    RegProperty regProperty=null;
                    regProperty=new RegProperty();
                    regProperty.setName(caRegTemplate[1][0]);
                    regProperty.setType(caRegTemplate[1][1]);
                    regProperty.setLabel(caRegTemplate[1][2]);
                    regProperty.setShortName(caRegTemplate[1][3]);
                    regProperty.setValue("CN");
                    regProperty.setFlag(1);
                    regProperties.add(regProperty);
                    //CN
                    regProperty=new RegProperty();
                    regProperty.setName(caRegTemplate[0][0]);
                    regProperty.setType(caRegTemplate[0][1]);
                    regProperty.setLabel(caRegTemplate[0][2]);
                    regProperty.setShortName(caRegTemplate[0][3]);
                    regProperty.setValue(realname);
                    regProperty.setFlag(1);
                    regProperties.add(regProperty);

                    //paperType
                    regProperty=new RegProperty();
                    regProperty.setName(caRegTemplate[11][0]);
                    regProperty.setType(caRegTemplate[11][1]);
                    regProperty.setLabel(caRegTemplate[11][2]);
                    regProperty.setShortName(caRegTemplate[11][3]);
                    regProperty.setValue("身份证");
                    regProperty.setFlag(1);
                    regProperties.add(regProperty);

                    //paperNo
                    regProperty=new RegProperty();
                    regProperty.setName(caRegTemplate[12][0]);
                    regProperty.setType(caRegTemplate[12][1]);
                    regProperty.setLabel(caRegTemplate[12][2]);
                    regProperty.setShortName(caRegTemplate[12][3]);
                    regProperty.setValue(idno);
                    regProperty.setFlag(1);
                    regProperties.add(regProperty);
                    //telephone
                    regProperty=new RegProperty();
                    regProperty.setName(caRegTemplate[9][0]);
                    regProperty.setType(caRegTemplate[9][1]);
                    regProperty.setLabel(caRegTemplate[9][2]);
                    regProperty.setShortName(caRegTemplate[9][3]);
                    regProperty.setValue(telephone);
                    regProperty.setFlag(1);
                    regProperties.add(regProperty);
                                      //email
                    if(email!=null && !"".equals(email)){
                        regProperty = new RegProperty();
                        regProperty.setName(caRegTemplate[6][0]);
                        regProperty.setType(caRegTemplate[6][1]);
                        regProperty.setLabel(caRegTemplate[6][2]);
                        regProperty.setShortName(caRegTemplate[6][3]);
                        regProperty.setValue(email);
                        regProperty.setFlag(1);
                        regProperties.add(regProperty);
                    }
                    OnlineApplyResponse onlineApplyResponse=onlineClient.apply(null,vocde,regProperties);
                    handler.obtainMessage(0,onlineApplyResponse).sendToTarget();
                } catch (Exception var2) {
                    handler.obtainMessage(-1,new SparkClientException(var2.getMessage(), var2)).sendToTarget();
                }
            }
        }).start();
    }

}
