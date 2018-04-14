package com.sas.rh.reimbursehelper.view.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Adapter.ApplicantAdapter;
import com.sas.rh.reimbursehelper.Adapter.ApplicantRecyclerViewAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.UnreadNoticeBean;
import com.sas.rh.reimbursehelper.NetworkUtil.CompanyUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqing on 18/4/7.
 * 申请加入公司 列表
 */

public class ApplicantActivity extends BaseActivity {


    private TextView tvTitle;
    private RecyclerView rlApplicant;
    private int userId;
    private int result;
    private int noticeId;
    private ImageView ivBack;
    // private ApplicantAdapter applicantAdapter;
    private ApplicantRecyclerViewAdapter applicantAdapter;
    private List<UnreadNoticeBean> listData = new ArrayList<>();
    private LinearLayoutManager bxLayoutManager;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (jsonObject.getIntValue("status") == 200) {
                    ToastUtil.showToast(baseContext, " 操作成功！", Toast.LENGTH_LONG);
                    finish();
                }

            } else if (msg.what == 2) {
                List<UnreadNoticeBean> list = JSONArray.parseArray(
                        jsonArray.toJSONString(), UnreadNoticeBean.class);
                for (int i = 0; i < list.size(); i++) {
                    Loger.e("list.title=" + list.get(i).getTitle());
                }

                listData.clear();
                listData.addAll(list);
                for (int i = 0; i < listData.size(); i++) {
                    Loger.e("listdata.title=" + listData.get(i).getTitle());
                }
//                applicantAdapter.SetList(listData);

                applicantAdapter.notifyDataSetChanged();
            } else if (msg.what == 0) {
                ToastUtil.showToast(baseContext, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(baseContext, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }
    };
    private JSONArray jsonArray;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_applicant;
    }

    @Override
    protected void initData() {
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvTitle.setText("申请消息");
        sharedPreferencesUtil = new SharedPreferencesUtil(ApplicantActivity.this);
        userId = sharedPreferencesUtil.getUidNum();
        getMsgList();
        intTestData();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(this);
        rlApplicant = (RecyclerView) findViewById(R.id.rl_applicant);
        applicantAdapter = new ApplicantRecyclerViewAdapter(ApplicantActivity.this, listData);
        bxLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        rlApplicant.setLayoutManager(bxLayoutManager);
        // 设置adapter
        rlApplicant.setAdapter(applicantAdapter);
        applicantAdapter.setOnItemClickListener(new ApplicantRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onPassClick(View view, int position) {
                Toast.makeText(ApplicantActivity.this, "pass..", Toast.LENGTH_LONG).show();
                //通过是1
                result = 1;
                noticeId = listData.get(position).getNoticeId();
                approval();

            }

            @Override
            public void onUnpassClick(View view, int position) {
                Toast.makeText(ApplicantActivity.this, "unpass..", Toast.LENGTH_LONG).show();
                result = 2;
                noticeId = listData.get(position).getNoticeId();
                approval();
            }
//            @Override
//            public void onItemClick(View view, int position) {
//                if(view.getId()==R.id.tv_applicant_pass){
//                    Toast.makeText(ApplicantActivity.this, "pass..", Toast.LENGTH_LONG).show();
//                } else if(view.getId()==R.id.tv_applicant_unpass){
//                    Toast.makeText(ApplicantActivity.this, "unpass..", Toast.LENGTH_LONG).show();
//
//                }
//            }
        });

    }

    private void getMsgList() {
        new Thread(GetMsgListRunnable).start();
    }

    private void approval() {
        new Thread(ApplicantRunnable).start();

    }

    private JSONObject jsonObject;
    Runnable ApplicantRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                JSONObject jo = CompanyUtil.joinProcess(userId, result, noticeId);
                if (jo != null) {
                    jsonObject = jo;
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


    Runnable GetMsgListRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                JSONArray jo = CompanyUtil.getMsg(sharedPreferencesUtil.getUidNum());
                if (jo != null) {
                    jsonArray = jo;
                    //  expenseId = jo.getIntValue("expenseId");

                    //  new Thread(SubmitfileThread).start();
                    myHandler.sendEmptyMessage(2);
                } else {
                    myHandler.sendEmptyMessage(0);
                }

            } catch (Exception e) {
                myHandler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }


    };

    private void intTestData() {
        for (int i = 0; i < 4; i++) {
            // listData.add(i, new UnreadNoticeBean("title" + String.valueOf(i), "msgContent"));
        }
    }

    @Override
    protected void initListeners() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;

        }

    }
}
