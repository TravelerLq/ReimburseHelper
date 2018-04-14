package com.sas.rh.reimbursehelper.Adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.sas.rh.reimbursehelper.Bean.ApproveNum;
import com.sas.rh.reimbursehelper.Bean.Company;
import com.sas.rh.reimbursehelper.R;

import java.util.List;


/**
 * Created by Administrator on 2018/1/23 0023.
 * 审批配置列表
 */

public class SelectCompanyAdapter extends BaseQuickAdapter<Company, BaseViewHolder> {
    @Override
    protected void convert(BaseViewHolder helper, Company item) {
        helper.setText(R.id.tv_company_name, item.getCompanyName());

    }


    public SelectCompanyAdapter(@Nullable List<Company> data) {
        super(R.layout.item_select_company, data);
    }

//    @Override
//    protected void convert(BaseViewHolder baseViewHolder, CompanyInfoModifyBean companyInfoModifyBean) {
//        baseViewHolder.setText(R.id.basicTxt, "经验的数据");
//    }
}
