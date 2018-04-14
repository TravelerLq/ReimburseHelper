package com.sas.rh.reimbursehelper.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.ExpenseApprovalResponseBean;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.DateUtils;

import java.util.Date;

/**
 * Created by liqing on 18/3/27.
 * 查看报销单 详情
 */

public class ExpenseDetailActivity extends BaseActivity {

    private TextView tvTitle;
    private TextView tvExpenseName;
    private TextView tvProgress;
    private TextView tvApprovalResult;
    private TextView tvFinalResult;
    private TextView tvUpdateTime;
    private TextView tvReason;
    private LinearLayout llReject;
    private static ExpenseApprovalResponseBean itemBean;
    private static String reason;
    private static int approvalId;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_expense_detail;
    }

    @Override
    protected void initData() {
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvApprovalResult = (TextView) findViewById(R.id.tv_approval_result);
        tvFinalResult = (TextView) findViewById(R.id.tv_final_result);
        tvUpdateTime = (TextView) findViewById(R.id.tv_update_time);
        tvExpenseName = (TextView) findViewById(R.id.tv_expense_name);
        llReject = (LinearLayout) findViewById(R.id.ll_reject_reason);
        tvReason = (TextView) findViewById(R.id.tv_reason);
        tvTitle.setText("报销单详情");
        Bundle bundle = (Bundle) getIntent().getExtras().get("bundle");
        itemBean = (ExpenseApprovalResponseBean) bundle.getSerializable("itemBean");
        if (itemBean == null) {
            Log.e("itemBean", "--intent =null");
        } else {
            //=1 通过
            if (itemBean.getApproveResultId() == 1) {
                tvApprovalResult.setText(getResources().getString(R.string.pass));
                llReject.setVisibility(View.GONE);
            } else {
                tvApprovalResult.setText(getResources().getString(R.string.unpass));
                llReject.setVisibility(View.VISIBLE);
                tvReason.setText(itemBean.getRejectReason());

            }
            Log.e("name-", "-getApprovalName" + itemBean.getApprovalName());
            if (!TextUtils.isEmpty(itemBean.getApprovalName())) {
                tvExpenseName.setText(itemBean.getApprovalName().toString());
            }


            tvProgress.setText(String.valueOf(itemBean.getApproveProcessId()));
            tvFinalResult.setText(String.valueOf(itemBean.getFinallyResultId()));
//            String time = String.valueOf(itemBean.getUpdateTime());

            Date date = itemBean.getUpdateTime();
            String dateStr = DateUtils.parse(date);

            //  Log.e("", "time=" + DateUtils.parse(time));

            tvUpdateTime.setText(dateStr);

        }
        Log.e("approvalId", "--" + approvalId);

    }


    @Override
    protected void initListeners() {
    }


    @Override
    public void onClick(View view) {

    }
}
