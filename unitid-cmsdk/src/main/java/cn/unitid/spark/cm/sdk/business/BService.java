package cn.unitid.spark.cm.sdk.business;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import cn.unitid.spark.cm.sdk.ui.CBSDialogFragment;

/**
 * Created by lyb on 2016/6/30.
 */
public abstract class BService {
    public static final Integer MSG_SUCCESS=0;   //获取信息成功
    public static final Integer MSG_FAILURE=-1;   //获取信息失败

    protected CBSCertificateStore store =null;
    protected FragmentManager manager;
    protected CBSDialogFragment curDialog = null;
    protected String _pin;
    protected String data = "";

    protected boolean base64 = false;

    protected Certificate selectedCertificate ;
    protected Activity mainProcessActivity;
    protected ProcessListener<DataProcessResponse> processListener;
    public BService(FragmentActivity activity, ProcessListener<DataProcessResponse> processListener ){
        this.mainProcessActivity=activity;
        manager=activity.getSupportFragmentManager();
        this.processListener=processListener;
        data=mainProcessActivity.getIntent().getStringExtra("data");
        base64=mainProcessActivity.getIntent().getBooleanExtra("isBase64",false);
        if (data == null || data.equals("")) {
            processListener.doException(new CmSdkException("data 不能为空"));
        }

        SparkApplication.init(mainProcessActivity.getApplication());
        this.store =CBSCertificateStore.getInstance();;
    }
   protected  abstract String getDataProcessType();
}
