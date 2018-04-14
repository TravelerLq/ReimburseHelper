package cn.unitid.spark.cm.sdk.data.response;


import cn.com.syan.spark.client.sdk.data.response.Response;

/**
 * 业务处理返回结果信息
 */
public class ObjectResponse extends Response {
    private Object object = null;

    /**
     * 构造业务返回信息
     * @param ret  返回状态 0 成功  -1 失败
     * @param message  业务执行异常信息描述
     * @param object  业务返回对象
     */
    public ObjectResponse(int ret, String message, Object object) {
        setRet(ret);
        setMessage(message);
        this.object = object;
    }
    /**
     * 返回对象
     * @return obejct
     */
    public Object getObject() {
        return object;
    }

    @Override
    public String getJSONString() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("");
    }
}