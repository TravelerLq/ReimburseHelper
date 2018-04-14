package com.sas.rh.reimbursehelper.view.activity;

import android.view.View;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;

/**
 * Created by liqing on 18/4/3.
 */

public class CompanyManaSelectActivity extends BaseActivity {
    private TextView tvRegisterCompany;
    private TextView tvEditCompany;


    @Override
    protected int getLayoutId() {
        return R.layout.company_manage_select_activity;
    }

    @Override
    protected void initData() {
        tvRegisterCompany = (TextView) findViewById(R.id.tv_register_com);
        tvEditCompany = (TextView) findViewById(R.id.tv_edit_com);

    }

    @Override
    protected void initListeners() {
        tvRegisterCompany.setOnClickListener(this);
        tvEditCompany.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_com:
                toActivity(baseContext, EnterpriseDetailActivity.class);
                break;
            case R.id.tv_edit_com:
                break;
            default:
                break;
        }

    }
}
