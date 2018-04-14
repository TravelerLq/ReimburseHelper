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
 *  数字信息封信实现
 */
public class EnvelopeSealService extends BService {
    private boolean useEncCert=true;

    /**
     * 构造数字信息封信实现
     * @param activity
     * @param useEncCert
     * @param processListener
     */
    public EnvelopeSealService(FragmentActivity activity,boolean useEncCert,ProcessListener<DataProcessResponse> processListener){
        super(activity,processListener);
        this.useEncCert=useEncCert;

       sealAct(0);
    }

    @Override
    protected String getDataProcessType() {
        return DataProcessType.ENVELOPE_SEAL.name();
    }

    private void sealAct(int step) {
        if (curDialog != null) {
            curDialog.dismiss();
        }
        if (step == 0) {
            curDialog = new CertificateSelectDialogFragment()
                    .setOnSelectListener(new CertificateSelectDialogFragment.OnSelectListener() {
                        @Override
                        public void onSelect(DialogFragment fragment, Certificate certificate) {
                            selectedCertificate = certificate;
                            sealAct(1);
                        }
                    }).setIsSign(useEncCert);
            curDialog.setOnCancelClickListener(new DialogFragmentClickListener() {
                @Override
                public void onClick(DialogFragment dialogFragment) {
                    curDialog.dismiss();
                }
            });
            curDialog.show(manager, "select");
        } else if (step == 1) {


            try {
                DataProcessResponse response = store.sealEnvelope(selectedCertificate, data);
                processListener.doFinish(response, selectedCertificate.getX509Certificate().toBase64String());
            }catch (Exception e){
                processListener.doException(new CmSdkException(e.getMessage()));
            }
        }
    }
}
