package com.sas.rh.reimbursehelper.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.Entity.ApproveConfigItemEntity;
import com.sas.rh.reimbursehelper.NetUtil.ShlcUtils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApproveProcedureManageConfigActivity extends AppCompatActivity {

    private ImageView backbt;
    //private String depatment_ID,department_Name;
    private TextView department_Name_tv;
    private RecyclerView approveconfig_recycler_view;
    private ApproveProcedureManageConfigActivity.HomeAdapter mAdapter;
    private List<ApproveConfigItemEntity> mList = new ArrayList<ApproveConfigItemEntity>();
    private LinearLayout savebt;
    private int count = 0;
    private String depatment_ID;
    private String department_Name;
    private String approve_member;
    private String approve_num;
    private ProgressDialogUtil pdu =new ProgressDialogUtil(ApproveProcedureManageConfigActivity.this,"提示","提交更改中");
    private JSONObject approvelist;
    private JSONObject approvealterrs;
    private Handler approvelistback = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {
            if(pdu.getMypDialog() != null){
                pdu.dismisspd();
            }
            if(msg.what == 1){
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                mList.clear();
                if (approvelist.get("resultList")!= null) {
                    //System.out.print("resultList:");
                    JSONArray jsonArray = approvelist.getJSONArray("resultList");
                    for (Object object : jsonArray) {
                        JSONObject jObject = JSONObject.fromObject(object);
                        ApproveConfigItemEntity aci = new ApproveConfigItemEntity();
                        aci.setApprover_ID_tv(jObject.get("shlcMember").toString());
                        aci.setApprover_Name_tv(jObject.get("shlcMemberName").toString().trim());
                        aci.setJob_Name_tv("审批人");
                        aci.setApprove_num(Integer.parseInt(jObject.get("shlcYouxianji").toString().trim()));
                        mList.add(aci);
                        //System.out.println(jObject);
                    }
                }else{
                    mList.add(new ApproveConfigItemEntity("请选择审批人",null,"财务"));
                    mList.add(new ApproveConfigItemEntity("请选择审批人",null,"出纳"));
                }
                mAdapter.notifyDataSetChanged();
                ToastUtil.showToast(ApproveProcedureManageConfigActivity.this,approvelist.get("HostTime")+":"+approvelist.get("Note").toString(), Toast.LENGTH_LONG);

            }else if(msg.what == 2){
                ToastUtil.showToast(ApproveProcedureManageConfigActivity.this,approvelist.get("HostTime")+":"+approvelist.get("ResultCode").toString(), Toast.LENGTH_LONG);
            }else if(msg.what == 0){
                ToastUtil.showToast(ApproveProcedureManageConfigActivity.this,"通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            }else if(msg.what == -1){
                ToastUtil.showToast(ApproveProcedureManageConfigActivity.this,"通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_procedure_manage_config);
        Intent getIntent = getIntent();
        depatment_ID = getIntent.getStringExtra("depatment_ID");
        department_Name = getIntent.getStringExtra("department_Name");
        backbt = (ImageView)findViewById(R.id.backbt) ;
        department_Name_tv = (TextView)findViewById(R.id.department_Name_tv) ;
        savebt = (LinearLayout) findViewById(R.id.savebt) ;
        approveconfig_recycler_view = (RecyclerView)findViewById(R.id.approveconfig_recycler_view) ;
        //initData();
        approveconfig_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ApproveProcedureManageConfigActivity.HomeAdapter();
        approveconfig_recycler_view.setAdapter(mAdapter);
        if(department_Name != null){
            department_Name_tv.setText(""+department_Name);
        }
        GetAllDepartment();

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        savebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
//    private void initData() {
//        mList.add(new ApproveConfigItemEntity("请选择审批人",null,"财务"));
//        mList.add(new ApproveConfigItemEntity("请选择审批人",null,"出纳"));
//    }

    public void toselectapper(int position){
        Intent it = new Intent(ApproveProcedureManageConfigActivity.this,ApproveProcedureManageConfigAddApproverActivity.class);
        it.putExtra("position", position);
        startActivityForResult(it,1);
    }

    //在列表里添加一个
    public void add(int position){
        //count++;
        mList.add(position,new ApproveConfigItemEntity("请选择审批人",null,"审批人"));
        mAdapter.notifyItemInserted(position);
        mAdapter.notifyItemRangeChanged(position,mList.size()-position);
    }

    //在列表里删除一个
    public void delete(int position){
        mList.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position,mList.size()-position);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null){
            int position = data.getExtras().getInt("position");
            if(position == -1){
                return;
            }
            mList.get(position).setApprover_Name_tv(data.getExtras().getString("approver_name"));
            mList.get(position).setApprover_ID_tv(data.getExtras().getString("approver_id"));
            mAdapter.notifyDataSetChanged();
            //mAdapter.notifyItemRemoved(position);
            //mAdapter.notifyItemRangeChanged(position,mList.size()-position);
            //System.out.println(position+":"+data.getExtras().getString("approver_name")+":"+data.getExtras().getString("approver_id"));
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<ApproveProcedureManageConfigActivity.HomeAdapter.MyViewHolder>
    {

        @Override
        public ApproveProcedureManageConfigActivity.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            ApproveProcedureManageConfigActivity.HomeAdapter.MyViewHolder holder = new ApproveProcedureManageConfigActivity.HomeAdapter.MyViewHolder(LayoutInflater.from(
                    ApproveProcedureManageConfigActivity.this).inflate(R.layout.item_add_approver, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ApproveProcedureManageConfigActivity.HomeAdapter.MyViewHolder holder, final int position)
        {
            //holder.tv.setText(mList.get(position).getDepartment_Name());
            holder.job_Name_tv.setText(mList.get(position).getJob_Name_tv());
            holder.approver_Name_tv.setText(mList.get(position).getApprover_Name_tv());
            holder.approver_Number_tv.setText(""+(position+1));
            //holder.approver_Number_tv.setText(""+mList.get(position).getApprove_num());
            holder.addapprover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add(position);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toselectapper(position);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    if(mList.get(position).getJob_Name_tv().equals("财务")||
                       mList.get(position).getJob_Name_tv().equals("出纳")){
                        new AlertDialog.Builder(ApproveProcedureManageConfigActivity.this)
                                .setMessage("不能删除 "+mList.get(position).getJob_Name_tv())
                                .setPositiveButton("确定", null).show();
                        return false;

                    }

                    new AlertDialog.Builder(ApproveProcedureManageConfigActivity.this)
                            .setMessage("确认删除当前第"+(position+1)+"审批对象？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delete(position);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            }).show();

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

            private TextView job_Name_tv;
            private TextView approver_Name_tv;
            private TextView approver_Number_tv;
            private ImageView addapprover;

            public MyViewHolder(View view)
            {
                super(view);
                job_Name_tv = (TextView) view.findViewById(R.id.job_Name_tv);
                approver_Name_tv = (TextView) view.findViewById(R.id.approver_Name_tv);
                approver_Number_tv = (TextView) view.findViewById(R.id.approver_Number_tv);
                addapprover = (ImageView) view.findViewById(R.id.addapprover);
            }
        }
    }

    private void GetAllDepartment(){
        pdu.showpd();
        new Thread(GetAllApproveInfoThread).start();
    }

    private void UpdateApprove(){
        pdu.showpd();
        new Thread(UpdateApproveInfoThread).start();
    }

    private void CreateApprove(){
        pdu.showpd();
        new Thread(CreateApproveInfoThread).start();
    }

    Runnable GetAllApproveInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new ShlcUtils().getShlc(Integer.parseInt(depatment_ID.trim()));
                if(jo != null){
                    approvelist = jo;
                    approvelistback.sendEmptyMessage(1);
                }else{
                    approvelistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                approvelistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable UpdateApproveInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new ShlcUtils().update(1,Integer.parseInt(depatment_ID.trim()),Integer.parseInt(approve_member.trim()),Integer.parseInt(approve_num.trim()),1);
                if(jo != null){
                    approvelist = jo;
                    approvelistback.sendEmptyMessage(2);
                }else{
                    approvelistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                approvelistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable CreateApproveInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try{
                JSONObject jo = new ShlcUtils().insert(Integer.parseInt(depatment_ID.trim()),Integer.parseInt(approve_member.trim()),Integer.parseInt(approve_num.trim()),1);
                if(jo != null){
                    approvelist = jo;
                    approvelistback.sendEmptyMessage(2);
                }else{
                    approvelistback.sendEmptyMessage(0);
                }
            }catch(Exception e){
                approvelistback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
}
