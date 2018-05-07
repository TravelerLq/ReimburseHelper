package com.sas.rh.reimbursehelper.newactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ReimbursementRight;
import com.sas.rh.reimbursehelper.Bean.newbean.DepartmentBean;
import com.sas.rh.reimbursehelper.NetworkUtil.DepartmentUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.view.activity.DepartmentsManageAddMasterActivity;
import com.sas.rh.reimbursehelper.widget.CircleImageView;
import com.warmtel.expandtab.KeyValueBean;

import java.util.ArrayList;
import java.util.List;

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.SinglePicker;

public class DepartDetailActivity extends AppCompatActivity {

    private ImageView addDM, backbt;

    private TextView tvName, tvLimit, tvMembers;
    private TextView tvManager;
    private TextView tvAuthority;
    private LinearLayout savebt, llExpenseAuthority;
    private TextView tvSure;
    private TextView tvTilte;
    private ImageView ivBack;
    private SharedPreferencesUtil spu;
    private JSONObject jsonresult;
    private JSONArray jsonArrayResult;
    private String dpname;//部门名称
    private String dplimit;//报销限额
    private String dmaster_id = "";//部门
    List<String> allStatus = new ArrayList<>();
    private ProgressDialogUtil pdu = new ProgressDialogUtil(DepartDetailActivity.this, "上传提示", "正在提交中");
    private Handler bumenxinxiback = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            //  pdu.dismisspd();
            if (msg.what == 1) {
                int status = jsonresult.getIntValue("status");
                if (status == 200) {
                    ToastUtil.showToast(DepartDetailActivity.this, "更新成功", Toast.LENGTH_LONG);
                } else {
                    ToastUtil.showToast(DepartDetailActivity.this, "更新失败，请重试", Toast.LENGTH_LONG);
                }

            } else if (msg.what == 2) {

                List<ReimbursementRight> reimbursementRights =
                        JSONArray.parseArray(jsonArrayResult.toJSONString(), ReimbursementRight.class);
                deptRight = new ArrayList<>();
                for (int i = 0; i < reimbursementRights.size(); i++) {

                    if (reimbursementRights.get(i).getReimbursementRightId() != null) {
                        ReimbursementRight bean = reimbursementRights.get(i);
                        allStatus.add(bean.getReimbursementRightName());
                        KeyValueBean keyValueBean = new KeyValueBean(bean.getReimbursementRightId().toString(), bean.getReimbursementRightName());
                        Loger.e("value--" + keyValueBean.getValue());
                        deptRight.add(keyValueBean);

                    }
                }


            } else if (msg.what == 0) {
                ToastUtil.showToast(DepartDetailActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(DepartDetailActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    private String tvAuthorityStr;
    private List<KeyValueBean> deptRight;
    private int selectPos;
    private String selectId;
    private String departName;
    private String manager;
    private Context context;
    private int departId;
    private int userId;
    private String authority;
    private String members;
    private String limit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depart_detail);
        context = DepartDetailActivity.this;
        spu = new SharedPreferencesUtil(DepartDetailActivity.this);
        userId = spu.getUidNum();
        tvAuthority = (TextView) findViewById(R.id.tv_expense_authority);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvLimit = (TextView) findViewById(R.id.tv_limit);
        tvManager = (TextView) findViewById(R.id.tv_manager);
        tvMembers = (TextView) findViewById(R.id.tv_members);

        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvTilte.setText("部门详情");

//        EditText  edtDepartName=(EditText)findViewById(R.id.dname);
//        EditText edtLimitNum=(EditText)findViewById(R.id.dlimit);
//
//        TextView tvAuthority=(TextView)findViewById(R.id.tv_expense_authority);
//        TextView tvManager=(TextView)findViewById(R.id.master_name);

        if (getIntent() != null) {
            DepartmentBean bean = (DepartmentBean) getIntent().getSerializableExtra("item");

            departName = bean.getDepartmentName();
            manager = bean.getDeptLeaderName();
            departId = bean.getDepartmentId();
            authority = bean.getReimbursementRightName();
            members = String.valueOf(bean.getNumberOfEmployees());
            limit = bean.getDepartmentQuota();

            // String limit=bean.get
            tvName.setText(departName);
            tvMembers.setText(members);
            tvLimit.setText(limit);
            tvAuthority.setText(authority);
            tvManager.setText(manager);

        }


//        getDeptRight();


        tvAuthority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Resources res = getResources();
//                String[] status = res.getStringArray(R.array.approval_no);
//                if (allStatus.size() > 0) {
//
//                    //  View view1 = TimePickerUtils.getInstance().
//                    // onListDataPicker(DepartmentsManageAddItemActivity.this, allStatus, tvAuthority);
//                    onListDataPicker(EditDepartActivity.this, allStatus, tvAuthority);
//
//                    // selectPos = (int)view1.getTag();
//
////                    selectId = deptRight.get(selectPos).getKey();
////                    Loger.e("--selectPos" + selectPos + "selectId--" + selectId);
//                }

               // Toast.makeText(context, "不可修改", Toast.LENGTH_SHORT).show();


                // TimePickerUtils.getInstance().onListPicker(DepartmentsManageAddItemActivity.this, deptRight, tvAuthority);
            }
        });


