package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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

import com.sas.rh.reimbursehelper.Dao.BaoxiaoItem;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.DataHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ReimburseBillCreateActivity extends AppCompatActivity {

    private ImageView backbt;
    private TextView selectprjbt,selectdpbt,sumall;
    private HomeAdapter mAdapter;
    private RecyclerView billlist_recycler_view;
    private double sum = 0.00;
    private List<BaoxiaoItem> bilist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reimburse_bill_create);
        backbt = (ImageView)findViewById(R.id.backbt) ;
        sumall = (TextView)findViewById(R.id.sumall);
        billlist_recycler_view = (RecyclerView)findViewById(R.id.billlist_recycler_view) ;
        initData();
        DecimalFormat df = new DecimalFormat( "#####0.00 ");
        sumall.setText("¥"+df.format(sum)+"(合计)");
        billlist_recycler_view.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeAdapter();
        billlist_recycler_view.setAdapter(mAdapter);
        // System.out.println("+++++++++++++++++:"+billidList.size());
        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        selectdpbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        selectprjbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initData() {
        sum = 0;
        bilist = new ArrayList<BaoxiaoItem>();
        ArrayList<String> billidList = (ArrayList<String>) getIntent().getStringArrayListExtra("billids");
        if(billidList.size() != 0){
            for(int i=0 ;i < billidList.size();i++){
                BaoxiaoItem bi = DataHelper.getABaoxiaoItem(new DataHelper(ReimburseBillCreateActivity.this,"BaoxiaoItem_DB",null,1),billidList.get(i));
                if(bi != null){
                    sum += Double.parseDouble(bi.getSum().trim());
                    bilist.add(bi);
                }
            }
        }
    }

    class HomeAdapter extends RecyclerView.Adapter<ReimburseBillCreateActivity.HomeAdapter.MyViewHolder>
    {

        @Override
        public ReimburseBillCreateActivity.HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            ReimburseBillCreateActivity.HomeAdapter.MyViewHolder holder = new ReimburseBillCreateActivity.HomeAdapter.MyViewHolder(LayoutInflater.from(
                    ReimburseBillCreateActivity.this).inflate(R.layout.item_bxcontent_confirm, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ReimburseBillCreateActivity.HomeAdapter.MyViewHolder holder, final int position)
        {
            holder.tv_bxtype.setText(bilist.get(position).getXflxsp());
            holder.tv_bxdate.setText(bilist.get(position).getDatepicker());
            holder.tv_bxnum.setText(bilist.get(position).getSum());
            if(bilist.get(position).getXflxsp().equals("差旅补助")){
                GradientDrawable myGrad = (GradientDrawable)holder.bxitem_icon.getBackground();
                myGrad.setColor(Color.RED);
                holder.bxitem_text.setText("旅");
            }else if(bilist.get(position).getXflxsp().equals("房租水电")){
                GradientDrawable myGrad = (GradientDrawable)holder.bxitem_icon.getBackground();
                myGrad.setColor(Color.BLUE);
                holder.bxitem_text.setText("房");
            }else if(bilist.get(position).getXflxsp().equals("通讯费用")){
                GradientDrawable myGrad = (GradientDrawable)holder.bxitem_icon.getBackground();
                myGrad.setColor(Color.CYAN);
                holder.bxitem_text.setText("通");
            }else if(bilist.get(position).getXflxsp().equals("采购补贴")){
                GradientDrawable myGrad = (GradientDrawable)holder.bxitem_icon.getBackground();
                myGrad.setColor(Color.GREEN);
                holder.bxitem_text.setText("采");
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(ReimburseBillCreateActivity.this,AddBaoxiaojizhuActivity.class);
                    it.putExtra("rcode",2);
                    it.putExtra("biiid",bilist.get(position).getBillid());
                    startActivityForResult(it,2);
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
            return bilist.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder
        {

            TextView tv_bxtype,tv_bxdate,tv_bxnum,bxitem_text;
            LinearLayout bxitem_icon;

            public MyViewHolder(View view)
            {
                super(view);
                tv_bxtype = (TextView) view.findViewById(R.id.tv_bxtype);
                tv_bxdate = (TextView) view.findViewById(R.id.tv_bxdate);
                tv_bxnum = (TextView) view.findViewById(R.id.tv_bxnum);
                bxitem_text = (TextView) view.findViewById(R.id.bxitem_text);
                bxitem_icon = (LinearLayout) view.findViewById(R.id.bxitem_icon);
            }
        }
    }

}
