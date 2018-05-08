package com.sas.rh.reimbursehelper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.sas.rh.reimbursehelper.Adapter.NoticeAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.UnreadNoticeBean;
import com.sas.rh.reimbursehelper.NetworkUtil.CompanyUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.view.activity.ApprovalDetailActivity;
import com.sas.rh.reimbursehelper.view.activity.ExpenseDetailActivity;
import com.sas.rh.reimbursehelper.view.activity.NoticeDetailActivity;
import com.sas.rh.reimbursehelper.view.activity.ViewExpenseActivity;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<String>();
    private List<UnreadNoticeBean> beanList;
    private NoticeAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    Handler handler = new Handler();
    int page = 0;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(getActivity(), "提示", "提交更改中");
    String department_id;
    private JSONObject bxmessagelist;
    private JSONObject messagealterrs;
    private SharedPreferencesUtil spu;
    private Context context;
    private Handler messagelistback = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (pdu.getMypDialog() != null) {
                pdu.dismisspd();
            }
            if (msg.what == 1) {
                if (jsonResult != null) {
                    //System.out.print("resultList:");
                    List<UnreadNoticeBean> list = JSONArray.parseArray(jsonResult.toJSONString(), UnreadNoticeBean.class);
                    if (list.size() == 0) {
                      //  ToastUtil.showToast(getActivity(), "暂无数据", Toast.LENGTH_SHORT);
                      //  Toast.makeText(getActivity(), "暂无消息", Toast.LENGTH_SHORT);
                    }
                    beanList.clear();
                    beanList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }


            } else if (msg.what == 2) {
                // ToastUtil.showToast(getActivity(), messagelist.get("HostTime") + ":" + messagelist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            } else if (msg.what == 0) {
                ToastUtil.showToast(getActivity(), "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(getActivity(), "通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    private JSONArray jsonResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.test_recycler_view_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.test_recycler_view);
        spu = new SharedPreferencesUtil(this.getActivity());
        beanList = new ArrayList<>();
        init(view);

        return view;
    }


    private void init(View view) {
        context = this.getActivity();
        spu = new SharedPreferencesUtil(context);
        adapter = new NoticeAdapter(getActivity(), beanList);
        mAdapter = new RecyclerAdapterWithHF((RecyclerView.Adapter) adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        ptrClassicFrameLayout.postDelayed(new Runnable() {

            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh(true);
            }
        }, 150);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 0;
                        mData.clear();
                        for (int i = 0; i < 17; i++) {
                            mData.add(new String("  RecyclerView item  -" + i));
                        }
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.refreshComplete();
                        ptrClassicFrameLayout.setLoadMoreEnable(true);
                    }
                }, 1500);
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mData.add(new String("  RecyclerView item  - add " + page));
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.loadMoreComplete(true);
                        page++;
                        Toast.makeText(getActivity(), "load more complete", Toast.LENGTH_SHORT).show();
                    }
                }, 1000);
            }
        });
        adapter.setOnItemClickListener(new NoticeAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                UnreadNoticeBean bean = beanList.get(pos);
                Loger.e("--notice-adapter-clicked");
                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("itemNotice", beanList.get(pos));
//                if (beanList.get(pos) == null) {
//                    Loger.e("-- item=null--");
//                }
//                intent.setClass(context, NoticeDetailActivity.class);
                intent.putExtra("id", String.valueOf(bean.getNoticeId()));
                intent.putExtra("title", bean.getTitle());
                intent.putExtra("content", bean.getContent());
                intent.putExtra("date", bean.getDate());
                intent.setClass(context, NoticeDetailActivity.class);
                startActivity(intent);
            }
        });
//
//        mAdapter.setOnItemClickListener(new RecyclerAdapterWithHF.OnItemClickListener() {
//            @Override
//            public void onItemClick(RecyclerAdapterWithHF adapter, RecyclerView.ViewHolder vh, int position) {
////                Toast.makeText(ViewExpenseActivity.this,
////                        "mAdapter第" + position + "个", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//
//                bundle.putSerializable("item", beanList.get(position));
//                Loger.e("-- mdapter ---notice--clicked");
//                intent.setClass(context, NoticeDetailActivity.class);
//
//                startActivity(intent);
//
//            }
//        });

        GetAllMessage();
    }

    private void GetAllMessage() {
        // pdu.showpd();
        new Thread(GetAllMessageInfoThread).start();
    }

    Runnable GetAllMessageInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
//                JSONObject jo = new ShenheUtils().getcommittermessage(1,1);
                JSONArray jsonArray = CompanyUtil.getNotice(spu.getUidNum());
                if (jsonArray != null) {
                    jsonResult = jsonArray;
                    messagelistback.sendEmptyMessage(1);
                } else {
                    messagelistback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                messagelistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    @Override
    public void onResume() {
        super.onResume();
        GetAllMessage();
    }
}
