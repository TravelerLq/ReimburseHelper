package com.sas.rh.reimbursehelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Entity.DeparrtmentEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApproveProcedureManageActivity extends AppCompatActivity {

    private ImageView backbt;
    private RecyclerView approve_recycler_view;
    private HomeAdapter mAdapter;
    private List<DeparrtmentEntity> mList = new ArrayList<DeparrtmentEntity>();
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_procedure_manage);
        backbt = (ImageView)findViewById(R.id.backbt) ;
        approve_recycler_view = (RecyclerView)findViewById(R.id.approve_recycler_view) ;


        initData();

        approve_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new HomeAdapter();
        approve_recycler_view.setAdapter(mAdapter);

        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {

        mList.add(new DeparrtmentEntity("1111111111","技术部","1"));
        mList.add(new DeparrtmentEntity("2222222222","财务部","1"));
        mList.add(new DeparrtmentEntity("3333333333","物流部","1"));
    }


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
            holder.tv.setText(mList.get(position).getDepartment_Name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it =new Intent(ApproveProcedureManageActivity.this,ApproveProcedureManageConfigActivity.class);
                    it.putExtra("depatment_ID", mList.get(position).getDepatment_ID());
                    it.putExtra("department_Name", mList.get(position).getDepartment_Name());
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

}
