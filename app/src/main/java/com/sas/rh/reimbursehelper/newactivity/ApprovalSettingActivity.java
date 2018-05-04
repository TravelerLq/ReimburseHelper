package com.sas.rh.reimbursehelper.newactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Adapter.ApprovalProducerAdapter;
import com.sas.rh.reimbursehelper.Adapter.newadapter.ApprovalSetRecycleAdapter;
import com.sas.rh.reimbursehelper.Adapter.newadapter.DepartmentRecycleAdapter;
import com.sas.rh.reimbursehelper.Bean.newbean.ApprovalSettingtBean;
import com.sas.rh.reimbursehelper.Bean.newbean.DepartmentBean;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.PopupWindowUtil;
import com.sas.rh.reimbursehelper.view.activity.ApproveProcedureAddActivity;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.view.activity.DepartmentsManageAddItemActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqing on 18/5/3.
 * 审核配置Activity
 */

public class ApprovalSettingActivity extends BaseActivity {
    private TextView tvTilte;
    private ImageView ivBack, ivAdd;
    private RecyclerView recyclerView;
    private Context context;
    private LinearLayoutManager layoutManager;
    private ApprovalSetRecycleAdapter adapter;
    private List<ApprovalSettingtBean> beanList;
    private PopupWindow mPopupWindow;
    private int selectPos;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_approval_setting;
    }

    @Override
    protected void initData() {
        context = ApprovalSettingActivity.this;
        beanList = new ArrayList<>();
        initTestData();
        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.VISIBLE);


        recyclerView = (RecyclerView) findViewById(R.id.rl_approval_set);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new ApprovalSetRecycleAdapter(context, beanList);
        recyclerView.setAdapter(adapter);
        tvTilte.setText("部门管理");
        adapter.setOnItemClickListener(new ApprovalSetRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Loger.e("--------departmentItemclick-");
            }

            @Override
            public void OnMoreClick(View view, int pos) {
                Loger.e("--------onMoreclick-");
                //  toActivity(context, DepartmentsManageAddItemActivity.class);
                //  selectPos = pos;
                showPopWindow(view, pos);
            }
        });
    }

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
                if (view.getId() == R.id.tv_edit) {
                    Bundle bundle = new Bundle();
                    ApprovalSettingtBean bean = beanList.get(pos);
                    bundle.putSerializable("item", bean);
                    //gai
                    Intent intent = new Intent(context, ApprovalEditorActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    beanList.remove(pos);
                    adapter.notifyDataSetChanged();
                }
                mPopupWindow.dismiss();
            }
        };

        contentView.findViewById(R.id.tv_edit).setOnClickListener(menuItemOnClickListener);
        contentView.findViewById(R.id.tv_delete).setOnClickListener(menuItemOnClickListener);
        return contentView;
    }

    private void initTestData() {
        ApprovalSettingtBean bean;
        for (int i = 0; i < 5; i++) {
            bean = new ApprovalSettingtBean();
            bean.setDname("出差审批" + i);
            bean.setName("张问" + i);
            bean.setNum("" + (i % 3));
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
                toActivity(context, ApproveProcedureAddActivity.class);
                break;
            default:
                break;
        }
    }
}
