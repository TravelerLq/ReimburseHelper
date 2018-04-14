package com.sas.rh.reimbursehelper.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sas.rh.reimbursehelper.Bean.ApproveNum;
import com.sas.rh.reimbursehelper.R;

import java.util.List;


/**
 * Created by Administrator on 2018/1/23 0023.
 * 审批配置列表
 */

public class ApprovalProducerAdapter extends BaseQuickAdapter<ApproveNum, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, ApproveNum item) {
        helper.setText(R.id.tv_approval_name, item.getApproveNumName());
        helper.setText(R.id.tv_process_no,String.valueOf(item.getApproveNum()));
        helper.setText(R.id.tv_process_name,String.valueOf(item.getApproverId()));
    }


    public ApprovalProducerAdapter(@Nullable List<ApproveNum> data) {
        super(R.layout.item_approval_process, data);
    }

//    @Override
//    protected void convert(BaseViewHolder baseViewHolder, CompanyInfoModifyBean companyInfoModifyBean) {
//        baseViewHolder.setText(R.id.basicTxt, "经验的数据");
//    }
}
