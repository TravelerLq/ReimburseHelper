package com.sas.rh.reimbursehelper.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.NetworkUtil.DepartmentUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Sortlist.SortModel;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.TimePickerUtils;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.newactivity.PersonSortActivity;
import com.sas.rh.reimbursehelper.widget.CircleImageView;
import com.warmtel.expandtab.KeyValueBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.addapp.pickers.listeners.OnItemPickListener;
import cn.addapp.pickers.listeners.OnSingleWheelListener;
import cn.addapp.pickers.picker.SinglePicker;

public class DepartmentsManageAddItemActivity extends AppCompatActivity {

    private static final int REQUEST_MANAGER_CODE = 113;
    public static final String DEPART = "department";
    private ImageView addDM, backbt;
    private CircleImageView master_head;
    private EditText dname, dlimit;
    private TextView master_name;
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
    private ProgressDialogUtil pdu = new ProgressDialogUtil(DepartmentsManageAddItemActivity.this, "上传提示", "正在提交中");
    private Handler bumenxinxiback = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            //  pdu.dismisspd();
            if (msg.what == 1) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));

                int status = jsonresult.getIntValue("status");
                if (status == 200) {
                    ToastUtil.showToast(DepartmentsManageAddItemActivity.this, " 创建成功！", Toast.LENGTH_LONG);
                    finish();
                } else {
                    ToastUtil.showToast(DepartmentsManageAddItemActivity.this, "创建失败！", Toast.LENGTH_LONG);
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
                ToastUtil.showToast(DepartmentsManageAddItemActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(DepartmentsManageAddItemActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    private String tvAuthorityStr;
    private List<KeyValueBean> deptRight;
    private int selectPos;
    private String selectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments_manage_add_item);
        spu = new SharedPreferencesUtil(DepartmentsManageAddItemActivity.this);
        tvAuthority = (TextView) findViewById(R.id.tv_expense_authority);
        addDM = (ImageView) findViewById(R.id.addDM);
        dname = (EditText) findViewById(R.id.dname);
        dlimit = (EditText) findViewById(R.id.dlimit);
        master_head = (CircleImageView) findViewById(R.id.master_head);
        master_name = (TextView) findViewById(R.id.master_name);
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvSure = (TextView) findViewById(R.id.tv_sure);
        tvTilte.setText("部门添加");
        //获取报销权限
        getDeptRight();


        tvAuthority.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Resources res = getResources();
                String[] status = res.getStringArray(R.array.approval_no);
                if (allStatus.size() > 0) {

                    //  View view1 = TimePickerUtils.getInstance().
                    // onListDataPicker(DepartmentsManageAddItemActivity.this, allStatus, tvAuthority);
                    onListDataPicker(DepartmentsManageAddItemActivity.this, allStatus, tvAuthority);

                    // selectPos = (int)view1.getTag();

//                    selectId = deptRight.get(selectPos).getKey();
//                    Loger.e("--selectPos" + selectPos + "selectId--" + selectId);
                }


                // TimePickerUtils.getInstance().onListPicker(DepartmentsManageAddItemActivity.this, deptRight, tvAuthority);
            }
        });


        addDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent it = new Intent(DepartmentsManageAddItemActivity.this, DepartmentsManageAddMasterActivity.class);
//                startActivityForResult(it, REQUEST_MANAGER_CODE);

                Intent it = new Intent(DepartmentsManageAddItemActivity.this, PersonSortActivity.class);
                it.putExtra("type", DEPART);
                startActivityForResult(it, REQUEST_MANAGER_CODE);
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pdu.showpd();
                CreateDepartmentInfo();
            }
        });
    }

    private void getDeptRight() {
        new Thread(getDeptRightRunnable).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_MANAGER_CODE && resultCode == RESULT_OK) {
            Loger.e("---onActivityResult-----");

            Bundle bundle = data.getExtras();
            SortModel userBean = (SortModel) bundle.getSerializable("user");
            String manager = userBean.getName();
            managerId = userBean.getUserId();
            String idStr = String.valueOf(managerId);
            dmaster_id = idStr;

//            String sn = data.getStringExtra("slectedname");
//            //  String sp = data.getStringExtra("slectedpath");
//          String sid = data.getStringExtra("slectedstaffid");
//            System.out.println("aaaaaaaaaa:" + requestCode + ":" + sn + ":" + ":" + sid);
//            //  master_head.setImageResource(Integer.parseInt(sp));
//            dmaster_id = sid;
            master_name.setText(manager + "(" + idStr + ")");

        }
    }


    private void CreateDepartmentInfo() {
        if (dname.getText().toString().trim().equals("")) {
            ToastUtil.showToast(DepartmentsManageAddItemActivity.this, "请部门目名称", Toast.LENGTH_LONG);
            return;
        }
        if (dlimit.getText().toString().trim().equals("")) {
            ToastUtil.showToast(DepartmentsManageAddItemActivity.this, "请填报销限额", Toast.LENGTH_LONG);
            return;
        }
        if (dmaster_id == null || dmaster_id.trim().equals("")) {
            ToastUtil.showToast(DepartmentsManageAddItemActivity.this, "请选择部门负责人", Toast.LENGTH_LONG);
            return;
        }
        if (dmaster_id == null || tvAuthority.getText().toString().trim().equals("")) {

            ToastUtil.showToast(DepartmentsManageAddItemActivity.this, "报销级别不可以为空", Toast.LENGTH_LONG);
            return;
        }
        // tvAuthorityStr = tvAuthority.getText().toString().trim();
        // tvAuthorityStr = selectPos;
        dpname = dname.getText().toString().trim();
        dplimit = dlimit.getText().toString().trim();
        //  pdu.showpd();
        new Thread(sendCreateDepartmentInfoThread).start();
    }

    private int managerId;
    Runnable sendCreateDepartmentInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = new DepartmentUtil().addDepartment(dpname, Byte.valueOf(selectId),
                        Double.parseDouble(dplimit), spu.getUidNum(), managerId);
                if (jo != null) {
                    jsonresult = jo;
                    bumenxinxiback.sendEmptyMessage(1);
                } else {
                    bumenxinxiback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                bumenxinxiback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


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
