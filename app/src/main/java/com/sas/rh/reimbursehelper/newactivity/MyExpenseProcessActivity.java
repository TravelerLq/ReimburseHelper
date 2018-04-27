package com.sas.rh.reimbursehelper.newactivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.fragment.myExpense.ApprovedFragment;
import com.sas.rh.reimbursehelper.fragment.myExpense.ApprovingFragment;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;

/**
 * Created by liqing on 18/4/27.
 * 我的报销
 */

public class MyExpenseProcessActivity extends BaseActivity {
    private TextView tvAppproving;
    private TextView tvApproved;
    private TextView tvTitle;
    private ImageView ivBack;
    private ApprovingFragment approvingFragment;
    private ApprovedFragment approvedFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_my_expense_process;
    }

    @Override
    protected void initData() {
        tvAppproving = (TextView) findViewById(R.id.tv_approving);
        tvApproved = (TextView) findViewById(R.id.tv_approved);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle.setText("我的报销");
        initApprovlingFragment();
    }

    @Override
    protected void initListeners() {
        tvAppproving.setOnClickListener(this);
        tvApproved.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_approving:
                initApprovlingFragment();
                break;
            case R.id.tv_approved:
                initApprovedFragment();
                break;

            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }


    private void initApprovlingFragment() {
        if (approvingFragment == null) {
            approvingFragment = new ApprovingFragment();
        }
        addFragmentNotToStack(R.id.fl_expense_process, approvingFragment);
        //tvStoreCombine.setBackground(getResources().getDrawable(R.drawable.bg_combine_select));
        tvAppproving.setBackgroundColor(getResources().getColor(R.color.blue));
        tvApproved.setBackgroundColor(getResources().getColor(R.color.white));
        tvAppproving.setTextColor(getResources().getColor(R.color.white));
        tvApproved.setTextColor(getResources().getColor(R.color.blue));
    }

    private void initApprovedFragment() {
        if (approvedFragment == null) {
            approvedFragment = new ApprovedFragment();
        }
        addFragmentNotToStack(R.id.fl_expense_process, approvedFragment);
        //tvStoreCombine.setBackground(getResources().getDrawable(R.drawable.bg_combine_select));
        tvApproved.setBackgroundColor(getResources().getColor(R.color.blue));
        tvAppproving.setBackgroundColor(getResources().getColor(R.color.white));
        tvApproved.setTextColor(getResources().getColor(R.color.white));
        tvAppproving.setTextColor(getResources().getColor(R.color.blue));
    }
}
