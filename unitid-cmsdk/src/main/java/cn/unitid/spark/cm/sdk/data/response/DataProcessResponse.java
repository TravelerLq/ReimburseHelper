package cn.unitid.spark.cm.sdk.data.response;

import cn.com.syan.spark.client.sdk.data.response.Response;

/**
 * 业务处理返回结果信息
 */
public class DataProcessResponse extends Response {
    private String result = null;

    /**
     * 构造业务返回信息
     * @param ret  返回状态 0 成功  -1 失败
     * @param message  业务执行异常信息描述
     * @param result  业务返回值
     */
    public DataProcessResponse(int ret, String message, String result) {
        setRet(ret);
        setMessage(message);
        this.result = result;
    }

    /**
     * 返回信息字符串
     * @return String
     */
    public String getResult() {
        return result;
    }
}