//        addDM.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent it = new Intent(DepartDetailActivity.this, DepartmentsManageAddMasterActivity.class);
//                startActivityForResult(it, 0);
//            }
//        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        tvSure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // CreateDepartmentInfo();
//                updateData();
//            }
//        });
    }

    private void getDeptRight() {
        new Thread(getDeptRightRunnable).start();
    }



//
//    private void updateData() {
//        if (dname.getText().toString().trim().equals("")) {
//            ToastUtil.showToast(DepartDetailActivity.this, "请部门目名称", Toast.LENGTH_LONG);
//            return;
//        }
//        if (dlimit.getText().toString().trim().equals("")) {
//            ToastUtil.showToast(DepartDetailActivity.this, "请填报销限额", Toast.LENGTH_LONG);
//            return;
//        }
//        if (dmaster_id == null || dmaster_id.trim().equals("")) {
//            ToastUtil.showToast(DepartDetailActivity.this, "请选择部门负责人", Toast.LENGTH_LONG);
//            return;
//        }
//        if (dmaster_id == null || tvAuthority.getText().toString().trim().equals("")) {
//
//            ToastUtil.showToast(DepartDetailActivity.this, "报销级别不可以为空", Toast.LENGTH_LONG);
//            return;
//        }
//        // tvAuthorityStr = tvAuthority.getText().toString().trim();
//        // tvAuthorityStr = selectPos;
//        dpname = dname.getText().toString().trim();
//        dplimit = dlimit.getText().toString().trim();
//        //  pdu.showpd();
//        new Thread(updateDepartmentInfoThread).start();
//    }

    private int managerId;



    Runnable getDeptRightRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jsonArray = new DepartmentUtil().getDeptRight(spu.getUidNum());
                if (jsonArray != null) {
                    jsonArrayResult = jsonArray;
                    bumenxinxiback.sendEmptyMessage(2);
                } else {
                    bumenxinxiback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                bumenxinxiback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    public void onListDataPicker(Activity context, final List<String> allStatus, final View view) {
        final int[] pos = {0};
        final SinglePicker<String> picker = new SinglePicker<String>(context, allStatus);
        picker.setCanLoop(true);
        picker.setWheelModeEnable(false);
        picker.setItemWidth(200);
        picker.setTopPadding(15);
        picker.setTextSize(25);
        picker.setSelectedIndex(0);
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int i, String s) {
                pos[0] = i;
                Loger.e("i," + i);
            }
        });

        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int i, String s) {
                Loger.e("viewName=" + (view instanceof TextView));
                spu.setPos(String.valueOf(i));
                selectId = deptRight.get(i).getKey();
                Loger.e("selectId==" + selectId);
                if (view instanceof TextView) {
                    Loger.e("view instanceof TextView---");
                    ((TextView) view).setText(allStatus.get(i));
                } else if (view instanceof EditText) {
                    ((EditText) view).setText(allStatus.get(i));
                }

                view.setTag(i);
            }
        });
        picker.setWeightEnable(true);

        if (view.getTag() == null) {
            view.setTag(0);
        }

        picker.show();
        Loger.e("view.selectPOs()" + view.getTag());

    }


}
