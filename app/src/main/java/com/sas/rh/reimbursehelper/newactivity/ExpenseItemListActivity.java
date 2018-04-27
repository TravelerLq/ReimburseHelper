package com.sas.rh.reimbursehelper.newactivity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.Adapter.newadapter.AddExpenseRecycleAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.newbean.ExpenseItemBean;
import com.sas.rh.reimbursehelper.NetworkUtil.DownloadFileUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.FileUtils;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.RecycleViewDivider;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liqing on 18/4/24.
 */

public class ExpenseItemListActivity extends BaseActivity {
    private TextView tvTitle, tvSubmit;
    private ImageView ivBack, ivAdd;

    private RecyclerView recyclerView;
    private AddExpenseRecycleAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private List<ExpenseItemBean> expenseItemBeanList = new ArrayList<>();

    private ProgressDialogUtil pdu = new ProgressDialogUtil(ExpenseItemListActivity.this, "提示", "正在加载中");
    private ProgressDialogUtil pduPdf = new ProgressDialogUtil(ExpenseItemListActivity.this, "提示", "正在生成，请稍后...");

    private SharedPreferencesUtil spu;
    private String pdfBase64Str;
    private File file;
    private JSONObject jsonobj, pdfJsonObjec;
    private int annexId;
    private String pdfPath;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Loger.e("---handler-msg");
                List<ExpenseItemBean> singleReimbursementList = JSONArray.parseArray(jaResult.toJSONString(), ExpenseItemBean.class);
                for (int i = 0; i < singleReimbursementList.size(); i++) {
                    Loger.e("name--" + singleReimbursementList.get(i).getName());
                }
                expenseItemBeanList.clear();
                expenseItemBeanList.addAll(singleReimbursementList);
                adapter.notifyDataSetChanged();

            }
            if (msg.what == 3) {
                //  int status=  jsonobj.getIntValue("status")
                int status = jsonobj.getIntValue("status");
                if (status == 200) {
                    annexId = jsonobj.getInteger("annexId");
                    toDownLoadPdf();
                } else {

                    if (pduPdf.getMypDialog().isShowing()) {
                        pduPdf.dismisspd();
                    }
                    // getPdfForm();
                    Toast.makeText(ExpenseItemListActivity.this, "获取生成pdf出错", Toast.LENGTH_SHORT).show();
                }

//                if (annexId != 0) {
//                    //去下载pdf
//                  //  int status = jsonobj.getIntValue("status");
//                    if (status == 200) {
//                        toDownLoadPdf();
//                    } else {
//                        if (pduPdf.getMypDialog().isShowing()) {
//                            pduPdf.dismisspd();
//                        }
//                        // getPdfForm();
//                        Toast.makeText(ExpenseItemListActivity.this, "获取生成pdf出错", Toast.LENGTH_SHORT).show();
//                    }
//
//                }

            } else if (msg.what == 4) {
                //download finish

                if (pduPdf.getMypDialog().isShowing()) {
                    pduPdf.dismisspd();
                }
                pdfBase64Str = pdfJsonObjec.getString("file");

                if (!TextUtils.isEmpty(pdfBase64Str)) {

                    // signVerifyP1(pdfBase64Str);
                }
                String originalFilename = pdfJsonObjec.getString("originalFilename");
                //  pdfPath = FileUtils.getExternalFilesDirPath(ExpenseItemListActivity.this);

//                final File file = new File(FileUtils.getExternalFilesDirPath(context),
//                        "reim/" + FileUtils.getFileName(url));
                String filePath = pdfPath + "/" + originalFilename;
                Loger.e("filePath-------" + filePath);
                file = DownloadFileUtil.base64StringToPdf(pdfBase64Str, filePath);
                if (file == null) {
                    Loger.e("-----file------null");
                }
                //  startActivity(IntentUtils.getPdfFileIntent(file, AddExpenseActivity.this));

                if (file.exists() && file.length() > 0) {
                    Loger.e("-----file exist");
                    spu.setPdfFile(file.getPath());
                    toActivityWithData(context, SubmitExpenseActivity.class, "data", pdfBase64Str);

                } else {
                    Toast.makeText(ExpenseItemListActivity.this, "生成pdf出错,请重试", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };
    private int formId;
    private JSONArray jaResult;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_expense_list;
    }

    @Override
    protected void initData() {
        context = ExpenseItemListActivity.this;
        spu = new SharedPreferencesUtil(context);
        formId = spu.getFormId();
        getExpenseItem();
        //  initTestData();
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvTitle.setText("提交报销");
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setVisibility(View.GONE);
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivAdd.setVisibility(View.VISIBLE);
        tvSubmit = (TextView) findViewById(R.id.tv_expense_submit);
        recyclerView = (RecyclerView) findViewById(R.id.rl_expense);
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

//        recyclerView.addItemDecoration(new RecycleViewDivider(context, LinearLayoutManager.HORIZONTAL,
//                1, getResources().getColor(R.color.divider)));
        adapter = new AddExpenseRecycleAdapter(context, expenseItemBeanList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new AddExpenseRecycleAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(View view, int pos) {
                Loger.e("item clicked pos==" + pos);

            }
        });

        pdfPath = FileUtils.getExternalFilesDirPath(context);
    }

    private void getExpenseItem() {
        new Thread(GetExpenseItemRunable).start();
    }

    Runnable GetExpenseItemRunable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jsonArray = FormUtil.getExpenseItem(formId);
                if (jsonArray != null) {
                    jaResult = jsonArray;
                    Loger.e("---send-msg");
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


//    private void initTestData() {
//        for (int i = 0; i < 10; i++) {
//            ExpenseItemBean bean = new ExpenseItemBean();
//            bean.setDate("2018-01-07");
//            bean.setFee("200." + i);
//            bean.setId(i);
//            bean.setRemark("remark" + i);
//            bean.setType("打车费");
//            expenseItemBeanList.add(i, bean);
//        }
//    }

    @Override
    protected void initListeners() {
        ivAdd.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                //
                //toActivity(context, AddExpenseItemActivtity.class);
                toActivityWithType(context, AddExpenseItemActivtity.class, "list");
                break;
            case R.id.tv_expense_submit:
                //
                //finish();
                getPdfForm();
                //     toActivity(context, SubmitExpenseActivity.class);
                break;
            default:
                break;
        }
    }

    private void getPdfForm() {
        pduPdf.showpd();
        new Thread(GetPdfFormRunnable).start();
    }

    private void toDownLoadPdf() {

        new Thread(DownLoadPdfRunnable).start();

    }


    //得到需要生成pdf的 annexId
    Runnable GetPdfFormRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = FormUtil.getFormPdf(formId, spu.getUidNum());

                if (jo != null) {

                    jsonobj = jo;

                    handler.sendEmptyMessage(3);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    //下载pdf form
    Runnable DownLoadPdfRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = DownloadFileUtil.downPdfForm(annexId, pdfPath);
                if (jo != null) {
                    pdfJsonObjec = jo;

//                    annexId = jsonobj.getInteger("annexId");
                    handler.sendEmptyMessage(4);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


    //这边去生成PDF 成功－跳到下一页面，不成功，继续生成 ，将file 保存在spu里


}
