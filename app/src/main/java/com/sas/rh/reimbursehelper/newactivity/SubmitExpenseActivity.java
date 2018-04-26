package com.sas.rh.reimbursehelper.newactivity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.DownloadFileUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.FileUtils;
import com.sas.rh.reimbursehelper.Util.IntentUtils;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.fragment.HomeFragment;
import com.sas.rh.reimbursehelper.view.activity.AddExpenseActivity;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.MainActivity;
import com.sas.rh.reimbursehelper.widget.CountEditText;

import java.io.File;

import butterknife.InjectView;
import cn.unitid.spark.cm.sdk.business.SignatureP1Service;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 * Created by liqing on 18/4/25.
 * 查看以及生成pdf报销单的逻辑
 * 以及Base64 pdf验签逻辑
 */

public class SubmitExpenseActivity extends BaseActivity {

    ImageView ivPdf;

    TextView tvSubmit;

    private Integer annexId;
    private JSONObject pdfJsonObjec;
    private JSONObject jsonobj;
    private Integer formId;
    private int index = 1;
    private SharedPreferencesUtil spu;
    private String pdfBase64Str;
    private String base64Code;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(SubmitExpenseActivity.this, "提示", "正在加载中");
    private ProgressDialogUtil pduPdf = new ProgressDialogUtil(SubmitExpenseActivity.this, "提示", "正在生成，请稍后...");

