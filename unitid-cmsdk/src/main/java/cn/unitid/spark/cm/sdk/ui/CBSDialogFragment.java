package cn.unitid.spark.cm.sdk.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import cn.unitid.spark.cm.sdk.R;


public class CBSDialogFragment extends DialogFragment {
    private static final String TAG = CBSDialogFragment.class.getName();
    protected InputMethodManager inputMethodManager;
    protected DialogFragmentClickListener onCancelListener = new DefaultOnCancelClickListener();
    protected DialogFragmentClickListener onBackListener = new DefaultOnCancelClickListener();

    public CBSDialogFragment setOnCancelClickListener(DialogFragmentClickListener onCancelListener) {
        this.onCancelListener = onCancelListener;
        return this;
    }

    public CBSDialogFragment setOnBackClickListener(DialogFragmentClickListener onBackListener) {
        this.onBackListener = onBackListener;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(STYLE_NORMAL, R.style.MyDialogFrame);
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {

            WindowManager windowManager = getActivity().getWindowManager();
            getView().setLayoutParams(new FrameLayout.LayoutParams(windowManager.getDefaultDisplay().getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
        } catch (Exception e) {
            Log.e(TAG, "", e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        onBackListener.onClick(CBSDialogFragment.this);
                        return true;
                    }
                }
                return false;
            }
        });
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void hideSoftInput() {
        if (inputMethodManager != null) {
            if (getActivity().getCurrentFocus() != null) {
                if (getActivity().getCurrentFocus().getWindowToken() != null) {
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    private class DefaultOnCancelClickListener implements DialogFragmentClickListener {
        @Override
        public void onClick(DialogFragment dialogFragment) {
            dialogFragment.dismiss();
        }
    }
}
