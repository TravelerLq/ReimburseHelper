package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.os.Handler;
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

import com.sas.rh.reimbursehelper.Entity.DepartmentDetailInfo;
import com.sas.rh.reimbursehelper.NetUtil.BumenUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApproveProcedureManageActivity extends AppCompatActivity {

    private ImageView backbt;
    private RecyclerView approve_recycler_view;
    private HomeAdapter mAdapter;
    private List<DepartmentDetailInfo> mList = new ArrayList<DepartmentDetailInfo>();
    private int count = 0;
    private ProgressDialogUtil pdu =new ProgressDialogUtil(ApproveProcedureManageActivity.this,"提示","提交更改中");
    String department_id;
    private JSONObject deapetmentlist;
    private JSONObject deapetmentalterrs;
    private Handler deapetmentlistback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if(pdu.getMypDialog() != null){
                pdu.dismisspd();
            }
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                mList.clear();
                if (deapetmentlist.get("resultList")!= null) {
                    //System.out.print("resultList:");
                    JSONArray jsonArray = deapetmentlist.getJSONArray("resultList");
                    for (Object object : jsonArray) {
                        JSONObject jObject = JSONObject.fromObject(object);
                        DepartmentDetailInfo dma = new DepartmentDetailInfo();
                        dma.setDepartment_name(jObject.get("bmName").toString());
                        dma.setDepartment_id(jObject.get("bmId").toString());
                        dma.setDepartment_gsid(jObject.get("gongsiId").toString());
                        dma.setDepartment_state(jObject.get("isOpen").toString());
                        mList.add(dma);
                        //System.out.println(jObject);
                    }
                }
                mAdapter.notifyDataSetChanged();
                ToastUtil.showToast(ApproveProcedureManageActivity.this,deapetmentlist.get("HostTime")+":"+deapetmentlist.get("Note").toString(), Toast.LENGTH_LONG);

            }else if(msg.what == 2){
                ToastUtil.showToast(ApproveProcedureManageActivity.this,deapetmentlist.get("HostTime")+":"+deapetmentlist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            }else if(msg.what == 0){
                ToastUtil.showToast(ApproveProcedureManageActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(ApproveProcedureManageActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_procedure_manage);
        backbt = (ImageView)findViewById(R.id.backbt) ;
        approve_recycler_view = (RecyclerView)findViewById(R.id.approve_recycler_view) ;


        //initData();

        approve_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new HomeAdapter();
        approve_recycler_view.setAdapter(mAdapter);
        GetAllDepartment();

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

//    private void initData() {
//
//        mList.add(new ApproveDepartmentEntity("1111111111","技术部","1"));
//        mList.add(new ApproveDepartmentEntity("2222222222","财务部","1"));
//        mList.add(new ApproveDepartmentEntity("3333333333","物流部","1"));
//    }


    class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder>
    {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    ApproveProcedureManageActivity.this).inflate(R.layout.item_approve_selectdm_list, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position)
        {
            holder.tv.setText(mList.get(position).getDepartment_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it =new Intent(ApproveProcedureManageActivity.this,ApproveProcedureManageConfigActivity.class);
                    it.putExtra("depatment_ID", mList.get(position).getDepartment_id());
                    it.putExtra("department_Name", mList.get(position).getDepartment_name());
                    startActivityForResult(it,1);
                    //System.out.println("*********TEST ECHO********:"+mList.get(position).getDepatment_ID());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    return false;
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return mList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView tv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.dm_name);
            }
        }
    }

    private void GetAllDepartment(){
        pdu.showpd();
        new Thread(GetAllDepartmentInfoThread).start();
    }

    Runnable GetAllDepartmentInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new BumenUtils().queryByGongsiId(1);
                if(jo != null){
                    deapetmentlist = jo;
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

}
