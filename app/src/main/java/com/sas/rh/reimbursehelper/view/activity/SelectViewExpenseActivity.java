package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.fragment.MessageFragment;
import com.sas.rh.reimbursehelper.R;

/**
 * Created by liqing on 18/3/26.
 * 选择查看 报销单／报销审批
 */

public class SelectViewExpenseActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvViewExpense;
    private TextView tvViewApprovalExpense;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_expense_view;
    }

    @Override
    protected void initData() {
        tvViewExpense = (TextView) findViewById(R.id.tv_view_expense);
        tvViewApprovalExpense = (TextView) findViewById(R.id.tv_expense_approval);

    }

    @Override
    protected void initListeners() {
        tvViewExpense.setOnClickListener(this);
        tvViewApprovalExpense.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //我的报销单列表
            case R.id.tv_view_expense:
                break;
            //我的待审批列表
            case R.id.tv_expense_approval:
                break;
            default:
                break;
        }
         //进入列表页面
        Intent intent =new Intent(this,MessageFragment.class);
        startActivity(intent);

    }
}
