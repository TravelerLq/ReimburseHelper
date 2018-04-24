package com.sas.rh.reimbursehelper.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ExpenseApprovalResponseBean;
import com.sas.rh.reimbursehelper.Bean.ResponseApprovalDetailBean;
import com.sas.rh.reimbursehelper.NetworkUtil.DownloadFileUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.DateUtils;
import com.sas.rh.reimbursehelper.Util.IntentUtils;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtils;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.List;

/**
 * Created by liqing on 18/3/27.
 * 查看报销单 详情
 */

public class ExpenseDetailActivity extends BaseActivity {

    private TextView tvTitle;
    private TextView tvExpenseName;
    private TextView tvProgress;
    private TextView tvApprovalResult;
    private TextView tvFinalResult;
    private TextView tvUpdateTime;
    private TextView tvReason;
    private LinearLayout llReject;
    private static ExpenseApprovalResponseBean itemBean;
    private static String reason;
    private static int approvalId;
    private ImageView ivPdfIcon;
    private int formId;
    private Integer annexId;
    private Context context;
    SharedPreferencesUtil spu;
    private String pdfBase64Str;
    private String pdfPathName;
    private String filePath;
    private static MyHandler myHandler;
    private JSONObject jsonobj;
    private ImageView ivBack;


    private String pdfPath = "/storage/emulated/0/Download/";
    private JSONObject pdfJsonObjec;
    private File file;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_expense_detail;
    }

    @Override
    protected void initData() {
        myHandler = new MyHandler(this);
        spu = new SharedPreferencesUtil(ExpenseDetailActivity.this);
        context = ExpenseDetailActivity.this;
        ivPdfIcon = (ImageView) findViewById(R.id.iv_pdf_icon_expense);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvApprovalResult = (TextView) findViewById(R.id.tv_approval_result);
        tvFinalResult = (TextView) findViewById(R.id.tv_final_result);
        tvUpdateTime = (TextView) findViewById(R.id.tv_update_time);
        tvExpenseName = (TextView) findViewById(R.id.tv_expense_name);
        llReject = (LinearLayout) findViewById(R.id.ll_reject_reason);
        tvReason = (TextView) findViewById(R.id.tv_reason);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle.setText("报销单详情");
        Bundle bundle = (Bundle) getIntent().getExtras().get("bundle");
        itemBean = (ExpenseApprovalResponseBean) bundle.getSerializable("itemBean");
        if (itemBean == null) {
            Log.e("itemBean", "--intent =null");
        } else {
            formId = itemBean.getFormId();
            //get annext download
            getPdfForm();

            //=1 通过
            if (itemBean.getApproveResultId() == null) {
                tvApprovalResult.setText(getResources().getString(R.string.wait_approval));
            } else {

                if (itemBean.getApproveResultId() == 1) {
                    tvApprovalResult.setText(getResources().getString(R.string.pass));
                    llReject.setVisibility(View.GONE);
                } else {
                    tvApprovalResult.setText(getResources().getString(R.string.unpass));
                    llReject.setVisibility(View.VISIBLE);
                    tvReason.setText(itemBean.getRejectReason());

                }
            }

            Log.e("name-", "-getApprovalName" + itemBean.getApprovalName());
            if (!TextUtils.isEmpty(itemBean.getApprovalName())) {
                tvExpenseName.setText(itemBean.getApprovalName().toString());

            }
            if (itemBean.getApproveProcessId() == null) {
                tvFinalResult.setText("暂无");
            } else {
                tvProgress.setText(String.valueOf(itemBean.getApproveProcessId()));
            }


            if (itemBean.getFinallyResultId() == null) {
                tvFinalResult.setText("暂无");
            } else {
                tvFinalResult.setText(String.valueOf(itemBean.getFinallyResultId()));
            }


//            String time = String.valueOf(itemBean.getUpdateTime());

            Date date = itemBean.getUpdateTime();
            String dateStr = DateUtils.parse(date);

            //  Log.e("", "time=" + DateUtils.parse(time));

            tvUpdateTime.setText(dateStr);

        }
        Log.e("approvalId", "--" + approvalId);


    }


    /**
     * 创建静态内部类
     */
    private class MyHandler extends Handler {
        //持有弱引用HandlerActivity,GC回收时会被回收掉.
        private final WeakReference<ExpenseDetailActivity> mActivty;

        public MyHandler(ExpenseDetailActivity activity) {
            mActivty = new WeakReference<ExpenseDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ExpenseDetailActivity activity = mActivty.get();
            super.handleMessage(msg);

            if (activity != null) {
                // 审批－通过成功
                if (msg.what == 4) {
                    pdfBase64Str = pdfJsonObjec.getString("file");

                    String originalFilename = pdfJsonObjec.getString("originalFilename");
                    filePath = pdfPath + originalFilename;
                    if (TextUtils.isEmpty(pdfBase64Str)) {
                        Toast.makeText(context, "文件为空", Toast.LENGTH_SHORT).show();

                    } else {
                        file = DownloadFileUtil.base64StringToPdf(pdfBase64Str, filePath);

                        if (file.exists() && file.length() > 0) {
                            ivPdfIcon.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(context, "生成pdf出错", Toast.LENGTH_SHORT).show();
                            ivPdfIcon.setVisibility(View.GONE);
                        }
                        pdfPathName = file.getPath();
                    }

                } else if (msg.what == 5) {
                    if (annexId != null) {
                        //去下载pdf
                        toDownLoadPdf();
                    }

                } else if (msg.what == 0) {
                    ToastUtil.showToast(ExpenseDetailActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
                } else if (msg.what == -1) {
                    ToastUtil.showToast(ExpenseDetailActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
                }
            }
        }
    }

    private void toDownLoadPdf() {

        new Thread(DownLoadPdfRunnable).start();

    }

    //下载pdf form
    Runnable DownLoadPdfRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = DownloadFileUtil.downPdfForm(annexId, pdfPath);
                if (jo != null) {
                    pdfJsonObjec = jo;

//                    annexId = jsonobj.getInteger("annexId");
                    myHandler.sendEmptyMessage(4);
                } else {
                    myHandler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                myHandler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    private void getPdfForm() {
        new Thread(GetPdfFormRunnable).start();
    }


    //得到需要生成pdf的 annexId
    Runnable GetPdfFormRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = FormUtil.getFormPdf(formId, spu.getUidNum());
                if (jo != null) {
                    jsonobj = jo;
                    annexId = jsonobj.getInteger("annexId");
                    myHandler.sendEmptyMessage(5);
                } else {
                    myHandler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                myHandler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


    @Override
    protected void initListeners() {
        ivBack.setOnClickListener(this);
        ivPdfIcon.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pdf_icon_expense:
                viewPdf();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }

    }

    private void viewPdf() {
        startActivity(IntentUtils.getPdfFileIntent(file, ExpenseDetailActivity.this));
//                     startActivity(IntentUtils.getPdfIntent(file));
    }
}
