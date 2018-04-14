package cn.unitid.spark.cm.sdk.business;


import android.app.Activity;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;

import cn.com.syan.jcee.utils.codec.binary.Base64;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import cn.unitid.spark.cm.sdk.ui.CertificateSelectDialogFragment;
import cn.unitid.spark.cm.sdk.ui.DialogFragmentClickListener;
import cn.unitid.spark.cm.sdk.ui.InputDialogFragment;

/**
 * P1签名服务
 */
public class SignatureP1Service extends BService {

    /**
     * 构造P1签名服务实现
     *
     * @param activity
     * @param processListener
     */
    public SignatureP1Service(FragmentActivity activity, ProcessListener<DataProcessResponse> processListener) {
        super(activity, processListener);
        this.processListener = processListener;
        signAct(0);
    }

    public SignatureP1Service(FragmentActivity activity, String pin, ProcessListener<DataProcessResponse> processListener) {
        super(activity, processListener);
        this.processListener = processListener;
        _pin = pin;
        signAct(0);
    }

    @Override
    protected String getDataProcessType() {
        return DataProcessType.SIGNATURE_P1.name();
    }

    private void signAct(int step) {
        if (curDialog != null) {
            curDialog.dismiss();
        }
        if (step == 0) {
            curDialog = new CertificateSelectDialogFragment()
                    .setPrivateKeyAccessible(true)
                    .setIsSign(true)
                    .setOnSelectListener(new CertificateSelectDialogFragment.OnSelectListener() {
                        @Override
                        public void onSelect(DialogFragment fragment, Certificate certificate) {
                            selectedCertificate = certificate;
                            if (!TextUtils.isEmpty(_pin)) {
                                signAct(2);
                            } else {
                                signAct(1);
                            }

                        }
                    }).setOnCancelClickListener(new DialogFragmentClickListener() {
                        @Override
                        public void onClick(DialogFragment dialogFragment) {
                            curDialog.dismiss();
                        }
                    });
            curDialog.show(manager, "select");
        } else if (step == 1) {
            curDialog = new InputDialogFragment()
                    .setPassword(true)
                    .setTitle("请输入PIN码")
                    .setOnConfirmListener(new InputDialogFragment.OnConfirmListener() {
                        @Override
                        public void onConfirm(DialogFragment dialogFragment, String str) {
                            _pin = str;
                            signAct(2);
                        }
                    })
                    .setOnBackClickListener(new DialogFragmentClickListener() {
                        @Override
                        public void onClick(DialogFragment dialogFragment) {
                            signAct(0);
                        }
                    })
                    .setOnCancelClickListener(new DialogFragmentClickListener() {
                        @Override
                        public void onClick(DialogFragment dialogFragment) {
                            signAct(0);
                        }
                    });
            curDialog.show(manager, "pin");
        } else if (step == 2) {
            if (_pin.equals("")) {
                Toast.makeText(mainProcessActivity, "请输入PIN码", Toast.LENGTH_SHORT).show();
                signAct(1);
            } else {


                try {
                    System.out.println("selectedCertificate----" + selectedCertificate.getSubject());
                    DataProcessResponse response = null;
                    if (base64) {
                        byte[] b64 = Base64.decodeBase64(data.getBytes());
                        response = CBSCertificateStore.getInstance().p1Sign(selectedCertificate, b64, _pin);
                    } else {
                        response = CBSCertificateStore.getInstance().p1Sign(selectedCertificate.getId(), data, _pin);
                    }

                    processListener.doFinish(response, selectedCertificate.getX509Certificate().toBase64String());
                } catch (Exception e) {
                    processListener.doException(new CmSdkException(e.getMessage()));
                }


            }
        }
    }
}
