package com.sas.rh.reimbursehelper.newactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.sas.rh.reimbursehelper.Adapter.AddExpenseRecycleViewAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ExpenseItemBean;
import com.sas.rh.reimbursehelper.Bean.ExpenseThirdTypeBean;
import com.sas.rh.reimbursehelper.Bean.SecondCategoryBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseCategoryUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseItemUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.Utils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.FileToBase64Util;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.TimePickerUtils;
import com.sas.rh.reimbursehelper.Util.TimeUtils;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.sas.rh.reimbursehelper.view.activity.AddBaoxiaojizhuActivity;
import com.sas.rh.reimbursehelper.view.activity.AddExpenseActivity;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;
import com.sas.rh.reimbursehelper.widget.CountEditText;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopTwoListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.InjectView;
import cn.unitid.spark.cm.sdk.business.SignatureP1Service;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by liqing on 18/4/24.
 * 添加报销项
 */

public class AddExpenseItemActivtity extends BaseActivity {
    private CountEditText countEditText;
    private TextView tvTitle, tvFinish, tvExpenseType;
    private ImageView ivBack;
    private RelativeLayout rlPickDate;
    private ExpandPopTabView expandTabView;
    private List<KeyValueBean> parentsList = new ArrayList<>();
    //private List<KeyValueBean> childList = new ArrayList<>();
    List<ArrayList<KeyValueBean>> childList = new ArrayList<>();
    private PopTwoListView popTwoListView;
    private RelativeLayout rlType;
    private JSONArray jsonresult;
    private SharedPreferencesUtil spu;
    private ArrayList<String> secondTypeList;
    private ImageView ivPic;
    private List<KeyValueBean> thirdList = new ArrayList<>();
    private byte expenseCategoryId;
    private String secondStr, tvDateStr, edtRemarkStr;
    private Byte expenseItem, expenseCategory;
    private Context context;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private ProgressDialogUtil pdu = new ProgressDialogUtil(AddExpenseItemActivtity.this, "提示", "正在加载中");

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                ToastUtil.showToast(AddExpenseItemActivtity.this, "加载完毕", Toast.LENGTH_LONG);
                List<SecondCategoryBean> expenseReimbursementFormList = JSONArray.parseArray(jsonSecondResult.toJSONString(), SecondCategoryBean.class);

