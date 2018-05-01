package com.sas.rh.reimbursehelper.newactivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;

/**
 * Created by liqing on 18/5/1.
 */

public class NewLoginActivity extends BaseActivity {
    private TextView tvTilte, tvLogin, tvForgetPsw;
    private ImageView ivBack;
    private EditText edtAccount, edtPsw;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_login;
    }

    @Override
    protected void initData() {
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        edtAccount = (EditText) findViewById(R.id.edt_account);
        edtPsw = (EditText) findViewById(R.id.edt_psw);
        tvForgetPsw = (TextView) findViewById(R.id.tv_forget_psw);
        tvLogin = (TextView) findViewById(R.id.tv_login);

        tvTilte.setText("登录");
    }

    @Override
    protected void initListeners() {
        tvLogin.setOnClickListener(this);
        tvForgetPsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_login:
                checkdata();
                break;
            case R.id.tv_forget_psw:

                break;
            default:
                break;
        }
    }


    //注册
    private void checkdata() {
        String account = edtAccount.getText().toString().trim();
        String psw = edtPsw.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "账号不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT).show();
            return;
        }

        goLogin();
    }

    private void goLogin() {
    }

}
