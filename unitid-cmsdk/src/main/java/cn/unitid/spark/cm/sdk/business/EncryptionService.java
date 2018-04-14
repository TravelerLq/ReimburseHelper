package cn.unitid.spark.cm.sdk.business;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import cn.unitid.spark.cm.sdk.ui.CertificateSelectDialogFragment;
import cn.unitid.spark.cm.sdk.ui.DialogFragmentClickListener;

/**
 * Created by lyb on 2016/6/30.
 */
public class EncryptionService extends BService {

    /**
     * 构造加密服务实现
     * @param activity
     * @param processListener
     */
    public EncryptionService(FragmentActivity activity,ProcessListener<DataProcessResponse> processListener){
        super(activity,processListener);

        encryptAct(0);
    }

    @Override
    protected String getDataProcessType() {
        return DataProcessType.ENCRYPT.name();
    }

    private void encryptAct(int step) {
        if (curDialog != null) {
            curDialog.dismiss();
        }
        if (step == 0) {
            curDialog = new CertificateSelectDialogFragment()
                    .setOnSelectListener(new CertificateSelectDialogFragment.OnSelectListener() {
                        @Override
                        public void onSelect(DialogFragment fragment, Certificate certificate) {
                            selectedCertificate = certificate;
                            encryptAct(1);
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
            try {
                DataProcessResponse response = store.publicEncrypt(selectedCertificate, data);
                processListener.doFinish(response, selectedCertificate.getX509Certificate().toBase64String());
            }catch (Exception e){
                processListener.doException(new CmSdkException(e.getMessage()));
            }
        }
    }
}
