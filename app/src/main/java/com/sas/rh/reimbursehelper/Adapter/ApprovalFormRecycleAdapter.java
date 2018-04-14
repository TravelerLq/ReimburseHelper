package com.sas.rh.reimbursehelper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.ResponseApprovalDetailBean;
import com.sas.rh.reimbursehelper.R;
import com.warmtel.expandtab.KeyValueBean;

import java.util.List;

/**
 * Created by liqing on 18/3/20.
 * 审批 报销单下的报销项目列表 adapter
 */

public class ApprovalFormRecycleAdapter extends RecyclerView.Adapter<ApprovalFormRecycleAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<ResponseApprovalDetailBean> list;
    private LayoutInflater inflater;
    private View view;

    public ApprovalFormRecycleAdapter(Context context, List<ResponseApprovalDetailBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            if (view.getTag() == null) {
                Log.e("---getTag=", "null");
            }
            listener.OnItemClick(view, (int) view.getTag());

        }

    }

    public static interface OnItemClickListener {
        void OnItemClick(View view, int pos);
    }

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        view = inflater.inflate(R.layout.item_form_item, parent, false);
        if (myViewHolder == null) {
            myViewHolder = new MyViewHolder(view);

        }
        view.setOnClickListener(this);
        return myViewHolder;
    }

    public void refresh(List<ResponseApprovalDetailBean> datas) {
        this.list.clear();
        this.list.addAll(datas);
        for (int i = 0; i < list.size(); i++) {
            Log.e("approval adapter i=", "" + i + list.get(i).getExpenseItem());
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        view.setTag(position);
        ResponseApprovalDetailBean bean = list.get(position);
        if (bean != null) {
            if (holder.tvFee == null) {
                Log.e("holder.tvTitle", "null");
            } else {
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

        TextView tvFee;
        TextView tvRemark;

        View view;

        public MyViewHolder(View view) {
            super(view);
            tvFee = view.findViewById(R.id.tv_detail_fee);
            tvRemark = view.findViewById(R.id.tv_detail_remark);
            this.view = view;
        }
    }
}


