package com.sas.rh.reimbursehelper.Adapter.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.newbean.ApprovalSettingtBean;
import com.sas.rh.reimbursehelper.Bean.newbean.DepartmentBean;
import com.sas.rh.reimbursehelper.R;

import java.util.List;

import io.fabric.sdk.android.services.settings.AppSettingsData;

/**
 * Created by liqing on 18/3/20.
 * 审核配置dapter
 */

public class ApprovalSetRecycleAdapter extends RecyclerView.Adapter<ApprovalSetRecycleAdapter.MyViewHolder> {

    private Context context;
    private List<ApprovalSettingtBean> list;
    private LayoutInflater inflater;

    public ApprovalSetRecycleAdapter(Context context, List<ApprovalSettingtBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    public static interface OnItemClickListener {
        void OnItemClick(View view, int pos);

        void OnMoreClick(View view, int pos);
    }

    public ApprovalSetRecycleAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(ApprovalSetRecycleAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        if (myViewHolder == null) {
            myViewHolder = new MyViewHolder(inflater.inflate(R.layout.item_approval_setting, parent, false));

        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ApprovalSettingtBean bean = list.get(position);
        if (bean != null) {
            Log.e("holder.tvTitle", "not null");
            holder.tvSettingName.setText(bean.getDname());
            holder.tvApprovalName.setText(bean.getName());
            holder.tvLevel.setText(String.valueOf(bean.getNum()) + "级");
            holder.tvMore.setTag(position);
            holder.rlItem.setTag(position);
            holder.tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.OnMoreClick(view, (int) view.getTag());
                    }
                }
            });
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

    @Override
    public int getItemCount() {

        if (list == null)
            return 0;
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvSettingName;
        TextView tvLevel;
        TextView tvApprovalName;
        TextView tvMore;
        RelativeLayout rlItem;
        View view;

        public MyViewHolder(View view) {
            super(view);
            tvSettingName = view.findViewById(R.id.tv_setting_name);
            tvLevel = view.findViewById(R.id.tv_level);
            tvApprovalName = view.findViewById(R.id.tv_approval_namne);
            rlItem = view.findViewById(R.id.rl_item_depart);
            tvMore = view.findViewById(R.id.tv_more);
            this.view = view;
        }
    }
}


