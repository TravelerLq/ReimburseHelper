package com.sas.rh.reimbursehelper.newactivity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Adapter.newadapter.AddExpenseRecycleAdapter;
import com.sas.rh.reimbursehelper.Bean.newbean.ExpenseItemBean;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.RecycleViewDivider;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqing on 18/4/24.
 */

public class ExpenseItemListActivity extends BaseActivity {
    private TextView tvTitle, tvSubmit;
    private ImageView ivBack, ivAdd;

    private RecyclerView recyclerView;
    private AddExpenseRecycleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private List<ExpenseItemBean> expenseItemBeanList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_expense_list;
    }

    @Override
    protected void initData() {
        context = ExpenseItemListActivity.this;
        initTestData();
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvTitle.setText("提交报销");
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setVisibility(View.GONE);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.VISIBLE);
        tvSubmit = (TextView) findViewById(R.id.tv_expense_submit);
        recyclerView = (RecyclerView) findViewById(R.id.rl_expense);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL,
//                1, getResources().getColor(R.color.divider)));
        adapter = new AddExpenseRecycleAdapter(context, expenseItemBeanList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AddExpenseRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Loger.e("item clicked pos==" + pos);
            }
        });
    }

    private void initTestData() {
        for (int i = 0; i < 10; i++) {
            ExpenseItemBean bean = new ExpenseItemBean();
            bean.setDate("2018-01-07");
            bean.setFee("200." + i);
            bean.setId(i);
            bean.setRemark("remark" + i);
            bean.setType("打车费");
            expenseItemBeanList.add(i, bean);
        }
    }

    @Override
    protected void initListeners() {
        ivAdd.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                //
                toActivity(context, AddExpenseItemActivtity.class);
                break;
            case R.id.tv_expense_submit:
                //
                finish();
                break;
            default:
                break;
        }
    }
}
