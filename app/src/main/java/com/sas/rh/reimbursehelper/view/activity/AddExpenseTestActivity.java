package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.sas.rh.reimbursehelper.Adapter.AddExpenseRecycleViewAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.ExpenseItemBean;
import com.sas.rh.reimbursehelper.Bean.SecondCategoryBean;
import com.sas.rh.reimbursehelper.Bean.ThirdExpenseCategoryBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseCategoryUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.UploadFileUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.FileToBase64Util;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopTwoListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.unitid.spark.cm.sdk.business.SignatureP1Service;
import cn.unitid.spark.cm.sdk.common.DataProcessType;
import cn.unitid.spark.cm.sdk.data.response.DataProcessResponse;
import cn.unitid.spark.cm.sdk.exception.CmSdkException;
import cn.unitid.spark.cm.sdk.listener.ProcessListener;
import io.fabric.sdk.android.Fabric;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static com.sas.rh.reimbursehelper.NetworkUtil.FormUtil.addForm;
//新增报销

public class AddExpenseTestActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_THIRD_TYPE = 100;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RecyclerView bxRecyclerView;
    private RecyclerView.Adapter bxAdapter;
    private RecyclerView.LayoutManager bxLayoutManager;
    private List<String> photos = null;//已选择照片的张数
    private List<String> xiaofeileixinglist = new ArrayList<String>();//创建一个String类型的数组列表。
    private SharedPreferencesUtil spu;

    private int requestcode = 0;
    private Byte expenseCategoryId;

    private JSONArray jsonresult;
    private JSONObject jsonobj;
    private int upamount = 0;
    //expenseItem 三级科目的ID
    private Byte expenseItem, expenseCategory;
    private Integer formId;
    private Double amount;
    private String remark;
    private String path;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(AddExpenseTestActivity.this, "上传提示", "正在加载中");

    //二级菜单控件
    private static ExpandPopTabView expandTabView;
    //填写详情
    private EditText edtRemark;
    private LinearLayout llRemark;
    private List<KeyValueBean> parentsList = new ArrayList<>();
    private List<ArrayList<KeyValueBean>> childList = new ArrayList<>();
    private List<String> mTypeLevelOne = new ArrayList<>();
    private List<String> mTypeLevelTwo = new ArrayList<>();
    private TextView tvRemarkTitle;
    private TextView tvRemarkFinish;
    private EditText edtFee;
    private List<ExpenseItemBean> addExpenseItemList = new ArrayList<>();
    private AddExpenseRecycleViewAdapter addExpenseRecycleViewAdapter;
    private TextView tvSecondTitle;

    private RelativeLayout llExpenseType;
    //三级类别集合
    //三级类别集合
    private List<ThirdExpenseCategoryBean> thirdExpenseCategoryBeanList = new ArrayList<>();
    private List<KeyValueBean> thirdList = new ArrayList<>();
    private ImageView ivPhoto;
    private Boolean isFirst;
    private ImageView ivSelect;
    private TextView tvAddExpense;
    private PopTwoListView popTwoListView;
    private Handler expenseCategoryback = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (pdu.getMypDialog().isShowing()) {
                pdu.dismisspd();
            }
            if (msg.what == 1) {
                // 获取所有二级报销类别
                ToastUtil.showToast(AddExpenseTestActivity.this, "加载完毕", Toast.LENGTH_LONG);
                List<SecondCategoryBean> expenseReimbursementFormList = JSONArray.parseArray(jsonresult.toJSONString(), SecondCategoryBean.class);

                xiaofeileixinglist = new ArrayList<String>();
                for (SecondCategoryBean object : expenseReimbursementFormList) {
                    xiaofeileixinglist.add(object.getExpenseCategoryName());
                    parentsList.add(new KeyValueBean(object.getExpenseCategoryId(), object.getExpenseCategoryName()));

                }
                initExpandaTabView();
                //System.out.println("========="+xiaofeileixinglist.size());
                //     initxflxsp();
//                if(jsonresult != null){
//                    finish();
//                }
            } else if (msg.what == 2) {
                //   ERFormList = new ArrayList<DeptCategoryItemVoExtend>();
                List<ThirdExpenseCategoryBean> thirdExpenseCategoryList =
                        JSONArray.parseArray(jsonresult.toJSONString(), ThirdExpenseCategoryBean.class);
//                for (int i = 0; i < thirdList.size(); i++) {
//                    Log.e("thirdExpenseCat i=", "" + i + thirdExpenseCategoryList.get(i).getItemName());
//                }
                thirdList.clear();
                for (int i = 1; i < thirdExpenseCategoryList.size(); i++) {

                    if (thirdExpenseCategoryList.get(i).getItem() != null) {
                        ThirdExpenseCategoryBean bean = thirdExpenseCategoryList.get(i);
                        KeyValueBean keyValueBean = new KeyValueBean(bean.getItem(), bean.getItemName());
                        thirdList.add(keyValueBean);
//                        dcive.setItem(deptCategoryItemVoList.get(i).getItem());
//                        dcive.setItemName(deptCategoryItemVoList.get(i).getItemName());
//                        dcive.setCategory(deptCategoryItemVoList.get(i).getCategory());
//                        dcive.setCategoryName(deptCategoryItemVoList.get(i).getCategoryName());
//                        dcive.setRemark(deptCategoryItemVoList.get(i).getRemark());
//                        ERFormList.add(dcive);
                    }


                }
                for (int i = 0; i < thirdList.size(); i++) {
                    Log.e("thirdList==", "" + thirdList.get(i).getValue());
                }
                popTwoListView.refreshChild(thirdList);


                initBxRclv();
            } else if (msg.what == 3) {
                //开始上传照片

                ToastUtil.showToast(AddExpenseTestActivity.this, "提交成功！", Toast.LENGTH_LONG);
            } else if (msg.what == 4) {
                //上传 签名成功
                ToastUtil.showToast(AddExpenseTestActivity.this, "签名文件提交成功！", Toast.LENGTH_LONG);
                Glide.with(AddExpenseTestActivity.this)
                        .load(uri)
                        .thumbnail(0.1f)
                        .into(ivPhoto);

            } else if (msg.what == 0) {
                ToastUtil.showToast(AddExpenseTestActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(AddExpenseTestActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    private String selectSecondType;
    private int expenseId;
    private String parentKey;
    private String base64CodeTwo;
    private String base64Code;
    private Uri uri;
    private String signJson1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        isFirst = true;
        setContentView(R.layout.activity_add_expanse);
        if (requestcode == 2) {
            //bxid = getIntent().getStringExtra("biiid");
        }
        //  ERFormList = new ArrayList<DeptCategoryItemVoExtend>();
        spu = new SharedPreferencesUtil(AddExpenseTestActivity.this);
        Log.e("--", "--userId" + spu.getUidNum());

        //二级菜单
        tvAddExpense = (TextView) findViewById(R.id.tv_add_expense);
        ivSelect = (ImageView) findViewById(R.id.iv_select);
        tvSecondTitle = (TextView) findViewById(R.id.tv_expense_sencond);
        expandTabView = (ExpandPopTabView) findViewById(R.id.expandable_list_view);
        llExpenseType = (RelativeLayout) findViewById(R.id.ll_expense_type);
        llRemark = (LinearLayout) findViewById(R.id.ll_remark);
        edtRemark = (EditText) findViewById(R.id.edt_remark);
        tvRemarkTitle = (TextView) findViewById(R.id.tv_remark_title);
        tvRemarkFinish = (TextView) findViewById(R.id.tv_finish_remark);
        edtFee = (EditText) findViewById(R.id.edt_fee);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        tvRemarkFinish.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        tvAddExpense.setOnClickListener(this);
        initBxRclv();
        // initExpandaTabView();
        GetallDepartmentInfo();


//        //照片选择器初始化
//        photoAdapter = new PhotoAdapter(this, selectedPhotos);
//        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
//        recyclerView.setAdapter(photoAdapter);
//
//

    }


//    private void setConfigsDatas() {
//        try {
//            InputStream is = getAssets().open("searchType");
//            String searchTypeJson = readStream(is);
//            ConfigsMessageDTO messageDTO = JSONObject.parseObject(searchTypeJson, ConfigsMessageDTO.class);
//            ConfigsDTO configsDTO = messageDTO.getInfo();
//
//            mPriceLists = configsDTO.getPriceType();
//            mSortLists = configsDTO.getSortType();
//            mFavorLists = configsDTO.getSortType();
//
//            List<ConfigAreaDTO> configAreaListDTO = configsDTO.getCantonAndCircle();
//            for (ConfigAreaDTO configAreaDTO : configAreaListDTO) {
//                KeyValueBean keyValueBean = new KeyValueBean();
//                keyValueBean.setKey(configAreaDTO.getKey());
//                keyValueBean.setValue(configAreaDTO.getValue());
//                mParentLists.add(keyValueBean);
//
//                ArrayList<KeyValueBean> childrenLists = new ArrayList<>();
//                for (KeyValueBean keyValueBean1 : configAreaDTO.getBusinessCircle()) {
//                    childrenLists.add(keyValueBean1);
//                }
//                mChildrenListLists.add(childrenLists);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    public String readStream(InputStream is) {
//        try {
//            ByteArrayOutputStream bo = new ByteArrayOutputStream();
//            int i = is.read();
//            while (i != -1) {
//                bo.write(i);
//                i = is.read();
//            }
//            return bo.toString();
//        } catch (IOException e) {
//            return "";
//        }
//    }
//}

    private void initExpandaTabView() {
//        for (int i = 0; i < 5; i++) {
//            mTypeLevelOne.add(i, "type" + i);
//        }
////        mTypeLevelTwo.addAll(mTypeLevelOne);
//        for (int i = 0; i <3; i++) {
//            KeyValueBean keyValueBean = new KeyValueBean();
//            keyValueBean.setKey(i + "");
//            keyValueBean.setValue(mTypeLevelOne.get(i));
//            mParentLists.add(keyValueBean);
//        }
//
//        ArrayList<KeyValueBean> childrenLists = new ArrayList<>();
//        for (int i = 0; i < 3; i++) {
//            KeyValueBean keyValueBean = new KeyValueBean();
//            keyValueBean.setKey(i + "two");
//            keyValueBean.setValue(mTypeLevelOne.get(i));
//            childrenLists.add(keyValueBean);
//            mChildrenListLists.add(i,childrenLists);
//
//        }
        setSecondMenuData();

        addItem(expandTabView, parentsList, childList, "", "合江亭", "请选择");
    }


    /**
     * 二级菜单数据源
     */
    void setSecondMenuData() {
        // parentsList = new ArrayList<>();
        KeyValueBean parentBean = new KeyValueBean();
        parentBean.setKey("1");
        parentBean.setValue("四川");
        parentsList.add(parentBean);

        parentBean = new KeyValueBean();
        parentBean.setKey("2");
        parentBean.setValue("重庆");
        parentsList.add(parentBean);

        parentBean = new KeyValueBean();
        parentBean.setKey("3");
        parentBean.setValue("云南");
        parentsList.add(parentBean);
        //==================================================

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

    String defaultabTitle = "请选择";

    public void addItem(final ExpandPopTabView expandTabView, List<KeyValueBean> parentLists,
                        List<ArrayList<KeyValueBean>> childrenListLists, String defaultParentSelect, final String defaultChildSelect, String defaultShowText) {
        popTwoListView = new PopTwoListView(this);
        popTwoListView.setDefaultSelectByValue(defaultParentSelect, defaultChildSelect);
        //distanceView.setDefaultSelectByKey(defaultParent, defaultChild);
        popTwoListView.setCallBackAndData(expandTabView, parentLists, childrenListLists, new PopTwoListView.OnSelectListener() {

            @Override
            public void getValue(String showText, String parentKey, String childrenKey) {
                Log.e("tag", "thirdshowText :" + showText + " ,parentKey :" + parentKey + " ,childrenKey :" + childrenKey);
                //三级ID


                //三级为空，则显示二级类别
                if (thirdList.size() == 0) {
                    tvRemarkTitle.setText(selectSecondType);
                } else {
                    tvRemarkTitle.setText(showText);
                }

                tvRemarkTitle.setText(showText);
                llRemark.setVisibility(View.VISIBLE);
                edtFee.setFocusable(true);
                edtFee.setFocusableInTouchMode(true);
                edtFee.requestFocus();//获取焦点 光标出现
                edtFee.setText("");
                edtFee.setHint(getResources().getString(R.string.hint_fee));
                ivPhoto.setImageResource(R.drawable.ic_add);
                edtRemark.setText("");
                edtRemark.setHint(getResources().getString(R.string.remark));
                isFirst = false;
                tvSecondTitle.setVisibility(View.VISIBLE);
                tvSecondTitle.setText(selectSecondType);
                ivSelect.setVisibility(View.GONE);
                expandTabView.setVisibility(View.GONE);

            }

            @Override
            public void getParentValue(int position, String showText, String key) {
                Log.e("tag", "sencondshowText :" + showText + " ,parentKey :" + key);
                parentKey = key;
                selectSecondType = showText;
                //报销科目2级ID
                expenseCategory = Byte.valueOf(key);
                //根据 parentID，获取三级
                getThirdCategory(position, key);

            }

        });
        expandTabView.addItemToExpandTab(defaultShowText, popTwoListView);
    }

    private void getThirdCategory(int position, String key) {

        expenseCategoryId = Byte.parseByte(key);
        GetallFormInfo();

//        ArrayList<KeyValueBean> sclist = new ArrayList<>();
//        if (key.equals("1")) {
//            KeyValueBean bean = new KeyValueBean();
//            bean.setKey("11");
//            bean.setValue("成都");
//            sclist.add(bean);
//            bean = new KeyValueBean();
//            bean.setKey("12");
//            bean.setValue("绵阳");
//            sclist.add(bean);
//
//            childList.add(sclist);
//        } else {
//
//            KeyValueBean bean = new KeyValueBean();
//            bean = new KeyValueBean();
//            bean.setKey("13");
//            bean.setValue("德阳");
//            sclist.add(bean);
//
//            bean = new KeyValueBean();
//            bean.setKey("14");
//            bean.setValue("宜宾");
//            sclist.add(bean);
//            bean = new KeyValueBean();
//            bean.setKey("15");
//            bean.setValue("宜兴");
//            sclist.add(bean);
//
//            childList.add(sclist);
//        }
        //  popTwoListView.refreshChild(sclist);

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

            Log.e("path=", "=" + path);
            //photoAdapter.notifyDataSetChanged();
            uri = Uri.fromFile(new File(selectedPhotos.get(0)));
            try {
                if (spu.getUidNum() == 1) {
                    base64Code = FileToBase64Util.encodeBase64File(path);
                    signVerifyP1(base64Code);
                }
//                 else {
//                    String two ="MzAzMDMwMzAzMDMwMzEzOTMyMzgzMDMwMzAzMDMwMzAzMDMzMzQzNDMwMzMzNTMwMzAzMDMwMzAz\n" +
//                            "                                                                     MDM3MzgzOTMwNEQ0OTQ5NDY2RjU0NDM0MzQyNDk2RDY3NDE3NzQ5NDI0MTY3NDk1MTY1NTY3QTY2\n" +
//                            "                                                                     MzU3QTM2NDYzODczMzY0NzQ3NTQ3NTMxNEUzNDcyNjg0NTQ0NDE0RTQyNjc2QjcxNjg2QjY5NDcz\n" +
//                            "                                                                     OTc3MzA0MjQxNTE3MzQ2NDE0NDQxN0E0RDUxNzM3NzQzNTE1OTQ0NTY1MTUxNDc0NTc3NEE0NDU0\n" +
//                            "                                                                     NkE0NTUyNEQ0MTM4NDc0MTMxNTU0NTQzNjc3NzQ5NTY1NzM1NzA1NjQ4NEEzMTYzMzM1MTc4NDU1\n" +
//                            "                                                                     NDQxNTA0MjY3NEU1NjQyNDE0RDRENDM0NjRFNDk1MjU1NEU0MjQ5NDU2Mzc5NEQ0MjM0NTg0NDU0\n" +
//                            "                                                                     NDUzNDRENDQ0RDc5NEY0NDQxMzQ0RDZBNEQzMDRGNTY2RjU4NDQ1NDQ1MzQ0RDQ0NTk3OTRGNDQ0\n" +
//                            "                                                                     NTMxNEU1NDZCMzE0RjU2NkY3NzRFNDQ0NTRDNEQ0MTZCNDc0MTMxNTU0NTQyNjg0RDQzNTEzMDM0\n" +
//                            "                                                                     Nzg0NDdBNDE0RTQyNjc0RTU2NDI0MTRENEQ0Mjc1NjE2NDZBNzU2RDY0NkI2QTQ1NTU0RDQyNDk0\n" +
//                            "                                                                     NzQxMzE1NTQ1NDY0MjRENEM0RDU0NTUzNTRFNTQ0NTM0NEY0NDQ5MzE0RTQ0NjM3NzY3Njc0NTY5\n" +
//                            "                                                                     NEQ0MTMwNDc0MzUzNzE0NzUzNDk2MjMzNDQ1MTQ1NDI0MTUxNTU0MTQxMzQ0OTQyNDQ3NzQxNzc2\n" +
//                            "                                                                     NzY3NDU0QjQxNkY0OTQyNDE1MTQ0Nzk1NDUyNTY1MjU5Nzc3QTU3NDc2OTcyNkQzMDZENTg3MDc4\n" +
//                            "                                                                     Nzk0QzZCNDg0NTUxNTc0QTc4MzI2QTUzNkQzNDc3Mzg2QTMxNUE0NDc3Njk3MDUzNDE2QjcxMzc0\n" +
//                            "                                                                     Rjc1NTk3MTc3NjE3NzQ4NTY2NDY2MzQ2MzVBNzE2NTZBNDY0RTcyNTc1QTMwNkE1NzM1NjM3NjYy\n" +
//                            "                                                                     NDI3NzUzNkE2MzJGNDQ2MjRBNEY0RTY4NTc2NDZGNjU2ODU0MzUzMDJGNEM1NjQ4NTA3ODMyNjI2\n" +
//                            "                                                                     MjM0NTA2OTQ1NzI3MjU0NTIyQjY1NzI0MjZGNjY2MjYxNjM1OTc3MzI2QjZCNDY0MjY3NjI2ODdB\n" +
//                            "                                                                     NzM3NDYxNDc3OTUzNDkzMjcxMzU1MDU4NTA0NzZCNkE0ODY0NTg1NTMxNEYzNjQ1Mzg1ODU4NjM0\n" +
//                            "                                                                     MzcwMzk0ODZBNEM2RDRBNTcyQjY0N0E1OTY5NEQzMjRBNDQ2MjM4NzE2NDYyNEU3QTYxMkI3Njc1\n" +
//                            "                                                                     NkMzMzMxNzM0OTYzNDg0Mzc1NzIyRjMyMkI0NDc5NkU3NzQ4NDg1NDUzNTQ0Nzc5NTM2QjY1NTY0\n" +
//                            "                                                                     RTQxNEQ0MzQzNTk3QTUwNDU0QzMzNTQzMjc3Nzk1MjYyNDg2RjZBMzc1MTY2NTk2RjUyNTA2RjVB\n" +
//                            "                                                                     MzM2MjZCNjI1OTU1Njk2QzJCMzA3NzYzNEI1NzUyNjQzMjRBNTk2RTZFNDU2QTY3NEE1NTU1MzI2\n" +
//                            "                                                                     Mzc4MkI3MjQ0Njc3QTQ4NDQ3MTYyNTM0QzczNjI2NDRFNTM2RDQ2NUEzNDYzNEY0NDZBNTIyQjYy\n" +
//                            "                                                                     Njg3NjY2NDc3MTY2NUEzNDRDNEEzMDRDNzU0OTY4MkYzMDM4NkM0ODc0NDI0NDM5MzYzMDZCNzk2\n" +
//                            "                                                                     QjRGNTA2QzRGNEM3NTZFNkM0QTQxNjc0RDQyNDE0MTQ3NkE2NzY3NEI3NTRENDk0OTQzNzE2QTQx\n" +
//                            "                                                                     NjY0MjY3NEU1NjQ4NTM0RDQ1NDc0NDQxNTc2NzQyNTI1NzY5NEUzNzZBNDc0NTRGNDM3NDMzNEI2\n" +
//                            "                                                                     QjRBNzU3NDQ1NzE1NzRDNTE2ODM4NTM3MzRBNkE0MTY0NDI2NzRFNTY0ODUxMzQ0NTQ2Njc1MTU1\n" +
//                            "                                                                     NTc2ODYyNzg3MTY2MkY0MTc4NTE3OTQyNDM1NzY5NkY1NTYyNTY0NDQ2NzA2NDdBNzQzOTMwNzc0\n" +
//                            "                                                                     Mzc3NTk0NDU2NTIzMDUwNDI0MTUxNDQ0MTY3NjI0MTRENDIzMDQ3NDEzMTU1NjQ0QTUxNTE1NzRE\n" +
//                            "                                                                     NDI1MTQ3NDM0MzczNDc0MTUxNTU0NjQyNzc0RDQzNDI2NzY3NzI0MjY3NDU0NjQyNTE2MzQ0NDI0\n" +
//                            "                                                                     NDQyNDM0MjY3NEU1NjQ4NTM0MTQ1NEY3QTQxMzU0RDQ0NjM0NzQzNTM3MTQyNDg0MTQ3NDczNzdB\n" +
//                            "                                                                     NzE0MjQ2NTQ0MTcxNEQ0MzY3NDc0MzQzNzM0NzQxNTE1NTQ2NDI3NzQ5NDI0NjY4Nzg2RjY0NDg1\n" +
//                            "                                                                     Mjc3NEY2OTM4NzY2NDMzNjQzMzRDNkU0RTZGNUE1NzRFNjg0QzZENEU3NjYyNTMzOTc3NjIzMjc4\n" +
//                            "                                                                     NzA1OTMzNkI3NjRENDE2QjQ3NDEzMTU1NjQ0NTc3NTE0MzRENDE0MTc3NjY1MTU5NDk0Qjc3NTk0\n" +
//                            "                                                                     MjQyNTE1NTQ4NDE1MTQ1NDU2MzU0NDI3NjRENDQ2NzQ3NDM0MzczNDc0MTUxNTU0NjQyN0E0MTQy\n" +
//                            "                                                                     Njg2OTc4NkY2NDQ4NTI3NzRGNjkzODc2NjIzMjRFN0E2MzQ0NEQ3NTYzMzI2ODZDNTkzMjQ1NzU1\n" +
//                            "                                                                     OTMyMzk3NDRDMzIzOTZBNjMzMzQxNzY2MzMyNjg2QzU5MzI0NTc2NjMzMjY4NkM1OTMyNDU3NTYy\n" +
//                            "                                                                     MzI0RTdBNjM0NDQxN0E0MjY3Njc3MjQyNjc0NTQ2NDI1MTYzNzc0MTZGNTk2RTYxNDg1MjMwNjM0\n" +
//                            "                                                                     NDZGNzY0QzMyNzg2QjU5NTg0MTc5NEM2RTRFNkY1QTU3NEU2ODRDNkQ0RTc2NjI1MzM5Nzk2MjMy\n" +
//                            "                                                                     MzkzMDRDMzM0RTZGNUE1NzRFNjg1QTdBNDk3NTVBNDc1Njc5NEQ0OTQ4NjM0MjY3NEU1NjQ4NTIz\n" +
//                            "                                                                     ODQ1Njc2NDUxNzc2NzY0NDU3NzRFNjE0MTdBNkY0NDQ3NDc0QzMyNjgzMDY0NDg0MTM2NEM3OTM5\n" +
//                            "                                                                     NzM1QTQ3NDY3NzRENjkzNTdBNjE0NzU2NkE1OTUzMzU2QTYyMzIzMDc2NTEzMDQ1Nzk0RDQ0NDE3\n" +
//                            "                                                                     ODRENTMzOTUzNTE1NDZCNzc0RDdBNDU3NjUxMzE0QTRENEQ1NDQ5NzU1OTMzNEE3MzRENDk0NzU4\n" +
//                            "                                                                     NkY0OTQ3NTU2RjQ5NDc1MjY4NkY0NzRGNjI0NzUyNjg2MzQ0NkY3NjRDMzI3ODZCNTk1ODQxNzk0\n" +
//                            "                                                                     QzZFNEU2RjVBNTc0RTY4NEM2RDRFNzY2MjU0NkY3QTRGNDQ2Qjc2NTkzMjM0Mzk1MTMxNEE0RDRE\n" +
//                            "                                                                     NTQ0OTc1NTkzMzRBNzM0QzQ3MzkzMTUwNTY0QTQyNEY1NDQxN0E0RDUzNzg3NjY0NTQzMTQ0NTE1\n" +
//                            "                                                                     NDQ5Nzc0RDQ0NDU3ODRDNDczOTMxNTA1NzRFNzk2MjQzNzg3NjUwNTY1Njc1NjE1NjUyNzk2NDU4\n" +
//                            "                                                                     NEUzMDUwMzI0RTZDNjM2RTUyNzA1QTZENkM2QTU5NTg1MjZDNTU2RDU2MzI2MjMyNEU2ODY0NDc2\n" +
//                            "                                                                     Qzc2NjI2Qjc4NzA2MzMzNTEyRjU5NkQ0NjdBNUE1NDM5NzY1OTZENzA2QzU5MzM1MjQ0NjI0NzQ2\n" +
//                            "                                                                     N0E2MzdBMzE2QTU1NkI3ODQ1NjE1ODRFMzA2MzZENkM2OTY0NTg1MjcwNjIzMjM1NTE2MjMyNkM3\n" +
//                            "                                                                     NTY0NDQ0MzQyNkE2NzU5NDc0QjZGNDU2MzQxNjM1NTM0NDI0OTQ3NDQ0RDQ5NDc0MTRENDU2QjQ3\n" +
//                            "                                                                     NDM0MzcxNDI0ODQxNDg0NjRGNDk0NTUxNDI0NDMxNzM1QTQ3NDY3NzRGNjkzODc2NjI0NzUyNjg2\n" +
//                            "                                                                     MzQ0NDk3NTYzMzI2ODZDNTkzMjQ1NzU1OTMyMzk3NDRDMzIzOTMxNTA1ODRFNkY1QTU3NEU2ODQ5\n" +
//                            "                                                                     NDc0RTZDNjM2RTUyNzA1QTZENkM2QTU5NTg1MjZDNDk0NzRFNkY1OTU3";
//                    singTwoVerifyP1(two);
//                }

            } catch (Exception e) {
                e.printStackTrace();
            }
//            try {
//                String base64Code = FileToBase64Util.encodeBase64File(path);
//               // String base64Code = spu.getTestBase64();
//                //key,
//                //生成sas文件
//                FileToBase64Util.  //生成sas文件
//                       GenerateSasFile(spu.getCert(),spu.getKey(), path, base64Code, "/storage/emulated/0/Download/");
//                Log.e("base64=", "--" + base64Code);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


//            Glide.with(this)
//                    .load(uri)
//                    .thumbnail(0.1f)
//                    .into(ivPhoto);
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_THIRD_TYPE) {
            //三级科目的key value 返回
            addItemVisible();

            if (data != null) {
                String thirdKey = data.getStringExtra("thirdKey");
                String thirdValue = data.getStringExtra("thirdValue");
                //报销三级类别名称：
                tvRemarkTitle.setText(thirdValue);
                expenseItem = Byte.valueOf(thirdKey);

            }
        }
    }

    private void addItemVisible() {
        llRemark.setVisibility(View.VISIBLE);
        edtFee.setFocusable(true);
        edtFee.setFocusableInTouchMode(true);
        edtFee.requestFocus();//获取焦点 光标出现
        edtFee.setText("");
        edtFee.setHint(getResources().getString(R.string.hint_fee));
        ivPhoto.setImageResource(R.drawable.ic_add);
        edtRemark.setText("");
        edtRemark.setHint(getResources().getString(R.string.remark));
    }
//private void singThreeVerifyP1();

    private void singTwoVerifyP1(final String base64Code2) {


        String plantext = base64Code2;

        AddExpenseTestActivity.this.getIntent().putExtra("data", plantext);
        AddExpenseTestActivity.this.getIntent().putExtra("type", DataProcessType.SIGNATURE_P1.name());
        SignatureP1Service signatureP1Service = new SignatureP1Service(AddExpenseTestActivity.this, new ProcessListener<DataProcessResponse>() {
            @Override
            public void doFinish(DataProcessResponse dataProcessResponse, String certificate) {
                if (dataProcessResponse.getRet() == 0) {
                    Log.e("密钥", "= " + dataProcessResponse.getResult());
                    spu.setCertKey(dataProcessResponse.getResult());
                    Log.e("cert", "= " + certificate);
                    spu.setCert(certificate);

                    base64Code = base64Code2;
                    submitSignJsonstring();

//                    int result = FileToBase64Util.  //生成sas文件
//                            GenerateSasFile(spu.getCert(), spu.getKey(), path, base64Code,
//                            "/storage/emulated/0/Download/");


                    //如果生成 sas文件成功 就显示图片，否则提示 验签失败，重新上传图片
                    Uri uri = Uri.fromFile(new File(selectedPhotos.get(0)));
                    path = FileToBase64Util.getSaspath();

//                    if (result == 0) {
//                        Glide.with(AddExpenseActivity.this)
//                                .load(uri)
//                                .thumbnail(0.1f)
//                                .into(ivPhoto);
//                    } else {
//                        Toast.makeText(AddExpenseActivity.this, "生成sas文件失败，重新上传图片", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Toast.makeText(AddExpenseTestActivity.this, "图片签名失败" + dataProcessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void doException(CmSdkException e) {
                Toast.makeText(AddExpenseTestActivity.this, "图片签名失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //签名验证
    private void signVerifyP1(final String base64Code1) {

        String plantext = base64Code1;

        AddExpenseTestActivity.this.getIntent().putExtra("data", plantext);
        AddExpenseTestActivity.this.getIntent().putExtra("type", DataProcessType.SIGNATURE_P1.name());
        SignatureP1Service signatureP1Service = new SignatureP1Service(AddExpenseTestActivity.this, new ProcessListener<DataProcessResponse>() {
            @Override
            public void doFinish(DataProcessResponse dataProcessResponse, String certificate) {
                if (dataProcessResponse.getRet() == 0) {
                    Log.e("密钥", "= " + dataProcessResponse.getResult());
                    spu.setCertKey(dataProcessResponse.getResult());
                    Log.e("cert", "= " + certificate);
                    spu.setCert(certificate);
                    signJson1 = SingleReimbursementUtil.jsontoStr(base64Code1, certificate, dataProcessResponse.getResult(), path, 1, expenseId);
                    if (!TextUtils.isEmpty(signJson1)) {

                        singTwoVerifyP1(signJson1);
                    }

                    //签完名字之后, 先上传签了名字的JsonString ，成功 则Glide 显示图片到界面，否则 提示出错
                    //  submitSignJsonstring();
                    //   SingleReimbursementUtil.signJsonString(base64Code, certificate, dataProcessResponse.getResult(), path, 1, expenseId);


//                    int result = FileToBase64Util.  //生成sas文件
//                            GenerateSasFile(spu.getCert(), spu.getKey(), path, base64Code,
//                            "/storage/emulated/0/Download/");


//                    //如果生成 sas文件成功 就显示图片，否则提示 验签失败，重新上传图片
//                    Uri uri = Uri.fromFile(new File(selectedPhotos.get(0)));
//                    path = FileToBase64Util.getSaspath();
//
//                    base64CodeTwo = null;
//                    try {
//                        base64CodeTwo = FileToBase64Util.encodeBase64File(path);
//                        spu.setSasBase64(base64CodeTwo);
//                        Log.e("sasBase64", "----" + base64CodeTwo);
//                        // singTwoVerifyP1(base64CodeTwo);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

//                    if (result == 0) {
//                        Glide.with(AddExpenseActivity.this)
//                                .load(uri)
//                                .thumbnail(0.1f)
//                                .into(ivPhoto);
//                    } else {
//                        Toast.makeText(AddExpenseActivity.this, "生成sas文件失败，重新上传图片", Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Toast.makeText(AddExpenseTestActivity.this, "图片签名失败" + dataProcessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void doException(CmSdkException e) {
                Toast.makeText(AddExpenseTestActivity.this, "图片签名失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitSignJsonstring() {
        new Thread(SubmitSignThread).start();
    }

    private void initBxRclv() {
        bxLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //  bxAdapter = new MyAdapter();
        bxAdapter = new AddExpenseRecycleViewAdapter(this, addExpenseItemList);
        bxRecyclerView = (RecyclerView) findViewById(R.id.billlist);
        // 设置布局管理器
        bxRecyclerView.setLayoutManager(bxLayoutManager);
        // 设置adapter
        bxRecyclerView.setAdapter(bxAdapter);
    }


    private void GetallDepartmentInfo() {
        pdu.showpd();
        new Thread(GetExpenseCategoryInfoThread).start();
    }

    private void SubmitbillInfo() {

        pdu.showpd();
        if (TextUtils.isEmpty(FormUtil.getBxdid())) {
            Log.e("---formId=null", "null");
        }
        formId = Integer.parseInt(FormUtil.getBxdid());
        new Thread(SubmitbillThread).start();


//        for (upamount = 0; upamount < ERFormList.size(); upamount++) {
//            expenseItem = ERFormList.get(upamount).getItem();
//            expenseCategory = ERFormList.get(upamount).getCategory();
//            formId = Integer.parseInt(FormUtil.getBxdid());
//            amount = ERFormList.get(upamount).getAmount();
//            remark = ERFormList.get(upamount).getRemark_written();
//            new Thread(SubmitbillThread).start();
//        }

        //pdu.showpd();
//        for(upamount = 0;upamount < photos.size();upamount++){
//            formId = Integer.parseInt(FormUtil.getBxdid());
//            path = photos.get(upamount);
//            new Thread(SubmitfileThread).start();
//
//        }
    }

    private void GetallFormInfo() {
        pdu.showpd();
        new Thread(GetFormInfoThread).start();
    }

    //上传签名JsonStr
    Runnable SubmitSignThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = SingleReimbursementUtil.signJsonString(base64Code, spu.getCert(), spu.getKey(), path, 2, expenseId);

                // JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(expenseItem, expenseCategory, formId, amount, remark);
                if (jo != null) {
                    jsonobj = jo;
                    //  expenseId = jo.getIntValue("expenseId");

                    //  new Thread(SubmitfileThread).start();
                    expenseCategoryback.sendEmptyMessage(4);
                } else {
                    expenseCategoryback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                expenseCategoryback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


    Runnable GetExpenseCategoryInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = ExpenseCategoryUtil.select(spu.getUidNum());
                if (jo != null) {
                    jsonresult = jo;
                    expenseCategoryback.sendEmptyMessage(1);
                } else {
                    expenseCategoryback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                expenseCategoryback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    // 获取三级报销单 fanhui
    Runnable GetFormInfoThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = addForm((Integer) spu.getUidNum(), expenseCategoryId);
                if (jo != null) {
                    jsonresult = jo;
                    expenseCategoryback.sendEmptyMessage(2);
                } else {
                    expenseCategoryback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                expenseCategoryback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    //新增一条报销项
    Runnable SubmitbillThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(spu.getUidNum(),expenseItem, expenseCategory, formId, amount, remark);
                if (jo != null) {
                    jsonobj = jo;
                    expenseId = jo.getIntValue("expenseId");

                    new Thread(SubmitfileThread).start();
                    expenseCategoryback.sendEmptyMessage(3);
                } else {
                    expenseCategoryback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                expenseCategoryback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    Runnable SubmitfileThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = UploadFileUtil.upload("" + expenseId, path);
                if (jo != null) {
                    jsonobj = jo;
                    expenseCategoryback.sendEmptyMessage(3);
                } else {
                    expenseCategoryback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                expenseCategoryback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_expense_type:
                if (isFirst) {
                    if (expandTabView.isShown()) {
                        expandTabView.onExpandPopView();
                    }

                } else {
                    Log.e("---", "--thirdTest");
                }

                break;
            case R.id.iv_photo:
                //选择图片操作
                Log.e("---", "onclick photo");
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setSelected(selectedPhotos)
                        .start(AddExpenseTestActivity.this);
                break;

            case R.id.tv_finish_remark:
                //判空

                Log.e("strToHex=", "=" + FileToBase64Util.str2HexStr("1928"));
                Log.e("hexToStr=", "=" + FileToBase64Util.hexStr2Str("3338"));
                checkData();
                break;
            case R.id.tv_add_expense:
                //select third type
                Intent intent = new Intent(AddExpenseTestActivity.this, ThirdExpenseTypeActivity.class);
                intent.putExtra("id", parentKey);
                startActivityForResult(intent, REQUEST_THIRD_TYPE);
                break;

            default:
                break;
        }
    }


    private void checkData() {
        String tvTitleStr = tvRemarkTitle.getText().toString();
        String edtFeeStr = edtFee.getText().toString();
        String edtRemarkStr = edtRemark.getText().toString();
        if (TextUtils.isEmpty(edtFeeStr)) {
            Toast.makeText(this, "报销金额不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(edtRemarkStr)) {
            Toast.makeText(this, "报销详情不可为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tvTitleStr)) {
            Toast.makeText(this, "请选择报销类别", Toast.LENGTH_SHORT).show();
            return;
        }
        // 判断照片是否为空
        if (selectedPhotos.size() == 0) {
            Toast.makeText(this, "票据照片必选", Toast.LENGTH_SHORT).show();
            return;
        }

        //新增报销项目
        amount = Double.parseDouble(edtFeeStr);
        remark = edtRemarkStr;
        SubmitbillInfo();

        ExpenseItemBean bean = new ExpenseItemBean(tvTitleStr, edtFeeStr, edtRemarkStr, selectedPhotos.get(0));
        addExpenseItemList.add(bean);
        //刷新数据
        if (addExpenseRecycleViewAdapter == null) {
            addExpenseRecycleViewAdapter = new AddExpenseRecycleViewAdapter(this, addExpenseItemList);
        }
        Log.e("addList", "size()=" + addExpenseItemList.size());
        // bxRecyclerView.setAdapter(addExpenseRecycleViewAdapter);
        addExpenseRecycleViewAdapter.notifyDataSetChanged();
        llRemark.setVisibility(View.GONE);
    }


    //destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (expandTabView != null) {
            expandTabView.onExpandPopView();
        }
    }
}

