package com.sas.rh.reimbursehelper.widget;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Kyle on 2016/1/4.
 */
public class BaseDialog extends Dialog {
    protected  ItemChoose itemChoose;
    public BaseDialog(Context context) {
        super(context);
    }
    public BaseDialog(Context context, int theme) {
        super(context,theme);
    }
    public interface ItemChoose{
        void onItemChoosed(String data);
    }
    public void setItemChooseListener(ItemChoose choose){
        itemChoose=choose;
    }
}
