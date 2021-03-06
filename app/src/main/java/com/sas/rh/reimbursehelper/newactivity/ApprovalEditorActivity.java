package com.sas.rh.reimbursehelper.newactivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.Bean.newbean.ApprovalSettingtBean;
import com.sas.rh.reimbursehelper.Bean.newbean.DepartmentBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ApprovalUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.ApproveNumUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Sortlist.SortModel;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.TimePickerUtils;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtils;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.MembersManageActivity;

import java.util.Arrays;
import java.util.List;

public class ApprovalEditorActivity extends BaseActivity {
    private static final int REQUEST_PERSON_CODE = 111;

    private LinearLayout llApprovalNum;
    private LinearLayout llApprovalPerson;
    private TextView edtApprovalNum;
    private TextView edtApprovalPerson;
    private EditText edtApprovalTitle;
    private TextView tvSure;
    private ImageView ivBack;
    private TextView tvTitle;
    private String approvalNo;
    private String approvalPerson;
    private String approvalTitle;
    private int userId;
    private JSONObject jsonobj;
    private int approvalId;
    private SharedPreferencesUtil sharedPreferencesUtil;
    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                int status = jsonobj.getIntValue("status");
                if (status == 200) {
                    ToastUtils.show(baseContext, "更新成功！", ToastUtils.LENGTH_SHORT);
                    finish();
                } else {
                    ToastUtils.show(baseContext, "更新失败，请重试！", ToastUtils.LENGTH_SHORT);
                }
            } else if (msg.what == 0) {
                ToastUtil.showToast(baseContext, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(baseContext, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }
    };
    private Byte approvalNoByte;
    private String title;
    private String approval;
    private int approverId;
    private Context context;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_approve_procedure_manage;
    }

    @Override
    protected void initData() {
        llApprovalNum = (LinearLayout) findViewById(R.id.ll_approval_num);
        llApprovalPerson = (LinearLayout) findViewById(R.id.ll_approval_person);

        edtApprovalNum = (TextView) findViewById(R.id.edt_approval_no);
        edtApprovalPerson = (TextView) findViewById(R.id.edt_approval_person);
        edtApprovalTitle = (EditText) findViewById(R.id.edt_approval_title);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvTitle.setText("编辑");

        sharedPreferencesUtil = new SharedPreferencesUtil(ApprovalEditorActivity.this);
        userId = sharedPreferencesUtil.getUidNum();
        context = ApprovalEditorActivity.this;
        Loger.e("userId--" + userId);
        if (getIntent() != null) {
            ApprovalSettingtBean bean = (ApprovalSettingtBean) getIntent().getSerializableExtra("item");
            // title
            title = bean.getApproveNumName();
            //审批人
            approval = bean.getApproverName();
            //审批item id
            approvalId = bean.getApproveNumId();
            //审批人ID
            approverId = bean.getApproverId();
            //审批级别
            approvalNo = String.valueOf(bean.getApproverOrder());

            edtApprovalTitle.setText(title);
            edtApprovalPerson.setText(approval);
            edtApprovalNum.setText(approvalNo);


        }

    }

    @Override
    protected void initListeners() {
        llApprovalNum.setOnClickListener(this);
        llApprovalPerson.setOnClickListener(this);
        tvSure.setOnClickListener(this);
        ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_approval_num:
//                Resources res = getResources();
//                String[] status = res.getStringArray(R.array.approval_no);
//                List<String> allStatus = Arrays.asList(status);
//                TimePickerUtils.getInstance().onListDataPicker(this, allStatus, edtApprovalNum);
                Toast.makeText(context, "不可修改", Toast.LENGTH_SHORT).show();

                break;
            case R.id.ll_approval_person:
                // toActivity(baseContext,);
                //Intent intent = new Intent(baseContext, MembersManageActivity.class);
                Intent intent = new Intent(baseContext, PersonSortActivity.class);
                intent.putExtra("type", "2");
                startActivityForResult(intent, REQUEST_PERSON_CODE);
                break;
            case R.id.tv_sure:
                checkData();
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PERSON_CODE && resultCode == RESULT_OK) {
            Loger.e("---onActivityResult-----");
            // 选择审批人
            Bundle bundle = data.getExtras();
            SortModel userBean = (SortModel) bundle.getSerializable("user");
            edtApprovalPerson.setText(userBean.getName());
            //此人(即审核人)的id
            approverId = userBean.getUserId();

            Loger.e(" approveProduAddUserId--" + approvalId);

        }
    }

    private void checkData() {
        approvalNo = edtApprovalNum.getText().toString();
        approvalPerson = edtApprovalPerson.getText().toString();
        //approvalPerson = "12";
        approvalTitle = edtApprovalTitle.getText().toString();
        if (TextUtils.isEmpty(approvalNo)) {
            ToastUtils.show(baseContext, "审批序号不可为空！", ToastUtils.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(String.valueOf(approvalId))) {
            ToastUtils.show(baseContext, "审批人不可为空！", ToastUtils.LENGTH_SHORT);
            return;
        }

        if (TextUtils.isEmpty(approvalPerson)) {
            ToastUtils.show(baseContext, "审批名称！", ToastUtils.LENGTH_SHORT);
            return;
        }
//        approvalId = 1;
        approvalNoByte = Byte.valueOf(approvalNo);

        update();
    }

    private void update() {
        new Thread(approvalProduceRunnable).start();
    }


    Runnable approvalProduceRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                //审核名称
//        String approveNumName = "经理审核";
//        //审核人id,即要指定的审核人的id
//        Integer approverId = 3;
//        //所处的审核序号
//        Byte approveNum = 3;
//        //创建人id
//        Integer userId = 3;
                Loger.e("approvalId---" + approvalId);
                JSONObject jo = ApprovalUtil.updateApprove(approvalId, approvalTitle, approverId, userId);
                if (jo != null) {
                    jsonobj = jo;
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