    private String pdfPath;
    private File file;
    private String pdfPathName;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                if (annexId != null) {
                    //去下载pdf
                    int status = jsonobj.getIntValue("status");
                    if (status == 200) {
                        toDownLoadPdf();
                    } else {
                        if (pduPdf.getMypDialog().isShowing()) {
                            pduPdf.dismisspd();
                        }
                        // getPdfForm();
                        Toast.makeText(SubmitExpenseActivity.this, "获取生成pdf出错", Toast.LENGTH_SHORT).show();
                    }

                }

            } else if (msg.what == 2) {
                //download finish

                if (pduPdf.getMypDialog().isShowing()) {
                    pduPdf.dismisspd();
                }
                pdfBase64Str = pdfJsonObjec.getString("file");

                if (!TextUtils.isEmpty(pdfBase64Str)) {

                    signVerifyP1(pdfBase64Str);
                }
                String originalFilename = pdfJsonObjec.getString("originalFilename");
                pdfPath = FileUtils.getExternalFilesDirPath(SubmitExpenseActivity.this);

//                final File file = new File(FileUtils.getExternalFilesDirPath(context),
//                        "reim/" + FileUtils.getFileName(url));
                String filePath = pdfPath + "/" + originalFilename;
                Loger.e("filePath-------" + filePath);
                file = DownloadFileUtil.base64StringToPdf(pdfBase64Str, filePath);
                if (file == null) {
                    Loger.e("-----file------null");
                }
                //  startActivity(IntentUtils.getPdfFileIntent(file, AddExpenseActivity.this));

                if (file.exists() && file.length() > 0) {
                    Loger.e("-----file exist");
                    ivPdf.setVisibility(View.VISIBLE);
                    //是否有pdf生成的标志


                } else {
                    ivPdf.setVisibility(View.GONE);
                    Toast.makeText(SubmitExpenseActivity.this, "生成pdf出错", Toast.LENGTH_SHORT).show();
                }
                pdfPathName = file.getPath();

                //   startActivity(IntentUtils.getPdfFileIntent(file,AddExpenseActivity.this));
                // startActivity(IntentUtils.getPdfIntent(file));
            } else if (msg.what == 3) {
                //上传 签名成功
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }

                int code = jsonobj.getIntValue("status");
                if (code == 200) {
                    ToastUtil.showToast(SubmitExpenseActivity.this, "提交成功！", Toast.LENGTH_LONG);
                    toActivity(SubmitExpenseActivity.this, MainActivity.class);
                    finish();

                } else {
                    ToastUtil.showToast(SubmitExpenseActivity.this, "文件提交失败，请重试！", Toast.LENGTH_LONG);
                }
            }
        }
    };
    private String pdfPath1;
    private File file1;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_submit_expense;
    }

    @Override
    protected void initData() {
        spu = new SharedPreferencesUtil(SubmitExpenseActivity.this);

        pdfPath = spu.getPdfFile();

        //  pdfPath = FileUtils.getExternalFilesDirPath(SubmitExpenseActivity.this);
        ivPdf = (ImageView) findViewById(R.id.iv_pdf);
        tvSubmit = (TextView) findViewById(R.id.tv_expense_submit);
        formId = spu.getFormId();
        file = new File(pdfPath);
        if (file.exists() && file.length() > 0) {
            viewPdf();
            ivPdf.setVisibility(View.VISIBLE);
        }
        if (getIntent() != null) {
            pdfBase64Str = getIntent().getStringExtra("data");
        } else {
            Loger.e("getIntent--null-");
        }

        if (formId != -1) {
            //  getPdfForm();
        } else {
            ToastUtil.showToast(SubmitExpenseActivity.this, "formid＝－1", Toast.LENGTH_SHORT);
        }


    }

    private void getPdfForm() {
        pduPdf.showpd();
        new Thread(GetPdfFormRunnable).start();
    }

    private void toDownLoadPdf() {

        new Thread(DownLoadPdfRunnable).start();

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
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

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
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
//

    //签名验证
    private void signVerifyP1(final String base64Code1) {
        pdu.showpd();

        String plantext = base64Code1;
        base64Code = base64Code1;

        SubmitExpenseActivity.this.getIntent().putExtra("data", plantext);
        SubmitExpenseActivity.this.getIntent().putExtra("type", DataProcessType.SIGNATURE_P1.name());
        SignatureP1Service signatureP1Service = new SignatureP1Service(SubmitExpenseActivity.this, new ProcessListener<DataProcessResponse>() {
            @Override
            public void doFinish(DataProcessResponse dataProcessResponse, String certificate) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                if (dataProcessResponse.getRet() == 0) {
                    Log.e("密钥", "= " + dataProcessResponse.getResult());
                    spu.setCertKey(dataProcessResponse.getResult());
                    //获得就是签名证书
                    Log.e("cert", "= " + certificate);
                    spu.setCert(certificate);

                    submitSignJsonstring();
                } else {
                    if (pdu.getMypDialog().isShowing()) {
                        pdu.dismisspd();
                    }
                    Toast.makeText(SubmitExpenseActivity.this, "文件签名失败" + dataProcessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void doException(CmSdkException e) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                Toast.makeText(SubmitExpenseActivity.this, "文件签名失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitSignJsonstring() {
        pdu.showpd();
        new Thread(SubmitSignThread).start();
    }

    private String signPath;
    private int expenseId;
    //上传签名JsonStr
    Runnable SubmitSignThread = new Runnable() {
        @Override
        public void run() {

            base64Code = pdfBase64Str;
            signPath = file.getPath();

            Log.e("signPath--", "--" + signPath);
            try {

                JSONObject jo = SingleReimbursementUtil.signJsonStrPdf(base64Code, spu.getCert(), spu.getKey(), signPath, index, formId);
                if (jo != null) {
                    jsonobj = jo;
                    handler.sendEmptyMessage(3);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    @Override
    protected void initListeners() {
        ivPdf.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_pdf:
                //查看pdf
                viewPdf();
                break;
            case R.id.tv_expense_submit:
                //签名pdf
                if (ivPdf.getVisibility() == View.VISIBLE) {
                    signVerifyP1(pdfBase64Str);
                } else {
                    ToastUtil.showToast(SubmitExpenseActivity.this, "报销单生成失败，无法提交!", Toast.LENGTH_SHORT);
                }
                break;
            default:
                break;
        }
    }

    private void viewPdf() {
        startActivity(IntentUtils.getPdfFileIntent(file, SubmitExpenseActivity.this));
    }
}
