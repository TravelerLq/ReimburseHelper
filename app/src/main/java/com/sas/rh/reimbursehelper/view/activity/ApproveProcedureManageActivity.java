package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Adapter.ApprovalFormRecycleAdapter;
import com.sas.rh.reimbursehelper.Adapter.ApprovalProducerAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ApproveNum;
import com.sas.rh.reimbursehelper.NetworkUtil.ApproveNumUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.TimePickerUtils;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApproveProcedureManageActivity extends BaseActivity {
    private static final int REQUEST_PERSON_CODE = 111;

    private List<ApproveNum> list;
    private RecyclerView recyclerView;
    private ImageView ivAdd;
    private ImageView ivBack;
    private TextView tvTitle;
    private int userId;
    private JSONArray jsonobj;
    private int approvalId;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private ApprovalProducerAdapter approvalProducerAdapter;
    private JSONArray jsonArray;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                List<ApproveNum> approveNumList = JSONArray.parseArray(jsonArray.toJSONString(), ApproveNum.class);
                list.clear();
                list.addAll(approveNumList);
                approvalProducerAdapter.notifyDataSetChanged();
            } else if (msg.what == 0) {
                ToastUtil.showToast(baseContext, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(baseContext, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }
    };
    private LinearLayoutManager bxLayoutManager;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_manager_approval;
    }

    @Override
    protected void initData() {
        recyclerView = (RecyclerView) findViewById(R.id.list_producer);
        list = new ArrayList<>();
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.VISIBLE);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvTitle.setText("审批序号列表");
        sharedPreferencesUtil = new SharedPreferencesUtil(ApproveProcedureManageActivity.this);
        userId = sharedPreferencesUtil.getUidNum();
        Loger.e("companyId--" + userId);

        bxLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        approvalProducerAdapter = new ApprovalProducerAdapter(list);

        // 设置布局管理器
        recyclerView.setLayoutManager(bxLayoutManager);
        // 设置adapter
        recyclerView.setAdapter(approvalProducerAdapter);
    }

    @Override
    protected void initListeners() {
        ivAdd.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                toActivity(baseContext, ApproveProcedureAddActivity.class);
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataList();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getDataList() {
        new Thread(approvalProduceRunnable).start();
    }


    Runnable approvalProduceRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                JSONArray jo = ApproveNumUtil.selectAllApproveNum(userId);
                if (jo != null) {
                    jsonArray = jo;
                    //  expenseId = jo.getIntValue("expenseId");

                    //  new Thread(SubmitfileThread).start();
                    myHandler.sendEmptyMessage(1);
                } else {
                    myHandler.sendEmptyMessage(0);
                }

            } catch (Exception e) {
                myHandler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }


    };
}
