package cn.unitid.spark.cm.sdk.ui;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import cn.unitid.spark.cm.sdk.R;


/**
 * Created by zhuyj on 2015/2/6.
 * Modified by brochexu 3/24/2015
 */

/**
 * 修改别名弹出框
 */
public class CertificateSetAliasDialogFragment extends CBSDialogFragment {
    private OnConfirmListener onConfirmListener;
    private EditText aliasView;

    public CertificateSetAliasDialogFragment setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.dialog_certificate_alias, container, false);
        aliasView = (EditText) view.findViewById(R.id.dialog_certificate_alias_input);
        Button cancel = (Button) view.findViewById(R.id.dialog_certificate_alias_cancel);
        Button ok = (Button) view.findViewById(R.id.dialog_certificate_alias_ok);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelListener.onClick(CertificateSetAliasDialogFragment.this);
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmListener.onConfirm(CertificateSetAliasDialogFragment.this, aliasView.getText().toString());
            }
        });
        return view;
    }

    public interface OnConfirmListener {
        public void onConfirm(DialogFragment dialogFragment, String alias);
    }
}
