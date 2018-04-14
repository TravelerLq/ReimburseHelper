package cn.unitid.spark.cm.sdk.ui;

import cn.unitid.spark.cm.sdk.listener.IssueOnConfirmListener;


/**
 * 签发证书时弹出
 * 自定义签发证书弹出框
 * 自定义样式弹出框可以继承本类
 */
public class IssueDialogFragment extends CBSDialogFragment {
    protected IssueOnConfirmListener onConfirmListener;
    protected String alias;
    public void setOnConfirmListener(IssueOnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
