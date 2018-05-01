package com.sas.rh.reimbursehelper.newactivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.UserUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;

/**
 * Created by liqing on 18/5/1.
 */

public class NewRegisterActivity extends BaseActivity {
    private TextView tvTilte,tvRegister;
    private ImageView ivBack;
    private EditText edtAccount, edtPsw;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_register;
    }

    @Override
    protected void initData() {
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        edtAccount = (EditText) findViewById(R.id.edt_account);
        edtPsw = (EditText) findViewById(R.id.edt_psw);
        tvRegister=(TextView) findViewById(R.id.tv_regist);
        tvTilte.setText("注册");
    }

    @Override
    protected void initListeners() {
        ivBack.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_regist:
               checkdata();
                break;
            default:
                break;
        }
    }

    //注册
    private void checkdata() {
        String account =edtAccount.getText().toString().trim();
        String psw=edtPsw.getText().toString().trim();
        if (TextUtils.isEmpty(account)) {
            Toast.makeText(this, "账号不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, "密码不可为空", Toast.LENGTH_SHORT).show();
            return;
        }

        goRegister();
    }



    private void goRegister() {
    }

//    Runnable RegisterRunnable = new Runnable() {
//        @Override
//        public void run() {
//
//            try {
//
//                JSONObject jo = UserUtil.addUser(spu.getUidNum(), 1, 1);
//                if (jo != null) {
//                    jsonRemark = jo;
//
//                    handler.sendEmptyMessage(6);
//                } else {
//                    handler.sendEmptyMessage(0);
//                }
//            } catch (Exception e) {
//                handler.sendEmptyMessage(-1);
//                e.printStackTrace();
//            }
//        }
//    };

}
