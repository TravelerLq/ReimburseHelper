package com.sas.rh.reimbursehelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.view.activity.AddExpenseActivity;

import butterknife.InjectView;


/**
 * Created by liqing on 18/4/23.
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.tv_bar_title)
    TextView tvTitle;
    @InjectView(R.id.iv_add_expense)
    ImageView ivAddEpense;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        ivAddEpense.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        tvTitle.setText("报销");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_expense:
                Loger.e("----ivAdd--click");
                Intent it = new Intent(getActivity(), AddExpenseActivity.class);
                startActivity(it);
                break;
            default:
                break;
        }

    }
}
