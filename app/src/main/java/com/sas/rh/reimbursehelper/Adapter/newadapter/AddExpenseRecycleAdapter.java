package com.sas.rh.reimbursehelper.Adapter.newadapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sas.rh.reimbursehelper.Adapter.ThirdTypeAdapter;
import com.sas.rh.reimbursehelper.Bean.newbean.ExpenseItemBean;
import com.sas.rh.reimbursehelper.R;

import java.io.File;
import java.util.List;

/**
 * Created by liqing on 18/3/20.
 * 添加报销 －记一笔的Adapter
 */

public class AddExpenseRecycleAdapter extends RecyclerView.Adapter<AddExpenseRecycleAdapter.MyViewHolder> {

    private Context context;
    private List<ExpenseItemBean> list;
    private LayoutInflater inflater;

    public AddExpenseRecycleAdapter(Context context, List<ExpenseItemBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    public static interface OnItemClickListener {
        void OnItemClick(View view, int pos);
    }

    public AddExpenseRecycleAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(AddExpenseRecycleAdapter.OnItemClickListener listener) {
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ExpenseItemBean bean = list.get(position);
        if (bean != null) {
            if (holder.tvExplain == null) {
                Log.e("holder.tvTitle", "null");
            } else {

                holder.tvFee.setText("¥" + bean.getMoney());
                holder.tvExplain.setText(bean.getRemark());
                holder.tvDate.setText(bean.getDate());
                holder.tvType.setText(bean.getName());
                holder.rlItem.setTag(position);
                holder.rlItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.OnItemClick(view, (int) view.getTag());
                        }
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
        View view;

        public MyViewHolder(View view) {
            super(view);
            tvFee = view.findViewById(R.id.tv_money);
            tvDate = view.findViewById(R.id.tv_expense_time);
            tvType = view.findViewById(R.id.tv_expense_category);
            tvExplain = view.findViewById(R.id.tv_explain);
            rlItem = view.findViewById(R.id.rl_expense_item);
            this.view = view;
        }
    }
}


