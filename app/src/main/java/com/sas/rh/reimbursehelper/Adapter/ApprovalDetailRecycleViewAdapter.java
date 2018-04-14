package com.sas.rh.reimbursehelper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.ResponseApprovalDetailBean;
import com.sas.rh.reimbursehelper.R;

import java.util.List;

/**
 * Created by liqing on 18/3/20.
 * 添加报销 －记一笔的Adapter
 */

public class ApprovalDetailRecycleViewAdapter extends RecyclerView.Adapter<ApprovalDetailRecycleViewAdapter.MyViewHolder> {

    private Context context;
    private List<ResponseApprovalDetailBean> list;
    private LayoutInflater inflater;

    public ApprovalDetailRecycleViewAdapter(Context context, List<ResponseApprovalDetailBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        if (myViewHolder == null) {
            myViewHolder = new MyViewHolder(inflater.inflate(R.layout.item_form_item, parent, false));

        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ResponseApprovalDetailBean bean = list.get(position);
        if (bean != null) {
            if (holder.tvTitle == null) {
                Log.e("holder.tvTitle", "null");
            } else {
                // holder.tvTitle.setText(bean.get().toString());
                holder.tvFee.setText(String.valueOf(bean.getAmount()).toString());
                holder.tvRemark.setText("报销备注：" + bean.getRemark().toString());
                //photoAdapter.notifyDataSetChanged();
//                Uri uri = Uri.fromFile(new File(bean.getPicStr()));
//
//                Glide.with(context)
//                        .load(uri)
//                        .thumbnail(0.1f)
//                        .into(holder.ivPhoto);
            }

        }

    }

    @Override
    public int getItemCount() {

        if (list == null)
            return 0;
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvFee;
        TextView tvRemark;
        ImageView ivPhoto;
        View view;

        public MyViewHolder(View view) {
            super(view);
            tvFee = view.findViewById(R.id.tv_fee);
            tvRemark = view.findViewById(R.id.tv_remark_content);

            this.view = view;
        }
    }
}


