package cn.unitid.spark.cm.sdk.business;


import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import cn.com.syan.jcee.cm.impl.CertificateStore;
import cn.com.syan.jcee.cm.impl.ICertificate;
import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.com.syan.spark.client.sdk.service.BaseService;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import cn.unitid.spark.cm.sdk.ui.CBSDialogFragment;
import cn.unitid.spark.cm.sdk.ui.CertificateSetAliasDialogFragment;


/**
 *更新别名服务
 */
public class SetAliasService {
    private FragmentManager manager;
    private Activity mainProcessActivity;
    private ProcessListener<DataProcessResponse> processListener;
    private CertificateStore store =null;
    private CBSDialogFragment curDialog = null;
    /**
     * 构造更新PIN服务实现
     * @param activity
     * @param processListener
     */
    public SetAliasService(FragmentActivity activity, ProcessListener<DataProcessResponse> processListener){
        this.mainProcessActivity=activity;
        this.processListener=processListener;
        manager = activity.getSupportFragmentManager();
        SparkApplication.init(mainProcessActivity.getApplication());
        File path = SparkApplication.getExampleApplicationContext().getFilesDir();
        String certpath = path.getAbsolutePath() + "/certificate.store";

        store = CertificateStore.getInstance(certpath);

    }

    public void update(final Certificate cert) {
        if (curDialog != null) {
            curDialog.dismiss();
        }
        curDialog = new CertificateSetAliasDialogFragment()
                .setOnConfirmListener(new CertificateSetAliasDialogFragment.OnConfirmListener() {
                    @Override
                    public void onConfirm(DialogFragment dialogFragment, String alias) {
                        if (alias == null || alias.equals("")) {
                            Toast.makeText(mainProcessActivity, "请输入别名", Toast.LENGTH_SHORT).show();
                        } else {
                            dialogFragment.dismiss();
                            DataProcessResponse      response=null;
                            try {
                                store.open();
                                ICertificate x509Certificate = store.getCertificate(cert.getId());
//                                x509Certificate.setAlias(alias);
                                store.importCertificate(x509Certificate);
                                x509Certificate=  store.getCertificate(cert.getEncCertId());
//                                x509Certificate.setAlias(alias);
                                store.importCertificate(x509Certificate);
                                response=  new DataProcessResponse(BaseService.MSG_SUCCESS, "", "");
                                store.save();
                                store.close();
                                processListener.doFinish(response,null);
                            } catch (Exception e) {
                                    Log.e("setAlias",e.getMessage(),e);
                                response=  new DataProcessResponse(BaseService.MSG_FAILURE, e.getMessage()+"", "");
                            }
                            processListener.doFinish(response,null);
                        }
                    }
                });
        curDialog.show(manager, "confirm");
    }
}
