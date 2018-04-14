package cn.unitid.spark.cm.sdk.common;

/**
 *CA过滤条件
 */
public enum CertificateFilterCaConstrain {
    NotCA(0), CA(1);

    private int value = 0;

    private CertificateFilterCaConstrain(int value) {
        this.value = value;
    }

    public static CertificateFilterCaConstrain valueOf(int value) {
        switch (value) {
            case 0:
                return NotCA;
            case 1:
                return CA;
            default:
                return NotCA;
        }
    }

    public int value() {
        return this.value;
    }
}