                if (expenseReimbursementFormList.size() == 0) {
                    ToastUtil.showToast(AddExpenseItemActivtity.this, "暂无数据，请重试！", Toast.LENGTH_LONG);
                    return;
                }
                secondTypeList = new ArrayList<String>();
                //是为了一进入，就默认获得第一个二级 的三级
                int i = 0;
                for (SecondCategoryBean object : expenseReimbursementFormList) {
                    i++;
                    //
                    secondTypeList.add(object.getExpenseCategoryName());

                    if (i == 1) {
                        String defaultParentId = object.getExpenseCategoryId();
                        // expenseCategoryId = Byte.valueOf(defaultParentId);
                        getThirdCategory(defaultParentId);
                    }
                    parentsList.add(new KeyValueBean(object.getExpenseCategoryId(), object.getExpenseCategoryName()));


                }
                initExpandaTabView();
            } else if (msg.what == 2) {
                int status = joFormId.getIntValue("status");
                if (status == 200) {
                    formId = joFormId.getIntValue("formId");
                    spu.setFormId(formId);
                    Loger.e("add item formid--" + spu.getFormId());
                    level = 1;
                } else if (status == 208) {
                    level = 0;
                    msgFormid = joFormId.getString("description");
                }
            } else if (msg.what == 3) {
                //三级报销科目
                thirdList.clear();
                //  Loger.e("third type--jsonresult-----" + jsonresult);
                //   ERFormList = new ArrayList<DeptCategoryItemVoExtend>();
                List<ExpenseThirdTypeBean> thirdExpenseCategoryList =
                        JSONArray.parseArray(jsonresult.toJSONString(), ExpenseThirdTypeBean.class);
                // Loger.e("thirdExpenseCategoryList--get" + thirdExpenseCategoryList.size());

                for (int i = 0; i < thirdExpenseCategoryList.size(); i++) {
                    //   Loger.e("thirdExpenseCategoryList-name" + thirdExpenseCategoryList.get(i).getExpenseItemName());
                    if (thirdExpenseCategoryList.get(i).getExpenseItemId() != null) {
                        ExpenseThirdTypeBean bean = thirdExpenseCategoryList.get(i);
                        KeyValueBean keyValueBean = new KeyValueBean(bean.getExpenseItemId().toString(), bean.getExpenseItemName());
                        thirdList.add(keyValueBean);
                    }


                }
                for (int i = 0; i < thirdList.size(); i++) {
                    //   Log.e("thirdListNewGet==", "" + thirdList.get(i).getValue());
                }
                popTwoListView.refreshChild(thirdList);


            } else if (msg.what == 4) {

                //新增一条报销项成功－提交jsonStr验签
                if (addSingleReimStatus == 200) {
                    submitJsonStr();
                } else {
                    if (pdu.getMypDialog().isShowing()) {
                        pdu.dismisspd();
                    }
                    ToastUtil.showToast(AddExpenseItemActivtity.this, "提交失败，请重试！", Toast.LENGTH_LONG);
                }

            } else if (msg.what == 5) {

                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                //上传 签名成功
                int code = jsonobj.getIntValue("status");

                if (code == 200) {

                    ToastUtil.showToast(AddExpenseItemActivtity.this, "提交成功！", Toast.LENGTH_LONG);
                    toActivity(AddExpenseItemActivtity.this, ExpenseItemListActivity.class);

                } else if (code == 208) {
                    //formId不存在，
                    ToastUtil.showToast(AddExpenseItemActivtity.this, "请先让领导为您分配部门！", Toast.LENGTH_LONG);
                } else {
                    ToastUtil.showToast(AddExpenseItemActivtity.this, "提交失败，请重试！", Toast.LENGTH_LONG);
                }
            } else if (msg.what == 6) {
                int status = jsonRemark.getIntValue("status");
                if (status == 200) {
                    remarkStr = jsonRemark.getString("remark");
                    countEditText.setEtHint(remarkStr);
                }
            } else if (msg.what == 0) {
                ToastUtil.showToast(context, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(context, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }
    };


    private ArrayList<String> photos = new ArrayList<>();
    private String path;
    private Uri uri;
    private String base64Code;
    private TextView tvDate;
    private EditText edtMoney;
    private String edtMoneyStr;
    private String expenseTypeStr;
    private String edtFeeStr;
    private double amount;
    private Date date;
    private JSONObject jsonRemark;
    private String remarkStr;
    private CharSequence inputStr;
    private int addSingleReimStatus;
    private String intentType;
    private JSONObject joFormId;
    private int level;
    private String msgFormid;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_expense_item;
    }

    @Override
    protected void initData() {
        context = AddExpenseItemActivtity.this;
        countEditText = (CountEditText) findViewById(R.id.edt_count);
        tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setVisibility(View.VISIBLE);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        rlType = (RelativeLayout) findViewById(R.id.rl_type);
        tvExpenseType = (TextView) findViewById(R.id.tv_expense_type);
        ivBack.setVisibility(View.GONE);
        expandTabView = (ExpandPopTabView) findViewById(R.id.expandable_list_view);
        ivPic = (ImageView) findViewById(R.id.iv_pic);
        tvDate = (TextView) findViewById(R.id.tv_expense_time);
        rlPickDate = (RelativeLayout) findViewById(R.id.rl_pick_date);

        edtMoney = (EditText) findViewById(R.id.edt_money);
        tvTitle.setText("报销单");
        spu = new SharedPreferencesUtil(AddExpenseItemActivtity.this);
        formId = spu.getFormId();
        Loger.e("addExpens--formId=" + formId);
        countEditText
                .setLength(125)
                .setType(CountEditText.PERCENTAGE)
                .show();
        //开始加载 二级科目
        getExpenseSecondType();

        /* 这里判断是从主页进入？还是从列表的"＋"进入的？
         从intent 里获取type 类型 ＝home :主页 ＝list 列表
         home -getFormId; list -则从spu 获得

          */
//        if (getIntent() != null) {
//            intentType = getIntent().getStringExtra("type");
//        }
//        if (intentType.equals("home")) {
//            getFormId();
//        } else {
//            formId = spu.getFormId();
//        }
        formId = spu.getFormId();
        //  Loger.e("--intentType--" + intentType + "  formId--" + formId);
    }

    public void setDrawable() {
        Drawable drawableLeft = getResources().getDrawable(
                R.drawable.ic_rmb_blue);
        edtMoney.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                null, null, null);
        edtMoney.setCompoundDrawablePadding(4);

    }

    private void getFormId() {
        new Thread(GetFormIdRunable).start();
    }

    private void getExpenseSecondType() {
        new Thread(GetExpenseSecondTypeThread).start();
    }

    private void getThirdCategory(String key) {
        expenseCategoryId = Byte.parseByte(key);
        new Thread(GetThirdTypeRunable).start();

    }

    private void submitJsonStr() {
        new Thread(SubmitSignThread).start();
    }

    private int index = 1;
    //上传签名JsonStr
    Runnable SubmitSignThread = new Runnable() {
        @Override
        public void run() {
            try {

                //  String result=  SingleReimbursementUtil.signJsonStringPdfTest(base64Code, spu.getCert(), spu.getKey(), signPath, index, expenseId);
                JSONObject jo = SingleReimbursementUtil.signJsonStringNew(base64Code, spu.getCert(), spu.getKey(), path, index, expenseId);

                //  JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(expenseItem, expenseCategory, formId, amount, remark);
                if (jo != null) {
                    jsonobj = jo;
                    handler.sendEmptyMessage(5);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };
    private JSONArray jsonSecondResult;
    Runnable GetExpenseSecondTypeThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = ExpenseCategoryUtil.select(spu.getUidNum());
                if (jo != null) {
                    jsonSecondResult = jo;
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

    private int formId;
    Runnable GetFormIdRunable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = FormUtil.getFormId(spu.getUidNum());
                if (jo != null) {
                    joFormId = jo;
                    handler.sendEmptyMessage(2);
                    //  formId = FormUtil.returnFormId();
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


    // 获取三级报销单 fanhui
    Runnable GetThirdTypeRunable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = ExpenseItemUtil.getThirdCategory(spu.getUidNum(), expenseCategoryId);
                if (jo != null) {
                    jsonresult = jo;
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


    private void initExpandaTabView() {
        setSecondMenuData();
        addItem(expandTabView, parentsList, childList, parentsList.get(0).getKey(), childList.get(0).get(0).getKey(), "");
    }


    /**
     * 二级菜单数据源
     */
    void setSecondMenuData() {
        // parentsList = new ArrayList<>();
//        KeyValueBean parentBean = new KeyValueBean();
//        parentBean.setKey("1");
//        parentBean.setValue("四川");
//        parentsList.add(parentBean);
//
//        parentBean = new KeyValueBean();
//        parentBean.setKey("2");
//        parentBean.setValue("重庆");
//        parentsList.add(parentBean);
//
//        parentBean = new KeyValueBean();
//        parentBean.setKey("3");
//        parentBean.setValue("云南");
//        parentsList.add(parentBean);
//        //==================================================

        childList = new ArrayList<>();
        ArrayList<KeyValueBean> sclist = new ArrayList<>();
        KeyValueBean bean = new KeyValueBean();
        bean.setKey("11");
        bean.setValue("成都");
        sclist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("12");
        bean.setValue("绵阳");
        sclist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("13");
        bean.setValue("德阳");
        sclist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("14");
        bean.setValue("宜宾");
        sclist.add(bean);
        childList.add(sclist);


        ArrayList<KeyValueBean> cqlist = new ArrayList<>();
        bean = new KeyValueBean();
        bean.setKey("21");
        bean.setValue("渝北");
        cqlist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("22");
        bean.setValue("渝中");
        cqlist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("23");
        bean.setValue("江北");
        cqlist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("24");
        bean.setValue("沙坪坝");
        cqlist.add(bean);
        childList.add(cqlist);

        ArrayList<KeyValueBean> shlist = new ArrayList<>();
        bean = new KeyValueBean();
        bean.setKey("31");
        bean.setValue("昆明");
        shlist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("32");
        bean.setValue("丽江");
        shlist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("33");
        bean.setValue("香格里拉");
        shlist.add(bean);

        bean = new KeyValueBean();
        bean.setKey("34");
        bean.setValue("凯里");
        shlist.add(bean);
        childList.add(shlist);

        //     childList.add(shlist);

    }

    public void addItem(final ExpandPopTabView expandTabView, final List<KeyValueBean> parentLists,
                        List<ArrayList<KeyValueBean>> childrenListLists, String defaultParentkey, String defaultChildkey, String defaultShowText) {
        popTwoListView = new PopTwoListView(this);
        //  popTwoListView.setDefaultSelectByValue(defaultParentSelect, defaultChildSelect);
        popTwoListView.setDefaultSelectByKey(defaultParentkey, defaultChildkey);
        //distanceView.setDefaultSelectByKey(defaultParent, defaultChild);
        popTwoListView.setCallBackAndData(expandTabView, parentLists, childrenListLists, new PopTwoListView.OnSelectListener() {

            @Override
            public void getValue(String showText, String parentKey, String childrenKey) {
                Log.e("----", "三级－－ :" + showText + " ,parentKey :" + parentKey + " ,childrenKey :" + childrenKey);
                if (TextUtils.isEmpty(parentKey)) {
                    // Toast.makeText(context, "请先选择二级科目类型", Toast.LENGTH_SHORT).show();
                    expenseCategory = Byte.valueOf(parentLists.get(0).getKey());
                    secondStr = parentLists.get(0).getValue();

                }
                Loger.e("----" + expenseCategory);
                tvExpenseType.setText(secondStr + "（" + showText + ")");
                expenseItem = Byte.valueOf(childrenKey);
                getExpenseRemark();


            }

            @Override
            public void getParentValue(int position, String showText, String key) {
                Log.e("－－－－", "二级－－－－ :" + showText + " ,二级key :" + key);
                secondStr = showText;
                //报销科目2级ID
                if (!TextUtils.isEmpty(key)) {
                    expenseCategory = Byte.valueOf(key);
                }
                getThirdCategory(key);
                Loger.e("expenseCategory--" + expenseCategory);


            }

        });
        expandTabView.addItemToExpandTab("", popTwoListView);

    }


    @Override
    protected void initListeners() {
        tvFinish.setOnClickListener(this);
        rlType.setOnClickListener(this);
        ivPic.setOnClickListener(this);
        rlPickDate.setOnClickListener(this);

    }


    //收到照片后处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            if (data != null) {
                photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                //System.out.println("********"+photos.get(0)+"********");
            }
            selectedPhotos.clear();


            if (photos != null) {

                selectedPhotos.addAll(photos);
            }
            path = selectedPhotos.get(0);
            uri = Uri.fromFile(new File(selectedPhotos.get(0)));
            try {

                String base64CodePic = FileToBase64Util.encodeBase64File(path);


                signVerifyP1(base64CodePic);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //签名验证
    private void signVerifyP1(final String base64Code1) {
        pdu.showpd();

        String plantext = base64Code1;
        base64Code = base64Code1;

        AddExpenseItemActivtity.this.getIntent().putExtra("data", plantext);
        AddExpenseItemActivtity.this.getIntent().putExtra("type", DataProcessType.SIGNATURE_P1.name());
        SignatureP1Service signatureP1Service = new SignatureP1Service(AddExpenseItemActivtity.this, "1234", new ProcessListener<DataProcessResponse>() {
            @Override
            public void doFinish(DataProcessResponse dataProcessResponse, String certificate) {
                Log.e("doFinish---", "= ");
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                if (dataProcessResponse.getRet() == 0) {
                    Log.e("密钥", "= " + dataProcessResponse.getResult());
                    spu.setCertKey(dataProcessResponse.getResult());
                    //获得就是签名证书
                    Log.e("cert", "= " + certificate);
                    spu.setCert(certificate);
                    Glide.with(AddExpenseItemActivtity.this)
                            .load(uri)
                            .thumbnail(0.1f)
                            .into(ivPic);


                } else {
//                    if (pdu.getMypDialog().isShowing()) {
//                        pdu.dismisspd();
//                    }
                    Loger.e("dataProcessResponse.getRet() !=0");
                    Toast.makeText(AddExpenseItemActivtity.this, "图片签名失败" + dataProcessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void doException(CmSdkException e) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                Toast.makeText(AddExpenseItemActivtity.this, "图片签名失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_finish:

                checkData();

                //  toActivity(context, ExpenseItemListActivity.class);
                break;
            case R.id.rl_type:
                if (expandTabView.isShown()) {
                    expandTabView.onExpandPopView();
                }
                // toActivity(AddExpenseItemActivtity.this, ExpenseTypeActivity.class);
                break;
            case R.id.rl_pick_date:
                TimePickerUtils.getInstance().onYearMonthDayPicker(AddExpenseItemActivtity.this, tvDate);
                break;
            case R.id.iv_pic:
                takePics();
            default:
                break;
        }


    }

    private void takePics() {
        PhotoPicker.builder()
                .setPhotoCount(1)
                .setShowCamera(true)
                .setPreviewEnabled(false)
                .setSelected(selectedPhotos)
                .start(AddExpenseItemActivtity.this);
    }


    private void checkData() {
        edtFeeStr = edtMoney.getText().toString();
        expenseTypeStr = tvExpenseType.getText().toString();
        tvDateStr = tvDate.getText().toString();
        edtRemarkStr = countEditText.getText().toString();


        if (TextUtils.isEmpty(edtFeeStr)) {
            Toast.makeText(this, "金额不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtRemarkStr)) {
            Toast.makeText(this, "说明不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(expenseTypeStr)) {
            Toast.makeText(this, "请选择类别", Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断照片是否为空
        if (selectedPhotos.size() == 0) {
            Toast.makeText(this, "票据照片必选", Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断照片是否为空
        if (TextUtils.isEmpty(tvDateStr)) {
            Toast.makeText(this, "时间不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        date = Utils.strToDate(tvDateStr);
        //新增报销项目

        amount = Double.parseDouble(edtFeeStr);

        submitExpenseItem();
    }


    private void submitExpenseItem() {

        pdu.showpd();

        formId = FormUtil.returnFormId();
        spu.setFormId(formId);

        new Thread(SubmitbillThread).start();

    }

    private void getExpenseRemark() {
        new Thread(GetExpenseRemarkRunable).start();
    }

    Runnable GetExpenseRemarkRunable = new Runnable() {
        @Override
        public void run() {

            try {
                Log.e("expenseItem=", "" + expenseItem);
                if (expenseCategory == null) {
                    Toast.makeText(context, "请选择二级科目", Toast.LENGTH_SHORT).show();
                }

                JSONObject jo = SingleReimbursementUtil.getExpenseRemark(spu.getUidNum(), expenseCategory, expenseItem);
                if (jo != null) {
                    jsonRemark = jo;

                    handler.sendEmptyMessage(6);
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }
    };


    private int expenseId;
    private JSONObject jsonobj;
    //新增一条报销项
    Runnable SubmitbillThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                Log.e("expenseItem=", "" + expenseItem);

                JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(spu.getUidNum(), expenseItem, expenseCategory,
                        formId, amount, edtRemarkStr, date);
                if (jo != null) {
                    jsonobj = jo;
                    expenseId = jo.getIntValue("expenseId");
                    addSingleReimStatus = jo.getIntValue("status");
                    Loger.e("expendId--" + expenseId);

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


}
