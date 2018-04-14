package cn.unitid.spark.cm.sdk.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import cn.unitid.spark.cm.sdk.R;
import cn.unitid.spark.cm.sdk.common.Utils;


/**
 * Created by zhuyj on 2015/2/6.
 * Modified by brochexu on 3/24/2015
 */

/**
 * 修改PIN码弹出框
 */
public class CertificateSetPINDialogFragment extends CBSDialogFragment {
    private onConfirmListener onConfirmListener;
    private EditText oldView;
    private EditText newView;
    private EditText confirmView;
    private NumberOnlyKeyBoard keyBoard;
    private boolean passwordShowing = false;
    private LinearLayout showPasswordView;
    private ImageView showPasswordImage;

    public CertificateSetPINDialogFragment setOnConfirmListener(CertificateSetPINDialogFragment.onConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.dialog_certificate_pin, container, false);
        oldView = (EditText) view.findViewById(R.id.dialog_certificate_pin_old);
        newView = (EditText) view.findViewById(R.id.dialog_certificate_pin_new);
        confirmView = (EditText) view.findViewById(R.id.dialog_certificate_pin_confirm);
        showPasswordView = (LinearLayout) view.findViewById(R.id.dialog_certificate_pin_show);
        showPasswordImage = (ImageView) view.findViewById(R.id.dialog_certificate_pin_show_icon);
        Button cancel = (Button) view.findViewById(R.id.dialog_certificate_pin_cancel);
        Button ok = (Button) view.findViewById(R.id.dialog_certificate_pin_ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelListener.onClick(CertificateSetPINDialogFragment.this);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmListener.onConfirm(CertificateSetPINDialogFragment.this, oldView.getText().toString(), newView.getText().toString(), confirmView.getText().toString());
            }
        });
        showPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(!passwordShowing);
            }
        });

        showPassword(passwordShowing);

        keyBoard = new NumberOnlyKeyBoard(view, getActivity());
        keyBoard.showKeyboard(oldView);
        oldView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (keyBoard == null) {
                    keyBoard = new NumberOnlyKeyBoard(view, getActivity());
                }
                keyBoard.showKeyboard(oldView);
                oldView.requestFocus();
                return true;
            }
        });

        newView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (keyBoard == null) {
                    keyBoard = new NumberOnlyKeyBoard(view, getActivity());
                }
                keyBoard.showKeyboard(newView);
                newView.requestFocus();
                return true;
            }
        });

        confirmView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (keyBoard == null) {
                    keyBoard = new NumberOnlyKeyBoard(view, getActivity());
                }
                keyBoard.showKeyboard(confirmView);
                confirmView.requestFocus();
                return true;
            }
        });

        return view;
    }

    private void showPassword(boolean showing) {
        passwordShowing = showing;
        Utils.showPassword(oldView, passwordShowing);
        Utils.showPassword(newView, passwordShowing);
        Utils.showPassword(confirmView, passwordShowing);
        if (passwordShowing) {
            showPasswordImage.setImageResource(R.drawable.background_myappapproved_checked);
        } else {
            showPasswordImage.setImageResource(0);
        }
    }

    public interface onConfirmListener {
        public void onConfirm(DialogFragment dialogFragment, String oldPin, String newPin, String newPin2);
    }
}
