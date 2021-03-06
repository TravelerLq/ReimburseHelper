package com.sas.rh.reimbursehelper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.Company;
import com.sas.rh.reimbursehelper.NetworkUtil.CompanyUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.UserUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.newactivity.ApprovalSettingActivity;
import com.sas.rh.reimbursehelper.newactivity.DepartmentActivity;
import com.sas.rh.reimbursehelper.newactivity.PersonSortActivity;
import com.sas.rh.reimbursehelper.view.activity.ApplicantActivity;
import com.sas.rh.reimbursehelper.view.activity.ApproveProcedureAddActivity;
import com.sas.rh.reimbursehelper.view.activity.ApproveProcedureManageActivity;
import com.sas.rh.reimbursehelper.view.activity.CompanyManaSelectActivity;
import com.sas.rh.reimbursehelper.view.activity.DepartmentsManageActivity;
import com.sas.rh.reimbursehelper.view.activity.EnterpriseDetailActivity;
import com.sas.rh.reimbursehelper.view.activity.EnterpriseUpdateActivity;
import com.sas.rh.reimbursehelper.view.activity.MembersManageActivity;
import com.sas.rh.reimbursehelper.view.activity.ProjectsManagerActivity;
import com.sas.rh.reimbursehelper.view.activity.SelectCompanyActivity;

import io.fabric.sdk.android.services.settings.AppSettingsData;


public class EnterpriseFragment extends Fragment {
    private SharedPreferencesUtil sharedPreferencesUtil;
    private int userId;
    private int companyId;
    private JSONObject jsonresult;
    private String companyName;
    LinearLayout emb, smb, mmb, dmb, apb, pmb, llAddEnterprise, llApplicant;
    private TextView tvCompanyName, tvCompanyId, tvGetCode;

    private String shareCode;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int status = jsonresult.getIntValue("status");
                if (status == 200) {

                }
                String companyJson = jsonresult.getString("company");
                company = JSON.parseObject(companyJson, Company.class);
                if (company == null) {
                    Loger.e("公司信息为空---");
                    // Toast.makeText(EnterpriseFragment.this.getActivity(),"公司信息为空",Toast.LENGTH_SHORT).show();
                } else {
                    companyName = company.getCompanyName();
                    companyId = company.getCompanyId();
                    sharedPreferencesUtil.setCompName(companyName);
                    sharedPreferencesUtil.writeCompanyId(String.valueOf(companyId));

                    if (!TextUtils.isEmpty(companyName)) {
                        tvCompanyName.setText(companyName);
                    }
                }

//                companyName = jsonresult.getString("companyName");
//                companyId = jsonresult.getIntValue("companyId");
                //  Company company = JSON.parseObject(companyJson, Company.class);
//                if (companyName == null) {
//                    Toast.makeText(EnterpriseFragment.this.getActivity(), "公司信息为空", Toast.LENGTH_SHORT).show();
//                } else {
//
//                    sharedPreferencesUtil.setCompName(companyName);
//                    sharedPreferencesUtil.writeCompanyId(String.valueOf(companyId));
//
//                    if (!TextUtils.isEmpty(companyName)) {
//                        tvCompanyName.setText(companyName);
//                    }
//                }


            } else if (msg.what == 2) {
                shareCode = jsonresult.getString("shareCode");
                tvCodeContent.setText(shareCode);
            }
        }
    };
    private TextView tvCodeContent;
    private Context mContext;
    private Company company;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enterprise, container, false);
        mContext = EnterpriseFragment.this.getActivity();
        sharedPreferencesUtil = new SharedPreferencesUtil(EnterpriseFragment.this.getActivity());
        userId = sharedPreferencesUtil.getUidNum();
        companyName = sharedPreferencesUtil.getCompName();

        if (sharedPreferencesUtil.getCidNum() == -1) {
//            ToastUtil.showToast(mContext, "公司ID为空", Toast.LENGTH_SHORT);
            Loger.e("----公司ID为空");
        } else {

            companyId = sharedPreferencesUtil.getCidNum();

        }
        getCompany();
        Loger.e("userId==" + userId + "companyId--" + companyId);
        initview(view);
        return view;
    }

    private void getCompany() {
        new Thread(RunnableCompany).start();
    }


    Runnable RunnableCompany = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject jsonObject = CompanyUtil.selectCompany(userId);
                if (jsonObject != null) {
                    jsonresult = jsonObject;
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

    private void initview(View view) {
        //企业信息详情
        tvCompanyName = (TextView) view.findViewById(R.id.tv_company_name);
        tvCompanyId = (TextView) view.findViewById(R.id.tv_company_id);
        tvCompanyId.setText(String.valueOf(companyId));
        tvCompanyName.setText(companyName);

        emb = (LinearLayout) view.findViewById(R.id.enterprisedetail_btn);
        //企业管理
        smb = (LinearLayout) view.findViewById(R.id.subjectsmanage_btn);
        mmb = (LinearLayout) view.findViewById(R.id.membersmanage_btn);
        dmb = (LinearLayout) view.findViewById(R.id.departmentsmanage_btn);
        //审核配置
        apb = (LinearLayout) view.findViewById(R.id.approveprocedure_btn);
        pmb = (LinearLayout) view.findViewById(R.id.projectmanage_btn);
        llAddEnterprise = (LinearLayout) view.findViewById(R.id.ll_add_enterprise);
        llApplicant = (LinearLayout) view.findViewById(R.id.ll_applicant);
        tvGetCode = (TextView) view.findViewById(R.id.tv_get_code);
        tvCodeContent = (TextView) view.findViewById(R.id.tv_get_code_content);
        tvGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCompanyCode();
            }
        });


        llApplicant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), ApplicantActivity.class);
                startActivity(it);
            }
        });

        llAddEnterprise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getActivity(), SelectCompanyActivity.class);
                startActivity(it);
            }
        });


