package cn.unitid.spark.cm.sdk.common;

/**
 * 操作业务类型定义
 */
public enum DataProcessType {
    /**
     * 解密
     */
    DECRYPT,
    /**
     * 加密
     */
    ENCRYPT,
    /**
     *P1签名
     */
    SIGNATURE_P1,
    /**
     *p7签名
     */
    SIGNATURE_P7,
    /**
     *p1验签
     */
    VERIFY_P1,
    /**
     p7验签
     */
    VERIFY_P7,
    /**
     *数字信封
     */
    ENVELOPE_SEAL,
    /**
     *拆封
     */
    ENVELOPE_OPEN,
    /**
     *签发证书
     */
    ISSUE,
    /**
     *注册证书
     */
    REGISTER,
    /**
     *延期证书
     */
    DELAY
}
