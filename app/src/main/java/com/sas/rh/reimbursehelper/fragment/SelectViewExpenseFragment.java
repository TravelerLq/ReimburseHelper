package com.sas.rh.reimbursehelper.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.view.activity.TestApprovalDetailActivity;
import com.sas.rh.reimbursehelper.view.activity.ViewExpenseActivity;

/**
 * Created by liqing on 18/3/26.
 */

public class SelectViewExpenseFragment extends Fragment implements View.OnClickListener {
    private TextView tvViewExpense;
    private TextView tvViewApprovalExpense;
    private TextView tvTitle;
    private String type;
    private static final String TYPE = "type";
    private static final String USER_ID = "user_id";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expense_view, container, false);
        tvViewExpense = (TextView) view.findViewById(R.id.tv_view_expense);
        tvViewApprovalExpense = (TextView) view.findViewById(R.id.tv_expense_approval);
        tvTitle = (TextView) view.findViewById(R.id.tv_select_title);
        tvTitle.setText("查看报销单");
        tvViewExpense.setOnClickListener(this);
        tvViewApprovalExpense.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            //我的报销单列表
            case R.id.tv_view_expense:
                type = "0";
                break;
            //我的待审批列表
            case R.id.tv_expense_approval:
                type = "1";
                break;
            default:
                break;
        }
        //进入列表的Activity？
        Intent intent = new Intent(getActivity(), ViewExpenseActivity.class);
        //  Intent intent = new Intent(getActivity(), TestApprovalDetailActivity.class);
        intent.putExtra(TYPE, type);
        intent.putExtra(USER_ID, getId());
        startActivity(intent);


    }
}
