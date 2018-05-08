package com.sas.rh.reimbursehelper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Adapter.newadapter.AddExpenseRecycleAdapter;
import com.sas.rh.reimbursehelper.Adapter.newadapter.ApprovalDetailRecycleAdapter;
import com.sas.rh.reimbursehelper.Bean.UnreadNoticeBean;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.fragment.MessageFragment;

import java.util.List;

/**
 * Created by liqing on 18/5/7.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ChildViewHolder> {

    private List<UnreadNoticeBean> datas;
    private LayoutInflater inflater;
    private Context context;

    public NoticeAdapter(Context context, List<UnreadNoticeBean> data) {
        super();
        inflater = LayoutInflater.from(context);
        datas = data;
        this.context = context;
    }

    public static interface OnItemClickListener {
        void OnItemClick(View view, int pos);
        // void (View view, int pos);
    }

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(final ChildViewHolder holder, int position) {
        // ChildViewHolder holder = (ChildViewHolder) viewHolder;
        holder.tvTitile.setText(datas.get(position).getTitle());
        holder.tvContent.setText(datas.get(position).getContent());
        holder.tvDate.setText(datas.get(position).getDate());
        Boolean status = datas.get(position).isReadStatus();
        if (status) {
            holder.tvState.setTextColor(context.getResources().getColor(R.color.grey_a3a3a3));
            holder.tvState.setText("已读");

        } else {
            holder.tvState.setTextColor(context.getResources().getColor(R.color.blue));
            holder.tvState.setText("未读");
        }
        holder.rlItme.setTag(position);
        holder.rlItme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.OnItemClick(view, (int) view.getTag());
                }
            }
        });

    }

    @Override
    public ChildViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        // View view = inflater.inflate(R.layout.item_notice, viewHolder, false);
        ChildViewHolder myViewHolder = null;
        if (myViewHolder == null) {
            myViewHolder = new ChildViewHolder(inflater.inflate(R.layout.item_notice, parent, false));

        }
        return myViewHolder;

    }

    public static class ChildViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitile, tvContent, tvState, tvDate;
        public RelativeLayout rlItme;

        public ChildViewHolder(View view) {
            super(view);
            tvTitile = (TextView) view.findViewById(R.id.tv_notice_title);
            tvContent = (TextView) view.findViewById(R.id.tv_notice_content);
            tvState = (TextView) view.findViewById(R.id.tv_state);
            tvDate = (TextView) view.findViewById(R.id.tv_notice_time);
            rlItme = (RelativeLayout) view.findViewById(R.id.rl_item_notice);
        }

    }


}
