package com.sas.rh.reimbursehelper.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.ReimbursementDepartment;
import com.sas.rh.reimbursehelper.Bean.UnreadNoticeBean;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ls on 2017/7/27 0027.
 */

public class DepartmentRecycleAdapter extends RecyclerView.Adapter<DepartmentRecycleAdapter.myViewHolder> implements View.OnClickListener {
    private List<ReimbursementDepartment> list = new ArrayList<ReimbursementDepartment>();
    private Context context;
    private OnItemClickListener mOnItemClickListener;
    private ReimbursementDepartment bean;
    private static String TAG = "ApplicantRecyclerViewAdapter";

    public DepartmentRecycleAdapter(Context context, List<ReimbursementDepartment> list) {
        this.context = context;
        this.list = list;
    }

    //define interface
    public static interface OnItemClickListener {

        // void onItemClick(View view, int position);

        //取样
        void onPassClick(View view, int position);

        //结果
        void onUnpassClick(View view, int position);


    }


    //设置数据源
    public void SetList(List<ReimbursementDepartment> list) {
        this.list.clear();
        this.list.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            Loger.e("listAdapter.title=" + list.get(i).getDepartmentName());
        }
        notifyDataSetChanged();

    }

    //获取数据源
    public List<ReimbursementDepartment> getList() {
        return this.list;
    }

    //清楚数据源
    public void ClearList() {
        this.list.clear();
        notifyDataSetChanged();
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new myViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_departmentsmanage_list, parent, false));
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        //取出数据
        bean = list.get(position);
        //设置数据
        //车牌
        holder.tvTitle.setText(bean.getDepartmentName());
        holder.btnPass.setOnClickListener(this);
        holder.btnPass.setTag(position);

//        //是否喷漆
//        holder.tv_checkform_item_paint.setText(bean.getRadiationRecord());
//        //核辐射
//        holder.tv_checkform_item_radiation.setText(bean.getSprayRecord());
//        //集装箱号
//        holder.tv_checkform_item_containerIdNumber.setText(bean.getContainerIdNumber());
//        //卸货状态
//        if (bean.getLoad().equals("true")){
//            holder.tv_checkform_item_state.setText("已卸货");
//            holder.bt_checkform_item_test.setBackgroundResource(R.color.lightGray);
//            holder.bt_checkform_item_test.setClickable(false);
//        }else {
//            holder.tv_checkform_item_state.setText("未卸货");
//            holder.ll_checkform_item_root.setBackgroundResource(R.color.white);
//            holder.bt_checkform_item_test.setClickable(true);
//        }
//
//        if (position % 2 == 1)
//            holder.ll_checkform_item_root.setBackgroundResource(R.color.lightGray);
//        else
//            holder.ll_checkform_item_root.setBackgroundResource(R.color.white);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {

        if (mOnItemClickListener != null) {
            Log.e(TAG, "onClick: ---item listener not null");
            //注意这里使用getTag方法获取position
            // mOnItemClickListener.onItemClick(v, (int) v.getTag());
            switch (v.getId()) {
                case R.id.btn_join:
                    mOnItemClickListener.onPassClick(v, (int) v.getTag());
                    Log.e(TAG, "onClick: ---sampling");
                    break;

                default:
                    break;
            }
        }
        {
            Log.e(TAG, "onClick: ---listener null");
        }


    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        //车牌
        private TextView tvTitle;
        private Button btnPass;
        private Button btnUnPass;

        public myViewHolder(View itemView) {
            super(itemView);
            //单号
            tvTitle = (TextView) itemView.findViewById(R.id.itemTv);
            btnPass = (Button) itemView.findViewById(R.id.btn_join);


        }


    }
}
