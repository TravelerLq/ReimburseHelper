package cn.unitid.spark.cm.sdk.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import cn.unitid.spark.cm.sdk.R;
import cn.unitid.spark.cm.sdk.adapter.CertificateDialogAdapter;
import cn.unitid.spark.cm.sdk.business.CBSCertificateStore;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import cn.unitid.spark.cm.sdk.data.response.ObjectResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * 证书选择
 */
public class CertificateSelectDialogFragment extends CBSDialogFragment {
    private static final String TAG = CertificateSelectDialogFragment.class.getName();
    private ListView listView;
    private TextView noView;
    private CertificateDialogAdapter adapter;
    private List<Certificate> certificates = new ArrayList<Certificate>();
    private OnSelectListener onSelectListener = null;

    private CBSCertificateStore certificateStore = CBSCertificateStore.getInstance();
    private String appId = null;
    private Boolean privateKeyAccessible = null;
    private Boolean isMine = false;

    private Boolean isSign = null;


    public CertificateSelectDialogFragment setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
        return this;
    }

    public CertificateSelectDialogFragment setIsSign(Boolean isSign) {
        this.isSign = isSign;
        return this;
    }

    public CertificateSelectDialogFragment setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public CertificateSelectDialogFragment setPrivateKeyAccessible(Boolean privateKeyAccessible) {
        this.privateKeyAccessible = privateKeyAccessible;
        return this;
    }

    public CertificateSelectDialogFragment setIsMine(Boolean isMine) {
        this.isMine = isMine;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);


        View view = inflater.inflate(R.layout.dialog_certificate_choose, container, false);

        listView = (ListView) view.findViewById(R.id.certificate);
        noView = (TextView) view.findViewById(R.id.nocertificate);
        adapter = new CertificateDialogAdapter(getActivity(), certificates);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Certificate item = certificates.get(position);
                if (onSelectListener != null) {
                    onSelectListener.onSelect(CertificateSelectDialogFragment.this, item);
                }
            }
        });

        Button cancel = (Button) view.findViewById(R.id.dialog_certificate_choose_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelListener.onClick(CertificateSelectDialogFragment.this);
            }
        });

        ObjectResponse response = certificateStore.getCertificateList(false, true, null, isSign);
        List<Certificate> result = (List<Certificate>) response.getObject();


        certificates.clear();
        certificates.addAll(result);
        if (certificates.size() > 0) {
            adapter.notifyDataSetChanged();
            listView.setVisibility(View.VISIBLE);
            noView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            noView.setText("暂无可用证书");
            noView.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public interface OnSelectListener {
        public void onSelect(DialogFragment fragment, Certificate certificate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void onResume() {
        super.onResume();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        int width2 = outMetrics.widthPixels;
        int height2 = outMetrics.heightPixels;
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        getDialog().getWindow().setLayout(width2, (int) height2 * 3 / 4);
    }


}
