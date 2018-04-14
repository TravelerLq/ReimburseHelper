package com.sas.rh.reimbursehelper.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sas.rh.reimbursehelper.Bean.ExpenseItemBean;
import com.sas.rh.reimbursehelper.R;
import com.warmtel.expandtab.KeyValueBean;

import java.io.File;
import java.util.List;

/**
 * Created by liqing on 18/3/20.
 * 三级目录 adapter
 */

public class ThirdTypeAdapter extends RecyclerView.Adapter<ThirdTypeAdapter.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<KeyValueBean> list;
    private LayoutInflater inflater;

    public ThirdTypeAdapter(Context context, List<KeyValueBean> list) {
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
        View view = inflater.inflate(R.layout.item_third_expense_type, parent, false);
        if (myViewHolder == null) {
            myViewHolder = new MyViewHolder(view);

        }
        myViewHolder.tvTitle.setOnClickListener(this);
        return myViewHolder;
    }

    public void refresh(List<KeyValueBean> datas) {
        this.list.clear();
        this.list.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTitle.setTag(position);
        KeyValueBean bean = list.get(position);
        if (bean != null) {
            if (holder.tvTitle == null) {
                Log.e("holder.tvTitle", "null");
            } else {
                holder.tvTitle.setText(bean.getValue().toString());

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

        View view;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_type_name);

            this.view = view;
        }
    }
}


