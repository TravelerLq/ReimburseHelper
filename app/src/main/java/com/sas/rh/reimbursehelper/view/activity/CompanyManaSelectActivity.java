package com.sas.rh.reimbursehelper.view.activity;

import android.sax.TextElementListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;

/**
 * Created by liqing on 18/4/3.
 */

public class CompanyManaSelectActivity extends BaseActivity {
    private TextView tvRegisterCompany;
    private TextView tvEditCompany;
    private TextView tvTitle;
    private ImageView ivBack;


    @Override
    protected int getLayoutId() {
        return R.layout.company_manage_select_activity;
    }

    @Override
    protected void initData() {
        tvRegisterCompany = (TextView) findViewById(R.id.tv_register_com);
        tvEditCompany = (TextView) findViewById(R.id.tv_edit_com);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle.setText("");

    }

    @Override
    protected void initListeners() {
        tvRegisterCompany.setOnClickListener(this);
        tvEditCompany.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_com:
                toActivity(baseContext, EnterpriseDetailActivity.class);
                break;
            case R.id.tv_edit_com:
                break;
            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }

    }
}
