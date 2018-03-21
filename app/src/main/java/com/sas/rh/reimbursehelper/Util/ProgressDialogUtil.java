package com.sas.rh.reimbursehelper.Util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by dh86 on 2017/12/28.
 */

public class ProgressDialogUtil {

    private Context context;
    private String title;
    private String message;
    ProgressDialog mypDialog;

    public ProgressDialogUtil(Context context, String title, String message) {
        this.context = context;
        this.title = title;
        this.message = message;
    }

    public void showpd(){
        mypDialog = new ProgressDialog(context);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //mypDialog.setTitle("提示信息");
        mypDialog.setMessage(message);
        //.setIcon(R.drawable.android);
        mypDialog.setIndeterminate(false);
        mypDialog.setCancelable(false);
        mypDialog.setCanceledOnTouchOutside(false);
        mypDialog.show();
    }

    public void dismisspd(){
        mypDialog.dismiss();
    }

    public ProgressDialog getMypDialog() {
        return mypDialog;
    }
    public void setDialogMessage(String msg) {
        mypDialog.setMessage(msg);
    }
}
