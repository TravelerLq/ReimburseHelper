package com.sas.rh.reimbursehelper.view.activity;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;

/**
 * Created by liqing on 18/4/17.
 */

public class JoinOrRegisterCompanyActivity extends BaseActivity {

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

    @Override
    protected int getLayoutId() {
        return R.layout.activity_join_register_company;
    }

    @Override
    protected void initData() {
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvSelectCompany = (TextView) findViewById(R.id.tv_company_join);
        tvRegisterCompany = (TextView) findViewById(R.id.tv_company_register);
        //  btnSure = (Button) findViewById(R.id.btn_sure);
        tvTitle.setText("加入公司");

    }

    @Override
    protected void initListeners() {
        ivBack.setOnClickListener(this);
        tvSelectCompany.setOnClickListener(this);
        tvRegisterCompany.setOnClickListener(this);
        //   btnSure.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_company_join:
                //去填写邀请码
                toActivity(JoinOrRegisterCompanyActivity.this, BeforeRegisterActivity.class);
                break;

            case R.id.tv_company_register:
                toActivity(JoinOrRegisterCompanyActivity.this, EnterpriseDetailActivity.class);
                Loger.e("-----take");

                break;

            default:
                break;
        }

    }
}
