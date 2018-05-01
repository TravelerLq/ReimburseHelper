package com.sas.rh.reimbursehelper.newactivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.Bean.newbean.ApprovalAllDetailBean;
import com.sas.rh.reimbursehelper.NetworkUtil.StrTobaseUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;

/**
 * Created by liqing on 18/4/28.
 */

public class ViewImageActivity extends BaseActivity {
    ImageView ivViewPic;
    private ApprovalAllDetailBean.SingleReimVoAppArrayListBean bean;
    private TextView tvTilte;
    private ImageView ivBack;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_view_image;
    }

    @Override
    protected void initData() {
        ivViewPic = (ImageView) findViewById(R.id.iv_view_big_pic);
//        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
//        ivBack = (ImageView) findViewById(R.id.iv_back);
        //   tvTilte.setText("查看大图");

        if (getIntent() != null) {
//            String data = getIntent().getStringExtra("data");
//            Bitmap bitmap = StrTobaseUtil.base64ToBitmap(data);
//            ivViewPic.setImageBitmap(bitmap);
            Bundle bundle = getIntent().getExtras();
            bean = (ApprovalAllDetailBean.SingleReimVoAppArrayListBean) bundle.getSerializable("itemBean");
            Bitmap bitmap = StrTobaseUtil.base64ToBitmap(bean.getFileData());
            Loger.e("view base64---" + bean.getFileData());
            ivViewPic.setImageBitmap(bitmap);
        } else {
            Loger.i("getInntet =null ");
        }

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            bean = (ApprovalAllDetailBean.SingleReimVoAppArrayListBean) bundle.getSerializable("itemBean");
//            Bitmap bitmap = StrTobaseUtil.base64ToBitmap(bean.getFileData());
//            Loger.e("view base64---"+bean.getFileData());
//            ivViewPic.setImageBitmap(bitmap);
//
//            //  ivViewPic.setImageBitmap(bean.getBitmap());
//        } else {
//            Loger.i("bean = " + (bean == null));
//        }


    }

    @Override
    protected void initListeners() {
        // ivBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
            default:
                break;
        }
    }
}
