package com.sas.rh.reimbursehelper.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Adapter.AddExpenseRecycleViewAdapter;
import com.sas.rh.reimbursehelper.Adapter.ApprovalDetailRecycleViewAdapter;
import com.sas.rh.reimbursehelper.Adapter.ApprovalFormRecycleAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ExpenseApprovalResponseBean;
import com.sas.rh.reimbursehelper.Bean.ResponseApprovalDetailBean;
import com.sas.rh.reimbursehelper.Bean.SingleReimbursement;
import com.sas.rh.reimbursehelper.NetworkUtil.ApprovalUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.DownloadFileUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.DateUtils;
import com.sas.rh.reimbursehelper.Util.DialogUtils;
import com.sas.rh.reimbursehelper.Util.IntentUtils;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtils;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.unitid.spark.cm.sdk.business.SignatureP1Service;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 * Created by liqing on 18/3/27.
 * 查看审批单详情 ( 进行通过 ／不通过 审批的 页面)
 */

public class ApprovalDetailActivity extends BaseActivity {
    private Button btnApproval;
    private TextView tvProgress;
    private EditText editReason;
    private TextView tvTitle;
    private TextView tvUpdateTime;
    private static JSONObject jsonResult;
    private TextView tvExpenseName;

    private static ExpenseApprovalResponseBean itemBean;
    private static MyHandler myHandler;
    private static int userId;
    private static Byte approveResultId;
    private static String reason;
    private static int approvalId;
    private Button btnUnpass;
    private String type;
    private LinearLayout llReject;
    private RecyclerView recyclerView;
    private static int formId;
    private LinearLayoutManager bxLayoutManager;
    // private ApprovalDetailRecycleViewAdapter bxAdapter;
    private ApprovalFormRecycleAdapter bxAdapter;
    private List<ResponseApprovalDetailBean> mData;
    private Context context;
    private ImageView ivPdfIcon;
    SharedPreferencesUtil spu;
    private String pdfBase64Str;
    private String pdfPathName;
    private String filePath;
    private int code;
    private ProgressDialog dialogLoading;
    private File file;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_approval_detail;
    }

    @Override
    protected void initData() {
        btnApproval = (Button) findViewById(R.id.btn_approval);
        editReason = (EditText) findViewById(R.id.edt_reason);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvUpdateTime = (TextView) findViewById(R.id.tv_time);
        tvExpenseName = (TextView) findViewById(R.id.tv_expense_name);
        tvProgress = (TextView) findViewById(R.id.tv_approval_progress);
        btnUnpass = (Button) findViewById(R.id.btn_unpass);
        llReject = (LinearLayout) findViewById(R.id.ll_reject_reason);
        recyclerView = (RecyclerView) findViewById(R.id.rl_approval_detail);
        ivPdfIcon = (ImageView) findViewById(R.id.iv_approval_pdf);

        context = ApprovalDetailActivity.this;
        // context = getApplicationContext();
        spu = new SharedPreferencesUtil(ApprovalDetailActivity.this);
        tvTitle.setText("审批单详情");

        myHandler = new MyHandler(this);
        SharedPreferencesUtil spu = new SharedPreferencesUtil(ApprovalDetailActivity.this);
        userId = spu.getUidNum();
        Bundle bundle = (Bundle) getIntent().getExtras().get("bundle");
        itemBean = (ExpenseApprovalResponseBean) bundle.getSerializable("itemBean");
        if (itemBean == null) {
            Log.e("itemBean", "--intent =null");
        } else {
            approvalId = itemBean.getApprovalId();
            formId = itemBean.getFormId();
            //formId = 593;
            Loger.e("approval dedtail---formId="+formId);
            getExpenseForms(formId);

            getPdfForm();
            tvExpenseName.setText(itemBean.getApprovalName());
            tvProgress.setText(String.valueOf(itemBean.getApproveProcessId()));
            Date date = itemBean.getUpdateTime();
            String dateStr = DateUtils.parse(date);

            tvUpdateTime.setText(dateStr);

        }
        Log.e("approvalId", "--" + approvalId);

        // 通过 approvalId=1
        btnApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // DialogUtils.startLoad(ApprovalDetailActivity.this, null);
                // DialogUtils.startLoad(context, null);
                ProgressDialogUtils.instance(context).show("提交中");
                approveResultId = 1;
                llReject.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(pdfBase64Str)) {
                    //签名
                    signVerifyP1(pdfBase64Str);
                }

                // submitApproval();

            }
        });
        //unpass =2
        btnUnpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                approveResultId = 2;
                llReject.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(editReason.getText().toString())) {
                    Toast.makeText(ApprovalDetailActivity.this, "请填写驳回理由", Toast.LENGTH_SHORT).show();
                } else {
                    submitApproval();
                }
            }
        });
        ivPdfIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(IntentUtils.getPdfFileIntent(file, ApprovalDetailActivity.this));
