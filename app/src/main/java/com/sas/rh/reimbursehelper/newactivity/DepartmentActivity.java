package com.sas.rh.reimbursehelper.newactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Adapter.newadapter.ApprovalRecycleAdapter;
import com.sas.rh.reimbursehelper.Adapter.newadapter.DepartmentRecycleAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.newbean.DepartmentBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ApprovalUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.DepartmentUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.PopupWindowUtil;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.DepartmentsManageAddItemActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqing on 18/5/3.
 * 部门管理Activity
 */

public class DepartmentActivity extends BaseActivity {
    private TextView tvTilte;
    private ImageView ivBack, ivAdd;
    private RecyclerView recyclerView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private DepartmentRecycleAdapter adapter;
    private List<DepartmentBean> beanList;
    private PopupWindow mPopupWindow;
    private int selectPos;
    private JSONArray jsonresult;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(DepartmentActivity.this, "提示", "正在加载中");

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                List<DepartmentBean> list = JSONArray.parseArray(jsonresult.toJSONString(), DepartmentBean.class);
                if (list.size() == 0) {
                    ToastUtil.showToast(context, "暂无数据", Toast.LENGTH_SHORT);
                }
                beanList.clear();
                beanList.addAll(list);
                adapter.notifyDataSetChanged();
            } else if (msg.what == 2) {

                int status = jsonObjectDelete.getIntValue("status");
                if (status == 200) {
                    ToastUtil.showToast(context, "删除成功", Toast.LENGTH_SHORT);
                    beanList.remove(selectPos);
                    adapter.notifyDataSetChanged();
                    getData();

                } else {
                    ToastUtil.showToast(context, "删除失败", Toast.LENGTH_SHORT);
                }
            } else if (msg.what == 0) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                ToastUtil.showToast(context, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                ToastUtil.showToast(context, "通信模块异常！", Toast.LENGTH_LONG);
            }

        }
    };
    private SharedPreferencesUtil spu;
    private int departmentId;
    private JSONObject jsonObjectDelete;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_department;
    }

    @Override
    protected void initData() {
        context = DepartmentActivity.this;
        spu = new SharedPreferencesUtil(context);
        beanList = new ArrayList<>();
        // initTestData();
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) findViewById(R.id.rl_department);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new DepartmentRecycleAdapter(context, beanList);
        recyclerView.setAdapter(adapter);
        tvTilte.setText("部门管理");
        adapter.setOnItemClickListener(new DepartmentRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Loger.e("--------departmentItemclick-");
                DepartmentBean bean = beanList.get(pos);

                //edit...
                Bundle bundle = new Bundle();
                // DepartmentBean bean = beanList.get(pos);
                bundle.putSerializable("item", bean);
                Intent intent = new Intent(context, DepartDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }

            @Override
            public void OnMoreClick(View view, int pos) {
                Loger.e("--------onMoreclick-");
                //  toActivity(context, DepartmentsManageAddItemActivity.class);
                //  selectPos = pos;
                showPopWindow(view, pos);
            }
        });

//        getData();
    }

    private void getData() {

        pdu.showpd();
        new Thread(getDepartmentRunnable).start();
    }


    Runnable getDepartmentRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jsonArray = new DepartmentUtil().selectDeptUnderCompany(spu.getUidNum());
                if (jsonArray != null) {
                    jsonresult = jsonArray;
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

    private void showPopWindow(View anchorView, int pos) {
        View contentView = getPopupWindowContentView(pos);
        mPopupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        // 如果不设置PopupWindow的背景，有些版本就会出现一个问题：无论是点击外部区域还是Back键都无法dismiss弹框
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        // 设置好参数之后再show
        int windowPos[] = PopupWindowUtil.calculatePopWindowPos(anchorView, contentView);
        int xOff = 20; // 可以自己调整偏移
        windowPos[0] -= xOff;
        mPopupWindow.showAtLocation(anchorView, Gravity.TOP | Gravity.START, windowPos[0], windowPos[1]);

    }

    private View getPopupWindowContentView(final int pos) {

        final View contentView = LayoutInflater.from(this).inflate(R.layout.dialog_menu, null);
        View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(view.getContext(), "Click " + ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
//                if (mPopupWindow != null) {
//                    mPopupWindow.dismiss();
//                }
                DepartmentBean bean = beanList.get(pos);
                departmentId = bean.getDepartmentId();
                if (view.getId() == R.id.tv_edit) {
                    //edit...
                    Bundle bundle = new Bundle();
                    // DepartmentBean bean = beanList.get(pos);
                    bundle.putSerializable("item", bean);
                    Intent intent = new Intent(context, EditDepartActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    //delete...
                    deleteData();
//                    beanList.remove(pos);
//                    adapter.notifyDataSetChanged();
                }
                mPopupWindow.dismiss();
            }
        };

        contentView.findViewById(R.id.tv_edit).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.tv_delete).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    private void deleteData() {
        new Thread(deleteDepartRunnable).start();
    }


    Runnable deleteDepartRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = DepartmentUtil.deleteDepartment(departmentId, spu.getUidNum());
                if (jo != null) {
                    jsonObjectDelete = jo;
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    private void initTestData() {
        DepartmentBean bean;
        for (int i = 0; i < 5; i++) {
            bean = new DepartmentBean();
            bean.setDepartmentName("财务部" + i);
            bean.setDeptLeaderName("张问" + i);
            bean.setNumberOfEmployees((i * 10));
            beanList.add(i, bean);
        }
    }

    @Override
    protected void initListeners() {
        ivBack.setOnClickListener(this);
        ivAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_add:
                toActivity(context, DepartmentsManageAddItemActivity.class);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }
}
