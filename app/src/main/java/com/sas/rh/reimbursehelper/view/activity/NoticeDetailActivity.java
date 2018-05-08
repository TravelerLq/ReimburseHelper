package com.sas.rh.reimbursehelper.view.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.UnreadNoticeBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ApprovalUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.CompanyUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;

/**
 * Created by liqing on 18/5/7.
 */

public class NoticeDetailActivity extends BaseActivity {
    private TextView tvMsgTitle, tvContent, tvDate, tvTitle;
    private ImageView ivBack;
    private int noticeId;
    private int userId;
    JSONObject jsonobj;
    private SharedPreferencesUtil spu;
    private Context context;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                int status = jsonobj.getIntValue("status");
                if (status == 200) {
                    Loger.e("---yidu");
                }
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice_detail;
    }

    @Override
    protected void initData() {
        context = NoticeDetailActivity.this;
        spu = new SharedPreferencesUtil(context);
        userId = spu.getUidNum();
        tvMsgTitle = (TextView) findViewById(R.id.tv_title);
        tvDate = (TextView) findViewById(R.id.tv_time);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvTitle.setText("消息详情");
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (getIntent() != null) {
//            intent.putExtra("title", bean.getTitle());
//            intent.putExtra("content", bean.getContent());
//            intent.putExtra("date", bean.getDate());
            String noticeIdStr = getIntent().getStringExtra("id");
            noticeId = Integer.valueOf(noticeIdStr);
            Loger.e("---bean=id" + noticeId);
            tvMsgTitle.setText(getIntent().getStringExtra("title"));
            tvContent.setText(getIntent().getStringExtra("content"));
            tvDate.setText(getIntent().getStringExtra("date"));

//                tvContent.setText(bean.getContent());
//                tvDate.setText(bean.getDate());
//                noticeId = bean.getNoticeId();

//            UnreadNoticeBean bean = (UnreadNoticeBean) getIntent().getSerializableExtra("itemNotice");
//            if (bean != null) {
//
//                tvMsgTitle.setText(bean.getTitle());
//                tvContent.setText(bean.getContent());
//                tvDate.setText(bean.getDate());
//                noticeId = bean.getNoticeId();
//            } else {
//                Loger.e("---bean=null");
//            }
        }
        if (noticeId != 0) {
            setNoticeRead();
        }


    }

    private void setNoticeRead() {
        new Thread(noticeReadRunnable).start();
    }

    @Override
    protected void initListeners() {

    }


    Runnable noticeReadRunnable = new Runnable() {
        @Override
        public void run() {

            try {


                JSONObject jo = CompanyUtil.readNotice( userId,noticeId);
                if (jo != null) {
                    jsonobj = jo;
                    //  expenseId = jo.getIntValue("expenseId");

                    //  new Thread(SubmitfileThread).start();
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(0);
                }

            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }


    };

    @Override
    public void onClick(View view) {

    }
}
