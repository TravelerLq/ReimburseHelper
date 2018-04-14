package cn.unitid.spark.cm.sdk.business;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.com.syan.spark.client.sdk.data.response.MyIssueResponse;
import cn.com.syan.spark.client.sdk.data.response.Response;
import cn.com.syan.spark.client.sdk.exception.SparkClientException;
import cn.com.syan.spark.client.sdk.service.BaseService;
import cn.com.syan.spark.client.sdk.service.MyRenewService;
import cn.com.syan.spark.client.sdk.service.OnDataListener;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 * 延期证书服务
 * Created by lyb on 2016/7/1.
 */
public class CertificateRenewService {
    private static final String TAG = CertificateRenewService.class.getName();
    private FragmentManager manager;
    private Activity mainProcessActivity;
    private ProcessListener<OnlineIssueResponse> processListener;
    private CBSCertificateStore store =null;
    private OnlineClient onlineClient;
    private Handler handler;
    private String baseUrl="";
    private String cert;

    /**
     *构造延期证书实现服务
     * @param activity FragmentActivity
     * @param processListener 延期签发回调方法实现
     */
    public CertificateRenewService(FragmentActivity activity, OnlineClient onlineClient, final ProcessListener<OnlineIssueResponse> processListener){
        this.mainProcessActivity=activity;
        this.processListener=processListener;
        this.onlineClient=onlineClient;
        manager = activity.getSupportFragmentManager();
        SparkApplication.init(mainProcessActivity.getApplication());
        this.store =CBSCertificateStore.getInstance();;
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
                            Response response1 = store.importCertificate(response.getSignCert());
                            if (response1.getRet() == BaseService.MSG_SUCCESS) {
                                response1= store.importCertificate(response.getEncCert());
                                if(response1.getRet()== BaseService.MSG_SUCCESS) {
                                    processListener.doFinish(response, null);
                                }else{
                                    Log.e(TAG,response1.getMessage());
                                    processListener.doException(new CmSdkException(response1.getMessage()));
                                }
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

    public void renew(final String cert){//
        (new Thread() {
            public void run() {
                try {
                    OnlineIssueResponse onlineIssueResponse = onlineClient.renew(cert);
                    handler.obtainMessage(0,onlineIssueResponse).sendToTarget();
                } catch (Exception var2) {
                    handler.obtainMessage(-1,new SparkClientException(var2.getMessage(), var2)).sendToTarget();
                }
            }
        }).start();
    }


}