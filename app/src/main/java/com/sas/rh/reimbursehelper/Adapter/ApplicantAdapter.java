package com.sas.rh.reimbursehelper.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sas.rh.reimbursehelper.Bean.ApproveNum;
import com.sas.rh.reimbursehelper.Bean.UnreadNoticeBean;
import com.sas.rh.reimbursehelper.R;

import java.util.List;


/**
 * Created by Administrator on 2018/1/23 0023.
 * 申请人员列表 adapter
 */

public class ApplicantAdapter extends BaseQuickAdapter<UnreadNoticeBean, BaseViewHolder> implements View.OnClickListener {
    private Context mContext;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public void onClick(View view) {

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }



    @Override
    protected void convert(BaseViewHolder helper, UnreadNoticeBean item) {
        helper.setText(R.id.tv_msg_title, item.getTitle());
//        helper.setOnClickListener(R.id.tv_applicant_pass, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "pass..", Toast.LENGTH_LONG).show();
//            }
//        });
//        helper.setOnClickListener(R.id.tv_applicant_unpass, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, "unpass..", Toast.LENGTH_LONG).show();
//            }
//        });

    }


    public ApplicantAdapter(Context context, @Nullable List<UnreadNoticeBean> data) {
        super(R.layout.item_applicant, data);
        mContext = context;
    }


//    @Override
//    public void onClick(View v) {
//        if (mOnItemClickListener != null)
//            mOnItemClickListener.onItemClick(v, getLayoutPosition());
//    }

//    @Override
//    protected void convert(BaseViewHolder baseViewHolder, CompanyInfoModifyBean companyInfoModifyBean) {
//        baseViewHolder.setText(R.id.basicTxt, "经验的数据");
//    }
}
