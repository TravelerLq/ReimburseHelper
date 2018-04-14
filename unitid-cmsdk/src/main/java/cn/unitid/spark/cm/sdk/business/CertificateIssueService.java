package cn.unitid.spark.cm.sdk.business;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.spongycastle.asn1.ASN1ObjectIdentifier;
import org.spongycastle.asn1.DEROctetString;
import org.spongycastle.asn1.x509.Extension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.com.syan.jcee.cm.impl.PKCS10CertificationRequest;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.com.syan.spark.client.sdk.data.response.Response;
import cn.com.syan.spark.client.sdk.exception.SparkClientException;
import cn.com.syan.spark.client.sdk.service.BaseService;
import cn.unitid.spark.cm.sdk.common.Utils;
import cn.unitid.spark.cm.sdk.data.response.ObjectResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.IssueOnConfirmListener;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import cn.unitid.spark.cm.sdk.ui.CertificateIssueDialogFragment;
import cn.unitid.spark.cm.sdk.ui.IssueDialogFragment;

/**
 * 签发证书
 * Created by lyb on 2016/6/30.
 */
public class CertificateIssueService{
    private static final String TAG = CertificateIssueService.class.getName();
    private FragmentManager manager;
    private Activity mainProcessActivity;

    private CBSCertificateStore store =null;
    private OnlineClient onlineClient;
    private Handler handler;
    private ProcessListener<OnlineIssueResponse> processListener;
    private IssueDialogFragment issueDialogFragment;

    private String certPin;
    /**
     * 构造证书签发服务对象
     * @param activity FragmentActivity
     * @param processListener    回调方法实现
     */
    public CertificateIssueService(FragmentActivity activity, OnlineClient onlineClient,final ProcessListener<OnlineIssueResponse> processListener){
        this.onlineClient=onlineClient;
        this.mainProcessActivity=activity;
        this.processListener=processListener;
        manager = activity.getSupportFragmentManager();
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
                        OnlineIssueResponse response=(OnlineIssueResponse)msg.obj;
                        if (response.getRet() == BaseService.MSG_SUCCESS) {
                            Response response1 = store.importCertificate(response,certPin);
                            if (response1.getRet() == BaseService.MSG_SUCCESS) {
                                processListener.doFinish(response, null);
                            } else {
                                Log.e(TAG,response1.getMessage());
                                processListener.doException(new CmSdkException(response1.getMessage()));
                            }

                        } else {
                            Log.e(TAG,response.getMsg());
                            processListener.doException(new CmSdkException(response.getMsg()));
                        }
                }
            }
        };
    }

    protected class IssueEvent implements IssueOnConfirmListener {

       private String issueId ;
       private String subject;
       private Map<String,String> extensionMap;
       private String algorithm;
       private boolean issue ;
       protected IssueEvent(String issueId,String subject,Map<String,String> extensionMap,String algorithm,boolean issue){
           this.issueId=issueId;
           this.subject=subject;
           this.extensionMap=extensionMap;
           this.algorithm=algorithm;
           this.issue=issue;
        }
       @Override
       public void onConfirm(android.support.v4.app.DialogFragment dialogFragment, String alias, String pin) {
           try {
               certPin =pin;
               List<Extension> extensions = new ArrayList<Extension>();
               if(extensionMap!=null && extensionMap.size()>0 ){
                   Iterator iterator= extensionMap.keySet().iterator();
                   while(iterator.hasNext()){
                       String key=iterator.next().toString();
                       String value=extensionMap.get(key);
                       Extension ext = new Extension(new ASN1ObjectIdentifier(key), false, new DEROctetString(value.getBytes()));
                       extensions.add(ext);
                   }
               }

               PKCS10CertificationRequest request=null;
               if(issue){
                   ObjectResponse response = store.createPKCS10(subject, extensions,pin, alias,algorithm);
                   if (response.getRet() != BaseService.MSG_SUCCESS ) {
                       Log.e(TAG, response.getMessage());
                       processListener.doException(new CmSdkException(response.getRet()+":"+response.getMessage()));
                       return;
                   }
                   request=(PKCS10CertificationRequest) response.getObject();
                   if(request==null){
                       processListener.doException(new CmSdkException("PKCS10CertificationRequest is null"));
                       return;
                   }
               }
               final   String  pkcs10=request.toBase64String();

               System.out.println("=====pkcs10=||"+pkcs10);
               (new Thread() {
                   public void run() {
                       try {
                           OnlineIssueResponse onlineIssueResponse= onlineClient.issue(issueId,pkcs10);
                           handler.obtainMessage(0,onlineIssueResponse).sendToTarget();
                       } catch (Exception var2) {
                           handler.obtainMessage(-1,new SparkClientException(var2.getMessage(), var2)).sendToTarget();
                       }
                   }
               }).start();
           } catch (Exception e) {
               processListener.doException(new CmSdkException(e.getMessage()));
               Log.e(TAG, "", e);

           }
           dialogFragment.dismiss();
       }
   }

    /**
     *
     * @param issueId  注册申请ID
     * @param subject  证书主题
     * @param extensionMap  扩展属性 可以为null
     * @param algorithm 算法标识 “SM2”or ""RSA"
     * @param issue  true: 生成pkcs10重新签发 false : 下载已经签发的证书
     */
    public void issue(final String issueId, final String subject, final Map<String,String> extensionMap, final String algorithm,final boolean issue){
        Map<String, String> subMap= Utils.parseSubject(subject);
        String alias="";
        if (subMap.containsKey("CN")) {
            alias= subMap.get("CN");
        } else {
            alias= "bieming";
        }
        if(issueDialogFragment==null){
            issueDialogFragment= new CertificateIssueDialogFragment();
        }
        issueDialogFragment.setOnConfirmListener(new IssueEvent(issueId,subject,extensionMap,algorithm,issue));
        issueDialogFragment.setAlias(alias);

        issueDialogFragment.show(manager, "confirm");



    }
}
