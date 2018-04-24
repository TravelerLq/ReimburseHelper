package com.sas.rh.reimbursehelper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.sas.rh.reimbursehelper.Adapter.ExpenseAdapter;
import com.sas.rh.reimbursehelper.Adapter.ThirdTypeAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ExpenseThirdTypeBean;
import com.sas.rh.reimbursehelper.Bean.ThirdExpenseCategoryBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseItemUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.RecycleViewDivider;
import com.warmtel.expandtab.KeyValueBean;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.com.syan.spark.client.sdk.data.handler.MyHandler;

import static com.sas.rh.reimbursehelper.NetworkUtil.FormUtil.addForm;

/**
 * Created by liqing on 18/3/28.
 * 三级报销类别
 */

public class ThirdExpenseTypeActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private ThirdTypeAdapter expenseAdapter;
    private Context context;
    private List<KeyValueBean> mData;
    private String id;
    private SharedPreferencesUtil spu;
    private Byte expenseCategoryId;
    private JSONArray jsonresult;
    private RecyclerView.LayoutManager bxLayoutManager;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Log.e("----handleMsg", "---");
                List<ExpenseThirdTypeBean> thirdExpenseCategoryList =
                        JSONArray.parseArray(jsonresult.toJSONString(), ExpenseThirdTypeBean.class);
                List<KeyValueBean> thirdList = new ArrayList<>();
                for (int i = 1; i < thirdExpenseCategoryList.size(); i++) {
                    Log.e("----list", "---");
                    if (thirdExpenseCategoryList.get(i).getExpenseItemId() != null) {
                        ExpenseThirdTypeBean bean = thirdExpenseCategoryList.get(i);
                        KeyValueBean keyValueBean = new KeyValueBean(bean.getExpenseItemId().toString(), bean.getExpenseItemName());
                        thirdList.add(keyValueBean);
                    }

                }
                expenseAdapter.refresh(thirdList);

                //  expenseAdapter = new ThirdTypeAdapter(context, thirdList);
                for (int i = 0; i < thirdList.size(); i++) {
                    Log.e("thirdAc==", "thirdList==" + thirdList.get(i).getValue());
                }
            }
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_third_expense_type;
    }

    @Override
    protected void initData() {
        context = ThirdExpenseTypeActivity.this;
        spu = new SharedPreferencesUtil(context);
        recyclerView = (RecyclerView) findViewById(R.id.rl_third_type);
        bxLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // 设置布局管理器
        recyclerView.setLayoutManager(bxLayoutManager);
        //add divider
        recyclerView.addItemDecoration(new RecycleViewDivider
                (context, LinearLayoutManager.HORIZONTAL, 1, getResources().getColor(R.color.blue)));
        mData = new ArrayList<>();
        id = getIntent().getStringExtra("id");
        if (!TextUtils.isEmpty(id)) {
            expenseCategoryId = Byte.parseByte(id);
            getThirdCategory(expenseCategoryId);
        }

        expenseAdapter = new ThirdTypeAdapter(context, mData);
        recyclerView.setAdapter(expenseAdapter);
        expenseAdapter.setOnItemClickListener(new ThirdTypeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Log.e("---", "pos=" + pos);
                Intent intent = new Intent();
                intent.putExtra("thirdKey", mData.get(pos).getKey());
                intent.putExtra("thirdValue", mData.get(pos).getValue());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getThirdCategory(Byte id) {
        new Thread(GetFormInfoThread).start();
    }


    @Override
    protected void initListeners() {

    }

    //setResult pass third type
//
//    private static class MyHandler extends Handler {
//        private final WeakReference<ThirdExpenseTypeActivity> mActivity;
//
//
//        private MyHandler(ThirdExpenseTypeActivity activity) {
//            mActivity = new WeakReference<ThirdExpenseTypeActivity>(activity);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            ThirdExpenseTypeActivity activity = mActivity.get();
//            if (activity != null) {
//                // ...
//                if (msg.what == 1) {
//                    Log.e("----handleMsg", "---");
//                    List<ThirdExpenseCategoryBean> thirdExpenseCategoryList =
//                            JSONArray.parseArray(jsonresult.toJSONString(), ThirdExpenseCategoryBean.class);
////                for (int i = 0; i < thirdList.size(); i++) {
////                    Log.e("thirdExpenseCat i=", "" + i + thirdExpenseCategoryList.get(i).getItemName());
////                }
//                    for (int i = 1; i < thirdExpenseCategoryList.size(); i++) {
//                        Log.e("----list", "---");
//                        if (thirdExpenseCategoryList.get(i).getItem() != null) {
//                            ThirdExpenseCategoryBean bean = thirdExpenseCategoryList.get(i);
//                            KeyValueBean keyValueBean = new KeyValueBean(bean.getItem(), bean.getItemName());
//                            mData.add(keyValueBean);
//                        }
//
//                    }
//                    Log.e("----list", "-end--");
//
//                    for (int i = 0; i < mData.size(); i++) {
//                        Log.e("thirdAc==", "thirdList==" + mData.get(i).getValue());
//                    }
//                }
//
//            }
//        }
//    }


    // 获取三级报销单 fanhui
    Runnable GetFormInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                //   JSONArray jo = addForm((Integer) spu.getUidNum(), expenseCategoryId);
                JSONArray jo = ExpenseItemUtil.getThirdCategory(spu.getUidNum(), expenseCategoryId);
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
    public void onClick(View view) {

    }
}