//                     startActivity(IntentUtils.getPdfIntent(file));
            }
        });

        initRecycle();
    }

    private void initRecycle() {
        mData = new ArrayList<>();
        bxLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bxAdapter = new ApprovalFormRecycleAdapter(this, mData);

        // 设置布局管理器
        recyclerView.setLayoutManager(bxLayoutManager);
        // 设置adapter
        recyclerView.setAdapter(bxAdapter);
        bxAdapter.setOnItemClickListener(new ApprovalFormRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                // Intent intent = new Intent(context,)
            }
        });
    }


    private void getExpenseForms(int formId) {
        new Thread(getFormRunnable).start();
    }

    private void submitApproval() {
        reason = editReason.getText().toString().trim();

        new Thread(approvalRunnable).start();

    }

    @Override
    protected void initListeners() {
    }

    @Override
    public void onClick(View view) {

    }


    /**
     * 创建静态内部类
     */
    private class MyHandler extends Handler {
        //持有弱引用HandlerActivity,GC回收时会被回收掉.
        private final WeakReference<ApprovalDetailActivity> mActivty;

        public MyHandler(ApprovalDetailActivity activity) {
            mActivty = new WeakReference<ApprovalDetailActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            ApprovalDetailActivity activity = mActivty.get();
            super.handleMessage(msg);

            if (activity != null) {
                // 审批－通过成功
                if (msg.what == 1) {
                    // DialogUtils.stopLoad();
                    ProgressDialogUtils.instance(context).dismiss();
                    Log.e("2-", "sub 1suc");
                    //ToastUtil.showToast(ApprovalDetailActivity.this, "审批成功！", Toast.LENGTH_LONG);
                    Toast.makeText(context, "审批成功！", Toast.LENGTH_SHORT).show();
                    Log.e("2-", "sub2 suc");
                    toFinish();
                } else if (msg.what == 3) {
                    //get form list success
                    List<ResponseApprovalDetailBean> beanList =
                            JSONArray.parseArray(jsonArrayResult.toJSONString(), ResponseApprovalDetailBean.class);
                    mData.clear();
                    mData.addAll(beanList);
                    // if()
                    for (int i = 0; i < mData.size(); i++) {
                        Log.e("ApproDetailAc i=", "" + i + mData.get(i).getExpenseItem());
                    }

                    bxAdapter.notifyDataSetChanged();

                } else if (msg.what == 4) {
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

                } else if (msg.what == 2) {
                    // 提交 签名后的 jsonStr
                    if (code == 200) {
                        Log.e("2-", "sign suc");
//                        ToastUtil.showToast(ApprovalDetailActivity.this, "验签成功！", Toast.LENGTH_LONG);
                        submitApproval();
                    } else {
                        ToastUtil.showToast(ApprovalDetailActivity.this, "提交验签失败，请重新！", Toast.LENGTH_LONG);
                    }
                } else if (msg.what == 5) {
                    if (annexId != null) {
                        //去下载pdf
                        toDownLoadPdf();
                    }

                } else if (msg.what == 0) {
                    ToastUtil.showToast(ApprovalDetailActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
                } else if (msg.what == -1) {
                    ToastUtil.showToast(ApprovalDetailActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
                }
            }
        }
    }

    private void toFinish() {
        this.finish();
    }


    private static JSONArray jsonArrayResult;
    private static final Runnable getFormRunnable = new Runnable() {


        @Override
        public void run() {
            //加载 审批的报销项 列表
            try {

                JSONArray jsonArray = SingleReimbursementUtil.selectbyformId(formId);
                if (jsonArray != null) {
                    jsonArrayResult = jsonArray;
                    myHandler.sendEmptyMessage(3);
                } else {
                    myHandler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                myHandler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }
    };

    private static final Runnable approvalRunnable = new Runnable() {


        @Override
        public void run() {
            //执行我们的业务逻辑
            try {

                JSONObject jo = ApprovalUtil.updateApproveNum(approvalId, approveResultId, reason, userId);
                if (jo != null) {
                    jsonResult = jo;
                    myHandler.sendEmptyMessage(1);
                } else {
                    myHandler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                myHandler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }
    };

    private JSONObject jsonobj;

    private String pdfPath = "/storage/emulated/0/Download/";
    private JSONObject pdfJsonObjec;
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


    private void toDownLoadPdf() {

        new Thread(DownLoadPdfRunnable).start();

    }


    private void signVerifyP1(final String base64Code1) {

        String plantext = base64Code1;
        String base64Code = base64Code1;

        ApprovalDetailActivity.this.getIntent().putExtra("data", plantext);
        ApprovalDetailActivity.this.getIntent().putExtra("type", DataProcessType.SIGNATURE_P1.name());
        SignatureP1Service signatureP1Service = new SignatureP1Service(ApprovalDetailActivity.this, new ProcessListener<DataProcessResponse>() {
            @Override
            public void doFinish(DataProcessResponse dataProcessResponse, String certificate) {
                if (dataProcessResponse.getRet() == 0) {
                    Log.e("密钥", "= " + dataProcessResponse.getResult());
                    spu.setCertKey(dataProcessResponse.getResult());
                    Log.e("cert", "= " + certificate);
                    spu.setCert(certificate);
//
                    //签完名字之后, 先上传签了名字的JsonString ，成功 则Glide 显示图片到界面，否则 提示出错

                    submitSignJsonstring();

                } else {
                    Toast.makeText(context, "图片签名失败" + dataProcessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void doException(CmSdkException e) {
                Toast.makeText(context, "图片签名失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitSignJsonstring() {
        new Thread(SubmitSignThread).start();
    }

    private int expenseId = 73;
    //上传签名JsonStr
    Runnable SubmitSignThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = SingleReimbursementUtil.signJsonStringPdf(
                        pdfBase64Str, spu.getCert(), spu.getKey(), filePath, 1, formId);

                // JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(expenseItem, expenseCategory, formId, amount, remark);
                if (jo != null) {
                    jsonobj = jo;
                    //  expenseId = jo.getIntValue("expenseId");
                    code = jo.getIntValue("code");
                    Log.e("code=", "=" + code);

                    //  new Thread(SubmitfileThread).start();
                    myHandler.sendEmptyMessage(2);
                } else {
                    myHandler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                myHandler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


    private Integer annexId;

    private void getPdfForm() {
        new Thread(GetPdfFormRunnable).start();
    }

    //得到需要生成pdf的 annexId
    Runnable GetPdfFormRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = FormUtil.getFormPdf(formId);
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

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_handler_leak);
//        MyHandler myHandler=new MyHandler(this);
//        //解决了内存泄漏,延迟5分钟后发送
//        myHandler.postDelayed(myRunnable, 1000 * 60 * 5);
//    }

}
