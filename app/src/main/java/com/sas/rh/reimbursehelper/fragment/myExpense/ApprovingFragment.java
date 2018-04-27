package com.sas.rh.reimbursehelper.fragment.myExpense;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.sas.rh.reimbursehelper.Adapter.newadapter.ApprovalRecycleAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.newbean.ApprovalBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ApprovalUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseCategoryUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.fragment.BaseFragment;
import com.sas.rh.reimbursehelper.newactivity.ApprovalProcessDetailActvity;
import com.sas.rh.reimbursehelper.newactivity.ExpenseProcessDetailActvity;
import com.sas.rh.reimbursehelper.newactivity.ExpenseProcessRecyActvity;
import com.sas.rh.reimbursehelper.view.activity.AddBaoxiaojizhuActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqing on 18/4/27.
 */

public class ApprovingFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private ApprovalRecycleAdapter adapter;
    private List<ApprovalBean> beanList = new ArrayList<>();
    private Context context;
    private SharedPreferencesUtil spu;
    private JSONArray jsonresult;

    private ProgressDialogUtil pdu = new ProgressDialogUtil(ApprovingFragment.this.getActivity(),
            "提示", "正在加载...");

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
//                if (pdu.getMypDialog().isShowing()) {
//                    pdu.dismisspd();
//                }
                List<ApprovalBean> list = JSONArray.parseArray(jsonresult.toJSONString(), ApprovalBean.class);
                if (list.size() == 0) {
                    ToastUtil.showToast(context, "暂无数据", Toast.LENGTH_LONG);
                }
                beanList.clear();
                beanList.addAll(list);
                adapter.notifyDataSetChanged();


            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_approving;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {
        context = ApprovingFragment.this.getActivity();
        spu = new SharedPreferencesUtil(context);

        recyclerView = (RecyclerView) self.findViewById(R.id.rl_approving);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        initTestData();
        adapter = new ApprovalRecycleAdapter(context, beanList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ApprovalRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                String approvalId = String.valueOf(beanList.get(pos).getApprovalId());
                toActivityWithData(context, ExpenseProcessDetailActvity.class, "approvalId", approvalId);

            }
        });

        getMyExpenseApproved();

    }

    private void getMyExpenseApproved() {
//        pdu.showpd();
        new Thread(GetApprovedRunnable).start();
    }


    Runnable GetApprovedRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = ApprovalUtil.getMyExpenseApproving(spu.getUidNum());
                if (jo != null) {
                    jsonresult = jo;
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }


    // 初始化显示的数据
    public void initTestData() {
        for (int i = 0; i < 10; i++) {
            ApprovalBean bean = new ApprovalBean();
            bean.setDate("2018-01-07");
            bean.setMoney("200." + i);
//            bean.setFormId(i);
//            bean.setApprovalName("remark" + i);
            bean.setUserName("张三");
            bean.setProcess("财务部审批中");
            bean.setCount(1);
            beanList.add(i, bean);
        }


    }

}
