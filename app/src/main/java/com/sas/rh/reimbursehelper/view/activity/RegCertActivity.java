package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.sas.rh.reimbursehelper.CertUtil.CertServiceUrl;
import com.sas.rh.reimbursehelper.R;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Map;

import cn.com.syan.jcee.common.sdk.utils.CertificateConverter;
import cn.com.syan.online.sdk.OnlineApplyResponse;
import cn.com.syan.online.sdk.OnlineClient;
import cn.com.syan.online.sdk.OnlineIssueResponse;
import cn.com.syan.online.sdk.OnlineResponse;
import cn.unitid.spark.cm.sdk.activity.WebViewActivity;
import cn.unitid.spark.cm.sdk.business.CertificateIssueService;
import cn.unitid.spark.cm.sdk.business.CertificateRegisterService;
import cn.unitid.spark.cm.sdk.business.CertificateRenewService;
import cn.unitid.spark.cm.sdk.business.VCodeGetService;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;

/**
 *演示注册签发
 */
public class RegCertActivity extends FragmentActivity {

    private EditText url;
    private EditText  telephoneText;
    private EditText issueId;
    private EditText algorithm;
    private EditText subject;

    private EditText signatureCert;

    private EditText encryptionCert;
    private EditText certValidate;

    private EditText realnameText;
    private EditText idnoText;

    private EditText vcodeText;

    private EditText extentionText;
    private android.support.v4.app.FragmentManager manager;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_cert);
        manager = this.getSupportFragmentManager();

        url=(EditText)findViewById(R.id.url);  //签发证书的sparkra服务
        telephoneText=(EditText)findViewById(R.id.telephone);  //名称
        issueId=(EditText)findViewById(R.id.issueId); //证书申请id
        algorithm=(EditText)findViewById(R.id.algorithm); //算法
        subject=(EditText)findViewById(R.id.subject);//主题

        realnameText=(EditText)findViewById(R.id.realname);  //姓名
        idnoText=(EditText)findViewById(R.id.idno);  //身份证号

        signatureCert=(EditText)findViewById(R.id.signatureCert);//签名证书
        encryptionCert=(EditText)findViewById(R.id.encryptionCert);//加密证书
        certValidate=(EditText)findViewById(R.id.certValidate);//有效期
        vcodeText=(EditText)findViewById(R.id.vcode);//验证码
        url.setText(CertServiceUrl.baseUrl);
        extentionText=(EditText)findViewById(R.id.extention);
        //注册证书
