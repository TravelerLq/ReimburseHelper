package com.sas.rh.reimbursehelper.Adapter.newadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.newbean.ApprovalAllDetailBean;
import com.sas.rh.reimbursehelper.Bean.newbean.ExpenseItemBean;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.newactivity.ViewImageActivity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * Created by liqing on 18/3/20.
 * 添加报销 －记一笔的Adapter
 */

public class ApprovalDetailRecycleAdapter extends RecyclerView.Adapter<ApprovalDetailRecycleAdapter.MyViewHolder> {

    private Context context;
    private List<ApprovalAllDetailBean.SingleReimVoAppArrayListBean> list;
    private LayoutInflater inflater;
    private List<Bitmap> listBitmap = new ArrayList<>();

    public ApprovalDetailRecycleAdapter(Context context, List<ApprovalAllDetailBean.SingleReimVoAppArrayListBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    public void setListBitmap(List<Bitmap> list) {
        this.listBitmap = list;

    }

    public static interface OnItemClickListener {
        //  void OnItemClick(View view, int pos);
        void onPicCLick(View view, int pos);
    }

    //    public static interface OnItemClickListener {
//        void OnItemClick(View view, int pos);
//    }
    public ApprovalDetailRecycleAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(ApprovalDetailRecycleAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        if (myViewHolder == null) {
            myViewHolder = new MyViewHolder(inflater.inflate(R.layout.item_expense, parent, false));

        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ApprovalAllDetailBean.SingleReimVoAppArrayListBean bean = list.get(position);
        if (bean != null) {
            if (holder.tvExplain == null) {
                Log.e("holder.tvTitle", "null");
            } else {

                holder.tvFee.setText(bean.getMoney());
                holder.tvExplain.setText(bean.getRemark());
                holder.tvDate.setText(bean.getDate());
                holder.tvType.setText(bean.getName());
                holder.rlItem.setTag(position);
                holder.ivPic.setTag(position);
                holder.ivPic.setImageBitmap(listBitmap.get(position));
                holder.ivPic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.onPicCLick(view, (int) view.getTag());
                        }
                        //bean.setBitmap(listBitmap.get(position));

//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("itemBean", bean);
//                        Intent intent = new Intent(context, ViewImageActivity.class);
//                        intent.putExtras(bundle);
////                        Intent intent = new Intent(context, ViewImageActivity.class);
////                        intent.putExtra("data", bean.getFileData());
//                        context.startActivity(intent);

                    }
                });

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

        TextView tvFee;
        TextView tvDate;
        TextView tvType;
        TextView tvExplain;
        RelativeLayout rlItem;
        ImageView ivPic;
        View view;

        public MyViewHolder(View view) {
            super(view);
            tvFee = view.findViewById(R.id.tv_money);
            tvDate = view.findViewById(R.id.tv_expense_time);
            tvType = view.findViewById(R.id.tv_expense_category);
            tvExplain = view.findViewById(R.id.tv_explain);
            rlItem = view.findViewById(R.id.rl_expense_item);
            ivPic = view.findViewById(R.id.iv_expense_item_pic);
            this.view = view;
        }
    }
}


