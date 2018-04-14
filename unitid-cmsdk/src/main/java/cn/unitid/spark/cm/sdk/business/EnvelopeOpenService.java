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
 * 数字信息解封实现
 * Created by lyb on 2016/6/30.
 */
public class EnvelopeOpenService extends BService {
    private boolean useEncCert=true;

    /**
     * 构造数字信息解封实现
     * @param activity
     * @param useEncCert
     * @param processListener
     */
    public EnvelopeOpenService(FragmentActivity activity,boolean useEncCert,ProcessListener<DataProcessResponse> processListener){
        super(activity,processListener);
        this.useEncCert=useEncCert;
        openAct(0);
    }

    @Override
    protected String getDataProcessType() {
        return DataProcessType.ENVELOPE_OPEN.name();
    }

    private void openAct(int step) {
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
                            openAct(1);
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
            curDialog = new InputDialogFragment()
                    .setPassword(true)
                    .setTitle("请输入PIN码")
                    .setOnConfirmListener(new InputDialogFragment.OnConfirmListener() {
                        @Override
                        public void onConfirm(DialogFragment dialogFragment, String str) {
                            _pin = str;
                            openAct(2);
                        }
                    })
                    .setOnBackClickListener(new DialogFragmentClickListener() {
                        @Override
                        public void onClick(DialogFragment dialogFragment) {
                            openAct(0);
                        }
                    })
                    .setOnCancelClickListener(new DialogFragmentClickListener() {
                        @Override
                        public void onClick(DialogFragment dialogFragment) {
                            openAct(0);
                        }
                    });
            curDialog.show(manager, "pin");
        } else if (step == 2) {
            if (_pin.equals("")) {
                Toast.makeText(mainProcessActivity, "请输入PIN码", Toast.LENGTH_SHORT).show();
                openAct(1);
            } else {

                try {
                    DataProcessResponse response = store.openEnvelope(selectedCertificate, data, _pin);
                    processListener.doFinish(response, selectedCertificate.getX509Certificate().toBase64String());
                }catch (Exception e){
                    processListener.doException(new CmSdkException(e.getMessage()));
                }
            }
        }
    }
}
