package cn.unitid.spark.cm.sdk.business;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import cn.unitid.spark.cm.sdk.ui.CertificateSelectDialogFragment;
import cn.unitid.spark.cm.sdk.ui.DialogFragmentClickListener;
import cn.unitid.spark.cm.sdk.ui.InputDialogFragment;

/**
 * 解密信息
 * Created by lyb on 2016/6/30.
 */
public class DecryptionService extends BService {

    /**
     * 构造解密服务实现
     * @param activity
     * @param processListener
     */
    public DecryptionService(FragmentActivity activity,ProcessListener<DataProcessResponse> processListener){
        super(activity,processListener);
        decryptAct(0);
    }

    @Override
    protected String getDataProcessType() {
        return DataProcessType.DECRYPT.name();
    }

    private void decryptAct(int step) {
        if (curDialog != null) {
            curDialog.dismiss();
        }
        if (step == 0) {
            curDialog = new CertificateSelectDialogFragment()
                    .setPrivateKeyAccessible(true)
                    .setOnSelectListener(new CertificateSelectDialogFragment.OnSelectListener() {
                        @Override
                        public void onSelect(DialogFragment fragment, Certificate certificate) {
                            selectedCertificate=certificate;
                            decryptAct(1);
                        }
                    }).setIsSign(false);
            curDialog.setOnCancelClickListener(new DialogFragmentClickListener() {
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
                            decryptAct(2);
                        }
                    })
                    .setOnBackClickListener(new DialogFragmentClickListener() {
                        @Override
                        public void onClick(DialogFragment dialogFragment) {
                            decryptAct(0);
                        }
                    })
                    .setOnCancelClickListener(new DialogFragmentClickListener() {
                        @Override
                        public void onClick(DialogFragment dialogFragment) {
                            decryptAct(0);
                        }
                    });
            curDialog.show(manager, "pin");
        } else if (step == 2) {
            if (_pin.equals("")) {
                Toast.makeText(mainProcessActivity, "请输入PIN码", Toast.LENGTH_SHORT).show();
                decryptAct(1);
            } else {

                try {
                    DataProcessResponse response = store.privateDecrypt(selectedCertificate, data, _pin);
                    processListener.doFinish(response, selectedCertificate.getX509Certificate().toBase64String());
                }catch (Exception e){
                    processListener.doException(new CmSdkException(e.getMessage()));
                }
            }
        }
    }
}
