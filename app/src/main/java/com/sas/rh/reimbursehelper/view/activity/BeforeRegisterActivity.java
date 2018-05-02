package com.sas.rh.reimbursehelper.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.CompanyUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.newactivity.*;
import com.sas.rh.reimbursehelper.newactivity.RegCertActivity;

/**
 * Created by liqing on 18/4/17.
 * 填写验证码Activity
 */

public class BeforeRegisterActivity extends BaseActivity {

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRegister;
    private Dialog dialog;
    private View inflate;

    private TextView cancel;
    private TextView tvSelectCompany;
    private TextView tvRegisterCompany;
    private EditText edtCode;
    private Button btnSure;
    private int userId;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private String code;
    private JSONObject jsonresult;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                int status = jsonresult.getIntValue("status");
                if (status == 200) {
                    int userId = jsonresult.getIntValue("userId");
                    ToastUtil.showToast(BeforeRegisterActivity.this, "加入公司成功！", Toast.LENGTH_SHORT);
                    sharedPreferencesUtil.writeUserId(String.valueOf(userId));
                    int usId = sharedPreferencesUtil.getUidNum();
                    Loger.e("---userid=" + usId);
                    // toActivity(RegCertActivity.this,MainActivity.class);
                    Intent intent = new Intent(BeforeRegisterActivity.this, RegCertActivity.class);
                    // intent.putExtra("id",)
                    startActivity(intent);
                    finish();
                } else {
                    ToastUtil.showToast(BeforeRegisterActivity.this, "失败，请重试！", Toast.LENGTH_SHORT);
                }

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_before_register;
    }

    @Override
    protected void initData() {
        sharedPreferencesUtil = new SharedPreferencesUtil(BeforeRegisterActivity.this);
     //   userId = sharedPreferencesUtil.getUidNum();

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvRegister = (TextView) findViewById(R.id.tv_before_register);
        edtCode = (EditText) findViewById(R.id.edt_verify_code);
        btnSure = (Button) findViewById(R.id.btn_sure);
        tvTitle.setText("加入公司");
        initTestData();

    }

    private void initTestData() {
        edtCode.setText("welq5n4ly");
    }

    @Override
    protected void initListeners() {
        ivBack.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        btnSure.setOnClickListener(this);


    }

    public void showDialog() {
        dialog = new Dialog(this, R.style.BottomDialog);
        inflate = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        tvRegisterCompany = (TextView) inflate.findViewById(R.id.tv_register_company);
        tvSelectCompany = (TextView) inflate.findViewById(R.id.tv_select_company);
        cancel = (TextView) inflate.findViewById(R.id.btn_cancel);
        tvRegisterCompany.setOnClickListener(this);
        tvSelectCompany.setOnClickListener(this);
        cancel.setOnClickListener(this);
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        // WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //获得window窗口的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        //设置窗口宽度为充满全屏
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //设置窗口高度为包裹内容
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //将设置好的属性set回去
        dialogWindow.setAttributes(lp);
        //将自定义布局加载到dialog上
        dialog.setContentView(inflate);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_before_register:
                showDialog();
                break;

            case R.id.tv_select_company:
                Loger.e("-----take");

                dialog.dismiss();
                break;
            case R.id.tv_register_company:
                //从相册选择照片，需要根据当前照片个数，去限制limit 最大可选择图片个数
                Loger.e("-----select");
                //takePicFromPhoto();
                // toActivity(MultiSelectActivity.class);

                dialog.dismiss();
                break;
            case R.id.btn_sure:
                checkData();

                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            default:
                break;
        }

    }

    private void checkData() {
//        if (userId == -1) {
//            ToastUtil.showToast(BeforeRegisterActivity.this, "userId为空", Toast.LENGTH_SHORT);
//        }
        code = edtCode.getText().toString();

        if (TextUtils.isEmpty(code)) {
            ToastUtil.showToast(BeforeRegisterActivity.this, "请输入公司邀请码", Toast.LENGTH_SHORT);
            edtCode.requestFocus();
        }

        joinCompany();

    }

    private void joinCompany() {
        new Thread(RunnableJoinCompany).start();
    }


    Runnable RunnableJoinCompany = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject jsonObject = CompanyUtil.sendShareCode(code);
                if (jsonObject != null) {
                    jsonresult = jsonObject;
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

}
