package com.sas.rh.reimbursehelper.Adapter.newadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.newbean.ApprovalBean;
import com.sas.rh.reimbursehelper.Bean.newbean.DepartmentBean;
import com.sas.rh.reimbursehelper.R;

import java.util.List;

/**
 * Created by liqing on 18/3/20.
 * 部门管理adapter
 */

public class DepartmentRecycleAdapter extends RecyclerView.Adapter<DepartmentRecycleAdapter.MyViewHolder> {

    private Context context;
    private List<DepartmentBean> list;
    private LayoutInflater inflater;

    public DepartmentRecycleAdapter(Context context, List<DepartmentBean> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(this.context);
    }

    public static interface OnItemClickListener {
        void OnItemClick(View view, int pos);

        void OnMoreClick(View view, int pos);
    }

    public DepartmentRecycleAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(DepartmentRecycleAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = null;
        if (myViewHolder == null) {
            myViewHolder = new MyViewHolder(inflater.inflate(R.layout.item_department_manage, parent, false));

        }
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DepartmentBean bean = list.get(position);
        if (bean != null) {
            Log.e("holder.tvTitle", "not null");
            holder.tvDepartName.setText(bean.getDname());
            holder.tvManageName.setText(bean.getName());
            holder.tvNum.setText(String.valueOf(bean.getNum())+"人");
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

        TextView tvDepartName;
        TextView tvManageName;
        TextView tvNum;
        TextView tvMore;
        RelativeLayout rlItem;
        View view;

        public MyViewHolder(View view) {
            super(view);
            tvDepartName = view.findViewById(R.id.tv_depart_name);
            tvManageName = view.findViewById(R.id.tv_manager);
            tvNum = view.findViewById(R.id.tv_depart_num);
            rlItem = view.findViewById(R.id.rl_item_depart);
            tvMore = view.findViewById(R.id.tv_more);
            this.view = view;
        }
    }
}


