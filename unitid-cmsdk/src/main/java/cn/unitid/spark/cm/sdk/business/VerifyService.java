package cn.unitid.spark.cm.sdk.business;

import android.support.v4.app.FragmentActivity;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 * 验签服务
 */
public class VerifyService extends BService {
    private DataProcessType verifyType;

    /**
     * 构造验签服务实现
     * @param activity
     * @param verifyType
     * @param processListener
     */
    public VerifyService(FragmentActivity activity,DataProcessType verifyType,ProcessListener<DataProcessResponse> processListener){
        super(activity,processListener);
        this.verifyType=verifyType;
        verify();
    }

    @Override
    protected String getDataProcessType() {
        return verifyType.name();
    }

    private void verify(){

        String  sign =mainProcessActivity.getIntent().getStringExtra("sign");
        String  certValue = mainProcessActivity.getIntent().getStringExtra("cert");
        boolean result=false;

            try {
                if(verifyType==DataProcessType.VERIFY_P1){
                    result = store.p1Verify(sign, data, certValue);
                }else{
                    result =  store.p7Verify(sign);
                }
                if (result) {
                    processListener.doFinish(new DataProcessResponse(0,"验签成功","true"),certValue);
                } else {
                    processListener.doFinish(new DataProcessResponse(-1,"验签失败","false"),certValue);
                }
            }catch (Exception e){
                processListener.doException(new CmSdkException(e.getMessage()));
            }


    }
}
