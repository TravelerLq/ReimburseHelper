package cn.unitid.spark.cm.sdk.business;

import android.os.Handler;
import android.os.Message;

import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineResponse;
import cn.com.syan.spark.client.sdk.exception.SparkClientException;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 * 获取验证码
 * Created by lyb on 2017/8/30.
 */
public class VCodeGetService {
    private static final String TAG = VCodeGetService.class.getName();

    private Handler handler;
    private OnlineClient onlineClient;

    /**
     * 构造证书注册服务对象
     * @param processListener    回调方法实现
     */
    public VCodeGetService(OnlineClient onlineClient, final ProcessListener<OnlineResponse> processListener){
        this.onlineClient=onlineClient;
        handler = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case -1:
                        SparkClientException e = (SparkClientException)msg.obj;
                        processListener.doException(new CmSdkException(e.getMessage()));
                        break;
                    case 0:
                        OnlineResponse response=(OnlineResponse)msg.obj;
                        processListener.doFinish(response, null);
                }
            }
        };

    }
//    public void register(final String templateId,final List<RegProperty> propertyList){
//        (new Thread() {
//            public void run() {
//                try {
//                    OnlineApplyResponse onlineApplyResponse=onlineClient.apply(templateId,propertyList);
//                    handler.obtainMessage(0,onlineApplyResponse).sendToTarget();
//                } catch (Exception var2) {
//                    handler.obtainMessage(-1,new SparkClientException(var2.getMessage(), var2)).sendToTarget();
//                }
//            }
//        }).start();
//    }


    public void get(final String telephone){
        if(telephone==null || "".equals(telephone)){
            throw new RuntimeException("telephone is required ");
        }

        (new Thread() {
            public void run() {
                try {

                    OnlineResponse onlineResponse=onlineClient.getVCode(telephone);
                    handler.obtainMessage(0,onlineResponse).sendToTarget();
                } catch (Exception var2) {
                    handler.obtainMessage(-1,new SparkClientException(var2.getMessage(), var2)).sendToTarget();
                }
            }
        }).start();
    }

}
