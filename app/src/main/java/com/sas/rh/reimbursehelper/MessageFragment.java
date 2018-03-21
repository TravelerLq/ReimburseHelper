package com.sas.rh.reimbursehelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.sas.rh.reimbursehelper.Entity.DepartmentDetailInfo;
import com.sas.rh.reimbursehelper.NetUtil.BumenUtils;
import com.sas.rh.reimbursehelper.NetUtil.ShenheUtils;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerView mRecyclerView;
    private List<String> mData = new ArrayList<String>();
    private RecyclerAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    Handler handler = new Handler();
    int page = 0;
    private ProgressDialogUtil pdu =new ProgressDialogUtil(getActivity(),"提示","提交更改中");
    String department_id;
    private JSONObject bxmessagelist;
    private JSONObject messagealterrs;
//    private Handler messagelistback = new Handler(){
//        @Override
//        public void handleMessage(android.os.Message msg) {
//            if(pdu.getMypDialog() != null){
//                pdu.dismisspd();
//            }
//            if(msg.what == 1){
////                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
////            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
//                mList.clear();
//                if (messagelist.get("resultList")!= null) {
//                    //System.out.print("resultList:");
//                    JSONArray jsonArray = messagelist.getJSONArray("resultList");
//                    for (Object object : jsonArray) {
//                        JSONObject jObject = JSONObject.fromObject(object);
//                        DepartmentDetailInfo dma = new DepartmentDetailInfo();
//                        dma.setDepartment_name(jObject.get("bmName").toString());
//                        dma.setDepartment_id(jObject.get("bmId").toString());
//                        dma.setDepartment_gsid(jObject.get("gongsiId").toString());
//                        dma.setDepartment_state(jObject.get("isOpen").toString());
//                        mList.add(dma);
//                        //System.out.println(jObject);
//                    }
//                }
//                mAdapter.notifyDataSetChanged();
//                ToastUtil.showToast(getActivity(),messagelist.get("HostTime")+":"+messagelist.get("Note").toString(), Toast.LENGTH_LONG);
//
//            }else if(msg.what == 2){
//                ToastUtil.showToast(getActivity(),messagelist.get("HostTime")+":"+messagelist.get("ResultCode").toString(), Toast.LENGTH_LONG);
//            }else if(msg.what == 0){
//                ToastUtil.showToast(getActivity(),"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
//            }else if(msg.what == -1){
//                ToastUtil.showToast(getActivity(),"通信模块异常！", Toast.LENGTH_LONG);
//            }
//        }
//
//    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.test_recycler_view_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.test_recycler_view);
        init(view);
        return view;
    }

    private void init(View view) {
        adapter = new RecyclerAdapter(getActivity(), mData);
        mAdapter = new RecyclerAdapterWithHF(adapter);
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
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<String> datas;
        private LayoutInflater inflater;

        public RecyclerAdapter(Context context, List<String> data) {
            super();
            inflater = LayoutInflater.from(context);
            datas = data;
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            ChildViewHolder holder = (ChildViewHolder) viewHolder;
            //holder.itemTv.setText(datas.get(position));
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
            View view = inflater.inflate(R.layout.item_msglist_layout, viewHolder,false);
            return new ChildViewHolder(view);
        }

    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTv;

        public ChildViewHolder(View view) {
            super(view);
            //itemTv = (TextView) view;
        }

    }

//    private void GetAllMessage(){
//        pdu.showpd();
//        new Thread(GetAllMessageInfoThread).start();
//    }
//
//    Runnable GetAllMessageInfoThread = new Runnable() {
//        @Override
//        public void run() {
//            // TODO Auto-generated method stub
//
//            try{
//                JSONObject jo = new ShenheUtils().getcommittermessage(1,1);
//                if(jo != null){
//                    bxmessagelist = jo;
//                    messagelistback.sendEmptyMessage(1);
//                }else{
//                    messagelistback.sendEmptyMessage(0);
//                }
//            }catch(Exception e){
//                messagelistback.sendEmptyMessage(-1);
//                e.printStackTrace();
//            }
//        }
//
//    };

}
