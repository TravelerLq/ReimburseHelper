package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.sas.rh.reimbursehelper.Adapter.ExpenseAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ExpenseApprovalResponseBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ApprovalUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import static com.sas.rh.reimbursehelper.NetworkUtil.ApprovalUtil.getMyApproval;
import static com.sas.rh.reimbursehelper.NetworkUtil.ApprovalUtil.getPendApproval;

/**
 * Created by liqing on 18/3/26.
 * 审批人 －查看审批项目详情 （报销单下的 －多条 报销项目）
 */

public class ViewApprovalDetailActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ExpenseAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    private List<ExpenseApprovalResponseBean> mData = new ArrayList<>();
    private TextView tvBarTitle;

    int page = 0;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(this, "提示", "加载中");
    private String userId;
    private SharedPreferencesUtil spu;
    private JSONArray jsonResult;
    private JSONObject jsonObjectResult;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //审批人 获取待自己审批的
            if (msg.what == 2) {
                mData.clear();
                List<ExpenseApprovalResponseBean> list = JSONArray.parseArray(jsonResult.toJSONString(),
                        ExpenseApprovalResponseBean.class);

                mData.addAll(list);
                mAdapter.notifyDataSetChanged();
                // pdu.dismisspd();
             //  getPic(mData.get(0).get);
            }
            //报销人 加载成功
            if (msg.what == 1) {
                mData.clear();
                List<ExpenseApprovalResponseBean> list = JSONArray.parseArray(jsonResult.toJSONString(),
                        ExpenseApprovalResponseBean.class);
                mData.addAll(list);
                mAdapter.notifyDataSetChanged();
                // pdu.dismisspd();
            }
            if (msg.what == 3) {
                String signtext = jsonObjectResult.getString("signtext");
                JSONObject signText = JSONObject.parseObject(signtext);
                Integer index = signText.getInteger("index");
                String doc = signText.getString("doc");
                String name = signText.getString("name");


            }
            //加载失败
            if (msg.what == 0) {
                //返回的mData 是null
                ToastUtil.showToast(ViewApprovalDetailActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }
            //
            if (msg.what == -1) {
                ToastUtil.showToast(ViewApprovalDetailActivity.this, "通信模块异常！", Toast.LENGTH_LONG);

            }
            // pdu.dismisspd();

        }
    };
    private String type;


    @Override
    protected int getLayoutId() {

        return R.layout.activity_view_expense;
    }

    @Override
    protected void initData() {
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.test_recycler_view_frame);
        mRecyclerView = (RecyclerView) findViewById(R.id.test_recycler_view);
        tvBarTitle = (TextView) findViewById(R.id.tv_bar_title);
        spu = new SharedPreferencesUtil(ViewApprovalDetailActivity.this);
        initView();
        type = getIntent().getStringExtra("type");
        if (type.equals("0")) {
            //报销人
            tvBarTitle.setText("报销单");
            getExpense();
        } else {
            tvBarTitle.setText("审批单");
            getApprovalExpense();
        }
    }

    @Override
    protected void initListeners() {

    }

    private void initView() {
        adapter = new ExpenseAdapter(ViewApprovalDetailActivity.this, mData);
        mAdapter = new RecyclerAdapterWithHF((RecyclerView.Adapter) adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
//        ptrClassicFrameLayout.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                ptrClassicFrameLayout.autoRefresh(true);
//            }
//        }, 150);

//        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        page = 0;
//                        mData.clear();
//                        for (int i = 0; i < 17; i++) {
//                            mData.add(new ExpenseApprovalResponseBean());
//                        }
//                        mAdapter.notifyDataSetChanged();
//                        ptrClassicFrameLayout.refreshComplete();
//                        ptrClassicFrameLayout.setLoadMoreEnable(true);
//                    }
//                }, 1500);
//            }
//        });

//        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//
//            @Override
//            public void loadMore() {
//                handler.postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        mData.add(new ExpenseApprovalResponseBean());
//                        mAdapter.notifyDataSetChanged();
//                        ptrClassicFrameLayout.loadMoreComplete(true);
//                        page++;
//                        Toast.makeText(ViewExpenseActivity.this, "load more complete", Toast.LENGTH_SHORT).show();
//                    }
//                }, 1000);
//            }
//        });

        //recycleView 的item点击事件
        adapter.setOnItemClickListner(new ExpenseAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                Toast.makeText(ViewApprovalDetailActivity.this,
                        "第" + view.getTag() + "个", Toast.LENGTH_SHORT).show();
            }
        });
        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
                Toast.makeText(ViewApprovalDetailActivity.this,
                        "mAdapter第" + position + "个", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemBean", mData.get(position));
                intent.putExtra("bundle", bundle);
              /*
               //审核实体id
        Integer approvalId = 24;
        //审核结果
        Byte approveResultId = 2;
        //驳回原因
        String rejectReason = "审核不通过，票面信息不完整";
        //用户id
        Integer userId = 3;
               */
                if (type.equals("0")) {
                    //报销人

                    intent.setClass(ViewApprovalDetailActivity.this, ExpenseDetailActivity.class);
                } else {
                    //审批人
                    intent.setClass(ViewApprovalDetailActivity.this, ApprovalDetailActivity.class);
                }

                startActivity(intent);

            }
        });
    }

    // type=0: 报销者查看 报销单
    private void getExpense() {
        //  pdu.showpd();
        new Thread(GetFormInfoThread).start();
    }


    Runnable GetFormInfoThread = new Runnable() {
        @Override
        public void run() {
            //

            try {
                JSONArray jsonArray = getMyApproval((Integer) spu.getUidNum());
                if (jsonArray != null) {
                    jsonResult = jsonArray;
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

    //type =1 审核者 查看审批单
    private void getApprovalExpense() {
        // pdu.showpd();
        new Thread(GetApprovalExpense).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("viewExpense --", "onResume---");
        if (type.equals("0")) {
            //报销人
            getExpense();
        } else {
            getApprovalExpense();
        }

    }

    Runnable GetApprovalExpense = new Runnable() {
        @Override
        public void run() {
            //

            try {

                jsonResult = getPendApproval( 1);

                if (jsonResult != null) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
                // pdu.dismisspd();
            }
        }

    };


    //根据expendId 获取图片

    public void getPic(int expendeId) {
        new Thread(GetPicRunnable).start();
    }

    Runnable GetPicRunnable = new Runnable() {
        @Override
        public void run() {
            //

            try {

                jsonObjectResult = ApprovalUtil.selectSignFile((Integer) spu.getUidNum());

                if (jsonObjectResult != null) {
                    handler.sendEmptyMessage(3);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
                // pdu.dismisspd();
            }
        }

    };

    @Override
    public void onClick(View view) {

    }
}
