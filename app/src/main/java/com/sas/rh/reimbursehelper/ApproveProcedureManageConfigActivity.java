package com.sas.rh.reimbursehelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
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

import com.sas.rh.reimbursehelper.Entity.ApproveConfigItemEntity;
import com.sas.rh.reimbursehelper.Entity.DeparrtmentEntity;

import java.util.ArrayList;
import java.util.List;

public class ApproveProcedureManageConfigActivity extends AppCompatActivity {

    private ImageView backbt;
    private String depatment_ID,department_Name;
    private TextView department_Name_tv;
    private RecyclerView approveconfig_recycler_view;
    private ApproveProcedureManageConfigActivity.HomeAdapter mAdapter;
    private List<ApproveConfigItemEntity> mList = new ArrayList<ApproveConfigItemEntity>();
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_procedure_manage_config);
        Intent getIntent = getIntent();
        depatment_ID = getIntent.getStringExtra("depatment_ID");
        department_Name = getIntent.getStringExtra("department_Name");
        backbt = (ImageView)findViewById(R.id.backbt) ;
        department_Name_tv = (TextView)findViewById(R.id.department_Name_tv) ;
        approveconfig_recycler_view = (RecyclerView)findViewById(R.id.approveconfig_recycler_view) ;
        initData();
        approveconfig_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ApproveProcedureManageConfigActivity.HomeAdapter();
        approveconfig_recycler_view.setAdapter(mAdapter);
        if(department_Name != null){
            department_Name_tv.setText(""+department_Name);
        }

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initData() {
        mList.add(new ApproveConfigItemEntity("丁总","财务"));
        mList.add(new ApproveConfigItemEntity("丁总","出纳"));
    }

    //在列表里添加一个
    public void add(int position){
        count++;
        mList.add(position,new ApproveConfigItemEntity("丁总","审批人"));
        mAdapter.notifyItemInserted(position);
        mAdapter.notifyItemRangeChanged(position,mList.size()-position);
    }

    //在列表里删除一个
    public void delete(int position){
        mList.remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position,mList.size()-position);
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
            holder.addapprover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add(position);
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


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
}