//        emb.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent(getActivity(), EnterpriseDetailActivity.class);
//                startActivity(it);
//            }
//        });


        smb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(new SharedPreferencesUtil(getActivity()).isCidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和公司关联", Toast.LENGTH_LONG);
//                    return;
//                }
//                if(new SharedPreferencesUtil(getActivity()).isUidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和当前用户关联", Toast.LENGTH_LONG);
//                    return;
//                }
                Intent it = new Intent(getActivity(), CompanyManaSelectActivity.class);
                startActivity(it);
            }
        });
        mmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(new SharedPreferencesUtil(getActivity()).isCidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和公司关联", Toast.LENGTH_LONG);
//                    return;
//                }
//                if(new SharedPreferencesUtil(getActivity()).isUidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和当前用户关联", Toast.LENGTH_LONG);
//                    return;
//                }
                //  Intent it = new Intent(getActivity(), MembersManageActivity.class);
                Intent it = new Intent(getActivity(), PersonSortActivity.class);
                startActivity(it);
            }
        });
        dmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(new SharedPreferencesUtil(getActivity()).isCidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和公司关联", Toast.LENGTH_LONG);
//                    return;
//                }
//                if(new SharedPreferencesUtil(getActivity()).isUidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和当前用户关联", Toast.LENGTH_LONG);
//                    return;
//                }
                // Intent it = new Intent(getActivity(), DepartmentsManageActivity.class);
                Intent it = new Intent(getActivity(), DepartmentActivity.class);
                startActivity(it);
            }
        });
        apb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(new SharedPreferencesUtil(getActivity()).isCidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和公司关联", Toast.LENGTH_LONG);
//                    return;
//                }
//                if(new SharedPreferencesUtil(getActivity()).isUidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和当前用户关联", Toast.LENGTH_LONG);
//                    return;
//                }
                //Intent it = new Intent(getActivity(), ApproveProcedureManageActivity.class);
                Intent it = new Intent(getActivity(), ApprovalSettingActivity.class);
                startActivity(it);
            }
        });

        //改为修改公司信息
        pmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(new SharedPreferencesUtil(getActivity()).isCidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和公司关联", Toast.LENGTH_LONG);
//                    return;
//                }
//                if(new SharedPreferencesUtil(getActivity()).isUidEmpty() == true ){
//                    ToastUtil.showToast(getActivity(),"没有和当前用户关联", Toast.LENGTH_LONG);
//                    return;
//                }
                //  Intent it = new Intent(getActivity(), ProjectsManagerActivity.class);
                Intent it = new Intent(getActivity(), EnterpriseUpdateActivity.class);
                Bundle bundle = new Bundle();
                if (company != null) {
                    bundle.putSerializable("data", company);
                }
                it.putExtras(bundle);
                startActivity(it);
            }
        });
    }

    private void getCompanyCode() {
        new Thread(RunnableGetCompanyCode).start();

    }


    Runnable RunnableGetCompanyCode = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject jsonObject = CompanyUtil.getShareCode(userId);
                if (jsonObject != null) {
                    jsonresult = jsonObject;
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
