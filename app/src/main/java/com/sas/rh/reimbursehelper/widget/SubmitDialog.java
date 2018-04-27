package com.sas.rh.reimbursehelper.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;


/**
 * 提交成功Dialog
 * Created by liqing on 17/5/3.
 */

public class SubmitDialog extends BaseDialog {
    private Button btnOk, btnCancel;
    private EditText edtHint;
    private TextView mDialogTitle;
    private OnOKCancelListener listener;
    private Context mContext;

    public interface OnOKCancelListener {
        void onOkClick();

        void onCancelClick();
    }

    public void setOnUpdateListener(OnOKCancelListener listener) {
        this.listener = listener;
    }

    public SubmitDialog(Context context, int theme) {
        super(context, theme);
    }

    public SubmitDialog(Context context) {
        super(context, R.style.CustomDialog);
        mContext = context;
        setOwnerActivity((Activity) context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.dialog_submit);
        setCanceledOnTouchOutside(true);
        btnOk = (Button) findViewById(R.id.btn_update);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        edtHint = (EditText) findViewById(R.id.edt_hint);
        mDialogTitle = (TextView) findViewById(R.id.tv_dialog_title);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null)
                    listener.onOkClick();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null)
                    listener.onCancelClick();
            }
        });
    }

    public void setContent(String msg) {
        edtHint.setText(msg);
    }

    public void setDialogTitle(String title) {
        mDialogTitle.setText(title);
    }

    public class ShareData {
        public String name;
        public int img;
    }
}
