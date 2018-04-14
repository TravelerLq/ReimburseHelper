package cn.unitid.spark.cm.sdk.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import cn.unitid.spark.cm.sdk.R;


/**
 * 默认的签发证书弹出框,签发证书时弹出
 */
public class CertificateIssueDialogFragment extends IssueDialogFragment {
    private NumberOnlyKeyBoard keyBoard;
    private EditText aliasView;
    private EditText pinView;
    private EditText confirmPinView;
    private RelativeLayout keyboardView;
    private boolean passwordShowing = false;
    private LinearLayout showPasswordView;
    private ImageView showPasswordImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_certificate_issue, container, false);
        aliasView = (EditText) view.findViewById(R.id.dialog_certificate_issue_alias);
        pinView = (EditText) view.findViewById(R.id.dialog_certificate_issue_pin);
        confirmPinView = (EditText) view.findViewById(R.id.dialog_certificate_issue_pin_confirm);
        Button cancel = (Button) view.findViewById(R.id.dialog_certificate_issue_cancel);
        Button ok = (Button) view.findViewById(R.id.dialog_certificate_issue_ok);
        showPasswordView = (LinearLayout) view.findViewById(R.id.dialog_certificate_pin_show);
        showPasswordImage = (ImageView) view.findViewById(R.id.dialog_certificate_pin_show_icon);
        keyboardView = (RelativeLayout) view.findViewById(R.id.keyboard);
        keyBoard = new NumberOnlyKeyBoard(view, getActivity());
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelListener.onClick(CertificateIssueDialogFragment.this);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String alias = aliasView.getText().toString().trim();
                String pin = pinView.getText().toString().trim();
                String confirm = confirmPinView.getText().toString().trim();
                if (alias.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_certificate_issue_noalias), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pin.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_certificate_issue_nopin), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirm.equals("")) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_certificate_issue_nopin_confirm), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pin.equals(confirm)) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.txt_certificate_issue_confirm_faild), Toast.LENGTH_SHORT).show();
                    return;
                }
                onConfirmListener.onConfirm(CertificateIssueDialogFragment.this, alias, pin);
            }
        });
        aliasView.setText(alias);
        aliasView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    keyBoard.hideKeyboard();
                    keyboardView.setVisibility(View.GONE);
                }
                return false;
            }
        });
        pinView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    keyBoard.showKeyboard(pinView);
                    hideSoftInput(aliasView);
                    keyboardView.setVisibility(View.VISIBLE);
                    pinView.requestFocus();
                }
                return true;
            }
        });
        confirmPinView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    keyBoard.showKeyboard(confirmPinView);
                    hideSoftInput(aliasView);
                    keyboardView.setVisibility(View.VISIBLE);
                    confirmPinView.requestFocus();
                }
                return true;
            }
        });
        showPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword(!passwordShowing);
            }
        });
        showPassword(passwordShowing);
        keyBoard.showKeyboard(pinView);
        hideSoftInput(aliasView);
        keyboardView.setVisibility(View.VISIBLE);
        pinView.requestFocus();
        return view;
    }

    private void showPassword(boolean showing) {
        passwordShowing = showing;
        showPassword(pinView, passwordShowing);
        showPassword(confirmPinView, passwordShowing);
        if (passwordShowing) {
            showPasswordImage.setImageResource(R.drawable.background_myappapproved_checked);
        } else {
            showPasswordImage.setImageResource(0);
        }
    }

    private void hideSoftInput(View view) {
        if (inputMethodManager != null) {
            if (view.getWindowToken() != null) {
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public static EditText showPassword(EditText view, boolean isShow) {
        if (isShow) {
            view.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            view.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        Editable etable = view.getText();
        Selection.setSelection(etable, etable.length());
        return view;
    }

    public void show(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }


}