//        Button register=(Button)findViewById(R.id.register);
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegIssueActivity.this, WebViewActivity.class);
//                intent.putExtra("baseUrl", url.getText().toString());
//                intent.putExtra("name", name.getText().toString());
//                intent.putExtra("type", DataProcessType.REGISTER.name());
//                startActivityForResult(intent,0);
//
//            }
//        });


        //下面这行是注册接口参数的json字符串

        //接口注册
        final Button interfaceRegister=(Button)findViewById(R.id.interfaceRegister);
        interfaceRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnlineClient onlineClient=new OnlineClient(CertServiceUrl.baseUrl,CertServiceUrl.appKey,CertServiceUrl.appSecret);

                String telephone= telephoneText.getText().toString();
                if("".equals(telephone)){
                    Toast.makeText(RegCertActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    telephoneText.requestFocus();
                    return;
                }
                String vcode= vcodeText.getText().toString();
                if("".equals(vcode)){
                    Toast.makeText(RegCertActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
                    realnameText.requestFocus();

                    VCodeGetService vCodeGetService=new VCodeGetService(onlineClient,new ProcessListener<OnlineResponse>(){
                        @Override
                        public void doFinish(OnlineResponse onlineResponse, String s) {
                            if(onlineResponse.getRet()==0){
                                Toast.makeText(RegCertActivity.this, "短信已发送请注意查收", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegCertActivity.this, "短信发送失败", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void doException(CmSdkException e) {
                            e.printStackTrace();
                            Toast.makeText(RegCertActivity.this,""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    vCodeGetService.get(telephone);

                    return;
                }
                // 实名实名认证自动注册
                String realname=realnameText.getText().toString();
                String idno=idnoText.getText().toString();
                if("".equals(realname)){
                    Toast.makeText(RegCertActivity.this, "请输入姓名", Toast.LENGTH_SHORT).show();
                    realnameText.requestFocus();
                    return;
                }
                if("".equals(idno)){
                    Toast.makeText(RegCertActivity.this, "请输入身份证号", Toast.LENGTH_SHORT).show();
                    idnoText.requestFocus();
                    return;
                }



                CertificateRegisterService certificateRegisterService=new CertificateRegisterService(RegCertActivity.this,onlineClient, new ProcessListener<OnlineApplyResponse>() {
                    @Override
                    public void doFinish(OnlineApplyResponse onlineApplyResponse, String s) {
                        issueId.setText(onlineApplyResponse.getApplyId());
                        subject.setText(onlineApplyResponse.getSubject());
                        algorithm.setText(onlineApplyResponse.getAlgorithm());
                        extentionText.setText(onlineApplyResponse.getExtensions());
                    }

                    @Override
                    public void doException(CmSdkException e) {
                        Toast.makeText(RegCertActivity.this,""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                try {
                    certificateRegisterService.register(realname, idno, telephone, vcode, null);
                }catch (Exception e){
                    Toast.makeText(RegCertActivity.this,""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }


                /**
                 *  以下是根据指定templateId 注册证书
                 */
                /**
                 List<RegProperty> itemList=new ArrayList<RegProperty>();
                 //别名
                 RegProperty item=new RegProperty();
                 item.setType("text");//模板中指定
                 item.setLabel("别名");  //模板中指定
                 item.setName("cert_alias"); //模板中指定
                 item.setShortName("cert_alias");//模板中指定
                 item.setValue("测试证书7"); //自定义
                 itemList.add(item);
                 //姓名
                 item=new RegProperty();
                 item.setType("text");//模板中指定
                 item.setLabel("姓名");  //模板中指定
                 item.setName("a056d5a2717b3bd477fd1145897258d7"); //模板中指定
                 item.setValue("测试证书7"); //自定义
                 itemList.add(item);
                 //省
                 item=new RegProperty();
                 item.setType("text");//模板中指定
                 item.setLabel("省");  //模板中指定
                 item.setName("0e6b7630b61e483ae3db354318a18afd"); //模板中指定
                 item.setValue("js"); //自定义
                 itemList.add(item);
                 //市
                 item=new RegProperty();
                 item.setType("text");//模板中指定
                 item.setLabel("市");  //模板中指定
                 item.setName("9d8ca7a89933e1dd462093165ffedf24"); //模板中指定
                 item.setValue("南京"); //自定义
                 itemList.add(item);

                 item=new RegProperty();
                 item.setType("text");//模板中指定
                 item.setLabel("组织机构");  //模板中指定
                 item.setName("17eb9c06d77ed36e78cb0cc1d0f8a2f9"); //模板中指定
                 item.setValue("syan"); //自定义
                 itemList.add(item);

                 OnlineClient onlineClient=new OnlineClient(CertServiceUrl.baseUrl,CertServiceUrl.appKey,CertServiceUrl.appSecret);
                 String shecaSm2TemplateId=CertServiceUrl.templateId;
                 CertificateRegisterService certificateRegisterService=new CertificateRegisterService(onlineClient, new ProcessListener<OnlineApplyResponse>() {
                @Override
                public void doFinish(OnlineApplyResponse onlineApplyResponse, String s) {
                issueId.setText(onlineApplyResponse.getApplyId());
                subject.setText(onlineApplyResponse.getSubject());
                algorithm.setText(onlineApplyResponse.getAlgorithm());
                }

                @Override
                public void doException(CmSdkException e) {
                Toast.makeText(RegIssueActivity.this,""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                });
                 certificateRegisterService.register(shecaSm2TemplateId,itemList);
                 ***/

            }
        });

        //签发证书
        Button signCert=(Button)findViewById(R.id.signCert);
        signCert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnlineClient onlineClient=new OnlineClient(CertServiceUrl.baseUrl,CertServiceUrl.appKey,CertServiceUrl.appSecret);

                CertificateIssueService certificateIssueService=new CertificateIssueService(RegCertActivity.this,onlineClient, new ProcessListener<OnlineIssueResponse>() {
                    @Override
                    public void doFinish(OnlineIssueResponse data, String cert) {
                        String your_signature=data.getSignCert();
                        String your_encryption=data.getEncCert();
                        if(your_signature!=null && !"".equals(your_signature)){
                            signatureCert.setText(your_signature);
                        }

                        if(your_encryption!=null && !"".equals(your_encryption)) {
                            encryptionCert.setText(your_encryption);
                        }

                        X509Certificate x509Certificate= null;
                        try {
                            x509Certificate = CertificateConverter.fromBase64(your_signature);

                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                        String validate=formatter.format(x509Certificate.getNotBefore())+" - "+formatter.format(x509Certificate.getNotAfter());

                        certValidate.setText(validate);

                    }

                    @Override
                    public void doException(CmSdkException exception) {
                        Toast.makeText(RegCertActivity.this,""+ exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                String exts=extentionText.getText().toString();
                Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                Map<String, String> retMap = gson.fromJson(exts,new TypeToken<Map<String, String>>() {}.getType());

                certificateIssueService.issue(issueId.getText().toString(),subject.getText().toString(),retMap,algorithm.getText().toString(),true);
            }
        });


        Button delay=(Button)findViewById(R.id.delay);
        delay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnlineClient onlineClient=new OnlineClient(CertServiceUrl.baseUrl,CertServiceUrl.appKey,CertServiceUrl.appSecret);

                CertificateRenewService certificateRenewService=new CertificateRenewService(RegCertActivity.this,onlineClient, new ProcessListener<OnlineIssueResponse>() {
                    @Override
                    public void doFinish(OnlineIssueResponse data, String cert) {
                        String new_signature=data.getSignCert();
                        String new_encryption=data.getEncCert();
                        signatureCert.setText(new_signature);
                        encryptionCert.setText(new_encryption);

                        X509Certificate newX509Certificate= null;
                        try {
                            newX509Certificate = CertificateConverter.fromPEMString(new_signature);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }

                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        String newvalidate=format.format(newX509Certificate.getNotBefore())+" - "+format.format(newX509Certificate.getNotAfter());

                        certValidate.setText(newvalidate);
                    }
                    @Override
                    public void doException(CmSdkException exception) {
                        Intent intent = new Intent(RegCertActivity.this, WebViewActivity.class);
                        intent.putExtra("baseUrl", url.getText().toString());
                        intent.putExtra("certificate", signatureCert.getText().toString());
                        intent.putExtra("name", "延期证书");
                        intent.putExtra("type", DataProcessType.DELAY.name());
                        RegCertActivity.this.startActivityForResult(intent, 0);

                    }
                });
                certificateRenewService.renew(signatureCert.getText().toString());

            }
        });

    }


}
