package com.sas.rh.reimbursehelper.newactivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.BeforeRegisterActivity;
import com.sas.rh.reimbursehelper.view.activity.EnterpriseDetailActivity;

/**
 * Created by liqing on 18/5/1.
 */

public class LoginOrRegisterActivity extends BaseActivity {

    private TextView tvLogin;
    private TextView tvRegister;

    private Dialog dialog;
    private View inflate;
    private TextView cancel;
    private TextView tvSelectCompany;
    private TextView tvRegisterCompany;

    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_or_register;
    }

    @Override
    protected void initData() {
        context = LoginOrRegisterActivity.this;
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvRegister = (TextView) findViewById(R.id.tv_register);
    }

    @Override
    protected void initListeners() {
        tvLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register:
                showDialog();
                break;
            case R.id.tv_login:
                break;
            case R.id.tv_select_company:
                Loger.e("-----take");
                toActivity(context, BeforeRegisterActivity.class);
                dialog.dismiss();
                break;
            case R.id.tv_register_company:
                //从相册选择照片，需要根据当前照片个数，去限制limit 最大可选择图片个数
                Loger.e("-----select");
                //takePicFromPhoto();
                toActivity(context, EnterpriseDetailActivity.class);

                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }


    //


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
}
