package com.sas.rh.reimbursehelper.view.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.sas.rh.reimbursehelper.NetworkUtil.DepartmentUtil;
import com.sas.rh.reimbursehelper.Entity.ReimbursementDepartment;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.RecyclerviewWithCheckbox.DividerItemDecoration;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;


public class DepartmentsManageActivity extends AppCompatActivity {

    PtrClassicFrameLayout ptrClassicFrameLayout;
    RecyclerView mRecyclerView;
    private List<ReimbursementDepartment> mData = new ArrayList<ReimbursementDepartment>();
    private DepartmentsManageActivity.RecyclerAdapter adapter;
    private RecyclerAdapterWithHF mAdapter;
    Handler handler = new Handler();
    private ImageView add_departmentitem,backbt;
    int page = 0;
    private ProgressDialogUtil pdu =new ProgressDialogUtil(DepartmentsManageActivity.this,"提示","提交更改中");
    String department_id;
    private SharedPreferencesUtil spu;
    private JSONArray departmentlist;
    private JSONObject deapetmentlist;
    private JSONObject deapetmentalterrs;
    private Handler deapetmentlistback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            ptrClassicFrameLayout.refreshComplete();
            ptrClassicFrameLayout.setLoadMoreEnable(false);
            if(pdu.getMypDialog() != null){
                pdu.dismisspd();
            }
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                mData.clear();
                if (departmentlist!= null ) {
                    mData = JSONArray.parseArray(departmentlist.toJSONString(), ReimbursementDepartment.class);

                    //System.out.print("resultList:");
                    //JSONArray jsonArray = deapetmentlist.getJSONArray("resultList");
//                    for (Object object : departmentlist) {
//                        JSONObject jObject = JSONObject.fromObject(object);
//                        DepartmentDetailInfo dma = new DepartmentDetailInfo();
//                        dma.setDepartment_name(jObject.get("bmName").toString());
//                        dma.setDepartment_id(jObject.get("bmId").toString());
//                        dma.setDepartment_gsid(jObject.get("gongsiId").toString());
//                        dma.setDepartment_state(jObject.get("isOpen").toString());
//                        mData.add(dma);
//                        System.out.println(jObject);
//                    }
                }
                mAdapter.notifyDataSetChanged();
                ToastUtil.showToast(DepartmentsManageActivity.this,"加载完毕", Toast.LENGTH_LONG);

            }else if(msg.what == 2){
                //ToastUtil.showToast(DepartmentsManageActivity.this,deapetmentlist.get("HostTime")+":"+deapetmentlist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            }else if(msg.what == 0){
                ToastUtil.showToast(DepartmentsManageActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(DepartmentsManageActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departments_manage);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.department_recycler_view_frame);
        mRecyclerView = (RecyclerView) findViewById(R.id.department_recycler_view);
        add_departmentitem = (ImageView) findViewById(R.id.add_departmentitem);
        backbt = (ImageView)findViewById(R.id.backbt);
        spu = new SharedPreferencesUtil(DepartmentsManageActivity.this);
        add_departmentitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(DepartmentsManageActivity.this,DepartmentsManageAddItemActivity.class);
                startActivity(it);
            }
        });

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }


    private void init() {
        adapter = new DepartmentsManageActivity.RecyclerAdapter(DepartmentsManageActivity.this);
        mAdapter = new RecyclerAdapterWithHF(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(DepartmentsManageActivity.this));
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(DepartmentsManageActivity.this, DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(DepartmentsManageActivity.this, R.drawable.divider_main_bg_height_1));
        mRecyclerView.addItemDecoration(itemDecorationHeader);
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
                GetallDepartmentInfo();
            }
        });
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private LayoutInflater inflater;

        public RecyclerAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
            DepartmentsManageActivity.ChildViewHolder holder = (DepartmentsManageActivity.ChildViewHolder) viewHolder;
            holder.itemTv.setText(mData.get(position).getDepartmentName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(DepartmentsManageActivity.this)
                            .setMessage("确定删除该项？")
                            .setNegativeButton("取消",null)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    department_id = ""+mData.get(position).getDepartmentId();
                                    DeleteSubject();
                                }
                            }).show();
                    return false;
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewHolder, int position) {
            View view = inflater.inflate(R.layout.item_departmentsmanage_list, viewHolder,false);
            return new DepartmentsManageActivity.ChildViewHolder(view);
        }

    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView itemTv;
        public ToggleButton toggleBtn;

        public ChildViewHolder(View view) {
            super(view);
            itemTv = (TextView) view.findViewById(R.id.itemTv);
            toggleBtn = (ToggleButton)view.findViewById(R.id.togglebutton) ;
        }

    }

    private void GetallDepartmentInfo(){

        new Thread(GetDepartmentInfoThread).start();
    }


    private void DeleteSubject(){
        pdu.showpd();
        new Thread(DeleteDepartmentThread).start();
    }

    Runnable GetDepartmentInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONArray jo = DepartmentUtil.selectDepartment(spu.getCidNum(),spu.getDidNum());
                if(jo != null){
                    departmentlist = jo;
                    deapetmentlistback.sendEmptyMessage(1);
                }else{
                    deapetmentlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                deapetmentlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


    Runnable DeleteDepartmentThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new DepartmentUtil().deleteDepartment(spu.getDidNum(),spu.getUidNum());
                if(jo != null){
                    deapetmentlist = jo;
                    deapetmentlistback.sendEmptyMessage(2);
                }else{
                    deapetmentlistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                deapetmentlistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
