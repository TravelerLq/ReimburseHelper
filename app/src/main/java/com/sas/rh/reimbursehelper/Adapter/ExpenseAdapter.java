package com.sas.rh.reimbursehelper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.ExpenseApprovalResponseBean;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.DateUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by liqing on 18/3/26.
 * 查看报销列表的Adapter
 */

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ChildViewHolder> implements View.OnClickListener {

    private List<ExpenseApprovalResponseBean> datas;
    private LayoutInflater inflater;

    public ExpenseAdapter(Context context, List<ExpenseApprovalResponseBean> datas) {
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void onClick(View view) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClickListener(view, (int) view.getTag());
        }
    }

    public static interface OnItemClickListener {
        void onItemClickListener(View view, int pos);
    }

    ;
    public OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListner(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;

    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_msglist_layout, parent, false);
        ChildViewHolder viewHolder = null;
        if (viewHolder == null) {
            viewHolder = new ChildViewHolder(view);
        }
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChildViewHolder holder, int position) {
        holder.llExpenseItem.setTag(position);
        ExpenseApprovalResponseBean approvalBean = datas.get(position);
        if (approvalBean != null) {
            if (!TextUtils.isEmpty(approvalBean.getApprovalName())) {
                holder.tvTitle.setText(approvalBean.getApprovalName().toString());
                holder.tvDepart.setText(String.valueOf(approvalBean.getApprovalId()));
                Date date = approvalBean.getUpdateTime();
                String dateStr = DateUtils.parse(date);
                holder.tvDate.setText(dateStr);


            }
        }
    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        View view;
        LinearLayout llExpenseItem;
        private TextView tvDepart, tvDate;


        public ChildViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            llExpenseItem = (LinearLayout) view.findViewById(R.id.ll_expense_item);
            tvDepart = (TextView) view.findViewById(R.id.tv_depart);
            tvDate = (TextView) view.findViewById(R.id.tv_approval_date);

            this.view = view;
        }

    }
}
