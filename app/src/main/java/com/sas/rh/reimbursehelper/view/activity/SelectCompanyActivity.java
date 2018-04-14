package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.sas.rh.reimbursehelper.Adapter.ApprovalProducerAdapter;
import com.sas.rh.reimbursehelper.Adapter.SelectCompanyAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ApproveNum;
import com.sas.rh.reimbursehelper.Bean.Company;
import com.sas.rh.reimbursehelper.NetworkUtil.ApproveNumUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.CompanyUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.util.ArrayList;
import java.util.List;
/*
 员工添加选择公司
 */

public class SelectCompanyActivity extends BaseActivity {
    private static final int REQUEST_PERSON_CODE = 111;

    private List<Company> list;
    private RecyclerView recyclerView;
    private ImageView ivAdd;
    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvSelectedCompany;
    private TextView tvSure;
    private EditText edtCompany;
    private TextView tvSelect;
    private int userId;
    private JSONArray jsonobj;
    private int approvalId;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private SelectCompanyAdapter adapter;
    private JSONObject jsonObject;
    private JSONArray jsonresult;

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

                //  List<ApproveNum> approveNumList = JSONArray.parseArray(jsonArray.toJSONString(), ApproveNum.class);
                List<Company> companyList = JSONArray.parseArray(jsonresult.toJSONString(), Company.class);
                if (companyList.size() == 0) {
                    ToastUtil.showToast(baseContext, "暂无数据！", Toast.LENGTH_LONG);
                    return;
                }
                list.clear();
                list.addAll(companyList);
                adapter.notifyDataSetChanged();
            } else if (msg.what == 2) {
                //添加公司
               int status =jsonObject.getIntValue("status");
               if(status==200){
                   ToastUtil.showToast(baseContext, "申请提交成功！", Toast.LENGTH_LONG);
                   finish();
               }

            } else if (msg.what == 0) {
                ToastUtil.showToast(baseContext, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(baseContext, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }
    };
    private LinearLayoutManager bxLayoutManager;
    private String selectedStr;
    private String selectedCompanyStr;
    private Integer companyId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_company;
    }

    @Override
    protected void initData() {
        tvSelectedCompany = (TextView) findViewById(R.id.tv_selected_company);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        edtCompany = (EditText) findViewById(R.id.edt_company);
        tvSelect = (TextView) findViewById(R.id.tv_select);

        recyclerView = (RecyclerView) findViewById(R.id.recycle_select_company);
        list = new ArrayList<>();

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvTitle.setText("添加公司");
        sharedPreferencesUtil = new SharedPreferencesUtil(SelectCompanyActivity.this);
        userId = sharedPreferencesUtil.getUidNum();
        Loger.e("companyId--" + userId);
        initTestData();

        bxLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        adapter = new SelectCompanyAdapter(list);

        // 设置布局管理器
        recyclerView.setLayoutManager(bxLayoutManager);
        // 设置adapter
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Loger.e("select---pos=" + position);
                String name = list.get(position).getCompanyName();
                edtCompany.setText(name);
                tvSelectedCompany.setText(name);
                selectedCompanyStr = tvSelectedCompany.getText().toString();
                companyId = list.get(position).getCompanyId();
            }
        });
    }

    private void initTestData() {
        edtCompany.setText("御安神");
    }

    @Override
    protected void initListeners() {
        ivBack.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
        tvSure.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_select:
                selectComp();

                break;
            case R.id.tv_sure:
                submit();
                break;
            default:
                break;
        }
    }

    private void selectComp() {
        selectedStr = edtCompany.getText().toString();

        if (TextUtils.isEmpty(selectedStr)) {
            ToastUtil.showToast(SelectCompanyActivity.this, "请输入密码", Toast.LENGTH_SHORT);
            edtCompany.requestFocus();
            return;
        }
        Loger.e("userId---" + spu.getUidNum());
        new Thread(SelectRunnable).start();


    }


    Runnable SelectRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = CompanyUtil.selectLike(selectedStr, spu.getUidNum());
                if (jo != null) {
                    jsonresult = jo;
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

    @Override
    protected void onResume() {
        super.onResume();
        // getDataList();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void submit() {
        new Thread(SubmitRunnable).start();
    }


    Runnable SubmitRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                JSONObject jo = CompanyUtil.joinCompany(companyId,userId);
                if (jo != null) {
                    jsonObject = jo;
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
}
