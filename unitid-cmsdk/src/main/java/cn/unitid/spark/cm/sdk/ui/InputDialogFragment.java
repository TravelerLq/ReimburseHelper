package cn.unitid.spark.cm.sdk.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import cn.unitid.spark.cm.sdk.R;


/**
 * Created by zhuyj on 2015/1/20.
 * Modified by brochexu on 3/24/2015
 */

/**
 * 单行信息输入弹出框
 */
public class InputDialogFragment extends CBSDialogFragment {
    private EditText input;
    private String title;
    private boolean isPassword = false;
    private NumberOnlyKeyBoard keyBoard;
    private OnConfirmListener onConfirmListener;
    private String content;
    private boolean passwordShowing = false;
    private LinearLayout showPasswordView;
    private ImageView showPasswordImage;

    public InputDialogFragment setTitle(String title) {
        this.title = title;
        return this;
    }

    public InputDialogFragment setPassword(boolean isPassword) {
        this.isPassword = isPassword;
        return this;
    }

    public InputDialogFragment setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    public InputDialogFragment setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View view = inflater.inflate(R.layout.dialog_input, container, false);
        TextView titleView = (TextView) view.findViewById(R.id.dialog_input_title);
        showPasswordView = (LinearLayout) view.findViewById(R.id.dialog_input_show);
        showPasswordImage = (ImageView) view.findViewById(R.id.dialog_input_show_icon);

        titleView.setText(title);
        input = (EditText) view.findViewById(R.id.dialog_input_input);
        if (content != null && !content.equals("null")) {
            input.setText(content);
        }
        keyBoard = new NumberOnlyKeyBoard(view, getActivity());
        if (isPassword) {
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            view.findViewById(R.id.keyboard_show).setVisibility(View.VISIBLE);
        }
        Button cancel = (Button) view.findViewById(R.id.dialog_input_cancel);
        Button ok = (Button) view.findViewById(R.id.dialog_input_ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelListener.onClick(InputDialogFragment.this);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmListener.onConfirm(InputDialogFragment.this, input.getText().toString());
            }
        });

        if (isPassword) {
            keyBoard.showKeyboard(input);
            input.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (keyBoard == null) {
                        keyBoard = new NumberOnlyKeyBoard(view, getActivity());
                    }
                    keyBoard.showKeyboard(input);
                    input.requestFocus();
                    return true;
                }
            });
            showPasswordView.setVisibility(View.VISIBLE);
            showPassword(passwordShowing);

            showPasswordView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPassword(!passwordShowing);
                }
            });
        } else {
            showPasswordView.setVisibility(View.GONE);
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    inputMethodManager.showSoftInput(input, 0);
                }
            }.sendEmptyMessageDelayed(0, 50);
        }
        return view;
    }

    private void showPassword(boolean showing) {
        passwordShowing = showing;
        showPassword(input, passwordShowing);
        if (passwordShowing) {
            showPasswordImage.setImageResource(R.drawable.background_myappapproved_checked);
        } else {
            showPasswordImage.setImageResource(0);
        }
    }

    public interface OnConfirmListener {
        public void onConfirm(DialogFragment dialogFragment, String str);
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

    public void onResume() {
        super.onResume();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width2 = outMetrics.widthPixels;
        int height2 = outMetrics.heightPixels;
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(width2,(int) height2*4/5);
    }
}