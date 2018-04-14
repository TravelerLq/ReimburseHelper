package cn.unitid.spark.cm.sdk.listener;

import android.support.v4.app.DialogFragment;

/**
 * Created by lenovo on 2017/9/1.
 */

public interface IssueOnConfirmListener {
    public void onConfirm(DialogFragment dialogFragment, String alias, String pin);
}
