package cn.unitid.spark.cm.sdk.listener;


import cn.unitid.spark.cm.sdk.exception.CmSdkException;

/**
 * 证书相关业务处理的回调接口
 */
public interface ProcessListener<T> {
    /**
     *  业务执行成功调用接口
     * @param a  回调业务参数
     * @param cert 操作的证书base64编码字符串
     */
    public void doFinish(T a,String cert);

    /**
     * 业务执行异常后调用接口
     * @param exception 异常信息 {@link cn.unitid.spark.cm.sdk.exception.CmSdkException}
     */
    public void doException(CmSdkException exception);
}
