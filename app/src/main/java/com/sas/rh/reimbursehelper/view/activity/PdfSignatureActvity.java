package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sas.rh.reimbursehelper.CertUtil.CertServiceUrl;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Map;

import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.com.syan.online.sdk.OnlineApplyResponse;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.unitid.spark.cm.sdk.business.CertificateIssueService;
import cn.unitid.spark.cm.sdk.business.CertificateRegisterService;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import me.iwf.photopicker.PhotoPicker;

/**
 * Created by liqing on 18/3/22.
 * pdf签证
 */

public class PdfSignatureActvity extends AppCompatActivity {
    private Button btnSig;
    private OnlineClient onlineClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_layout);
        btnSig = (Button) findViewById(R.id.btn_sig);
        onlineClient = new OnlineClient(CertServiceUrl.baseUrl, CertServiceUrl.appKey, CertServiceUrl.appSecret);

//        btnSig.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CertificateRegisterService certificateRegisterService =
//                        new CertificateRegisterService(PdfSignatureActvity.this, onlineClient, new ProcessListener<OnlineApplyResponse>() {
//                            @Override
//                            public void doFinish(OnlineApplyResponse onlineApplyResponse, String s) {
////                        issueId.setText(onlineApplyResponse.getApplyId());
////                        subject.setText(onlineApplyResponse.getSubject());
////                        algorithm.setText(onlineApplyResponse.getAlgorithm());
////                        extentionText.setText(onlineApplyResponse.getExtensions());
//                                //接口注册成功后执行
//                                CertificateIssueService certificateIssueService = new CertificateIssueService(PdfSignatureActvity.this, onlineClient, new ProcessListener<OnlineIssueResponse>() {
//                                    @Override
//                                    public void doFinish(OnlineIssueResponse data, String cert) {
//                                        Log.e("CertificateIssueService", "onFinish");
//                                        String your_signature = data.getSignCert();
//                                        String your_encryption = data.getEncCert();
//
//                                        X509Certificate x509Certificate = null;
//                                        try {
//                                            x509Certificate = CertificateConverter.fromBase64(your_signature);
//
//                                        } catch (CertificateException e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//                                        String validate = formatter.format(x509Certificate.getNotBefore()) + " - " + formatter.format(x509Certificate.getNotAfter());
//
//                                        //certValidate.setText(validate);
//                                        Intent it = new Intent(PdfSignatureActvity.this, SignVerifyP1Activity.class);
//                                        startActivity(it);
//                                        finish();
//
//                                    }
//
//                                    @Override
//                                    public void doException(CmSdkException exception) {
//                                        Log.e("CertificateIssueService", "doException");
//                                        Toast.makeText(PdfSignatureActvity.this, "" + exception.getMessage(), Toast.LENGTH_SHORT).show();
//                                        Log.e("firstRegister", "showException" + exception.getMessage());
//                                    }
//                                });
//                                String exts = onlineApplyResponse.getExtensions();
//                                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
//                                Map<String, String> retMap = gson.fromJson(exts, new TypeToken<Map<String, String>>() {
//                                }.getType());
//
//                                certificateIssueService.issue(onlineApplyResponse.getApplyId(), onlineApplyResponse.getSubject(), retMap, onlineApplyResponse.getAlgorithm(), true);
//
//                            }
//
//                            @Override
//                            public void doException(CmSdkException e) {
//                                ToastUtil.showToast(PdfSignatureActvity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
//                                Log.e("firstRegister", "CmSdkException" + e.getMessage());
//                            }
//                        });
//                try {
//                    // certificateRegisterService.register(realname, idno, telephone, vcode, null);
//                } catch (Exception e) {
//                    ToastUtil.showToast(PdfSignatureActvity.this, "" + e.getMessage(), Toast.LENGTH_SHORT);
//                    e.printStackTrace();
//                }
//            }
//        });
        btnSig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
