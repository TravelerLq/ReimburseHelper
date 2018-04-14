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
 * P7签名服务
 */
public class SignatureP7Service extends BService {

    /**
     * 构造P7签名服务实现
     * @param activity
     * @param processListener
     */
    public SignatureP7Service(FragmentActivity activity,ProcessListener<DataProcessResponse> processListener){
        super(activity,processListener);
        data = mainProcessActivity.getIntent().getStringExtra("data");
        if (data == null || data.equals("")) {
            processListener.doException(new CmSdkException("data 不能为空"));
        }
        signAct(0);
    }

    @Override
    protected String getDataProcessType() {
        return DataProcessType.SIGNATURE_P7.name();
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
                            selectedCertificate=certificate;

                            signAct(1);
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
                    DataProcessResponse response = store.p7Sign(selectedCertificate, data, _pin);
                    processListener.doFinish(response, selectedCertificate.getX509Certificate().toBase64String());
                }catch (Exception e){
                    processListener.doException(new CmSdkException(e.getMessage()));
                }

            }
        }
    }
}
