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
import cn.com.syan.jcee.cm.impl.IPrivateKey;
import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.com.syan.spark.client.sdk.service.BaseService;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import cn.unitid.spark.cm.sdk.ui.CBSDialogFragment;
import cn.unitid.spark.cm.sdk.ui.CertificateSetPINDialogFragment;


/**
 *更新PIN服务
 */
public class UpdatePinService  {
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
    public UpdatePinService(FragmentActivity activity, ProcessListener<DataProcessResponse> processListener){
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
        curDialog = new CertificateSetPINDialogFragment()
                .setOnConfirmListener(new CertificateSetPINDialogFragment.onConfirmListener() {
                    @Override
                    public void onConfirm(DialogFragment dialogFragment, String oldPin, String newPin, String newPin2) {
                        if (oldPin == null || oldPin.equals("")) {
                            Toast.makeText(mainProcessActivity, "请输入原密码", Toast.LENGTH_SHORT).show();
                        } else if (newPin == null || newPin.equals("") || newPin2 == null || newPin2.equals("")) {
                            Toast.makeText(mainProcessActivity, "请输入新密码", Toast.LENGTH_SHORT).show();
                        } else if (!newPin.equals(newPin2)) {
                            Toast.makeText(mainProcessActivity, "新密码不匹配", Toast.LENGTH_SHORT).show();
                        } else if (oldPin.equals(newPin)) {
                            Toast.makeText(mainProcessActivity, "新老密码不能相同", Toast.LENGTH_SHORT).show();
                        } else {
                            dialogFragment.dismiss();
                        }
                        try {
                            store.open();
                            ICertificate x509Certificate = store.getCertificate(cert.getId());
                            if (x509Certificate.isPrivateKeyAccessible()) {
                                IPrivateKey privateKey = x509Certificate.getPrivateKey();
                                privateKey.updatePin(oldPin, newPin);
                                store.updatePrivateKey(privateKey);
                                x509Certificate = store.getCertificate(cert.getEncCertId());
                                if (x509Certificate.isPrivateKeyAccessible()) {
                                    privateKey = x509Certificate.getPrivateKey();
                                    privateKey.updatePin(oldPin, newPin);
                                    store.updatePrivateKey(privateKey);
                                }
                                store.save();
                                store.close();

                                DataProcessResponse      response=  new DataProcessResponse(BaseService.MSG_SUCCESS, "", "");
                                processListener.doFinish(response,null);
                            } else {
                                processListener.doException(new CmSdkException("private key is not accessible"));
                            }

                        }catch (Exception e){
                            Log.e("Exception",e.getMessage(),e);
                            processListener.doException(new CmSdkException(e.getMessage()));
                        }

                    }
                });
        curDialog.show(manager, "confirm");
    }
}
