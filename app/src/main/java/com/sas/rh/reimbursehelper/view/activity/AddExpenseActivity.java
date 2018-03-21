package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.sas.rh.reimbursehelper.Adapter.AddExpenseRecycleViewAdapter;
import com.sas.rh.reimbursehelper.Adapter.PhotoAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Entity.DeptCategoryItemVo;
import com.sas.rh.reimbursehelper.Entity.DeptCategoryItemVoExtend;
import com.sas.rh.reimbursehelper.Entity.SecondCategoryBean;
import com.sas.rh.reimbursehelper.Entity.ExpenseItemBean;
import com.sas.rh.reimbursehelper.Entity.ThirdExpenseCategoryBean;
import com.sas.rh.reimbursehelper.Listener.RecyclerItemClickListener;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseCategoryUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.UploadFileUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopTwoListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static com.sas.rh.reimbursehelper.NetworkUtil.FormUtil.addForm;
//新增报销

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {

    private PhotoAdapter photoAdapter;//选取照片适配器
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView bxRecyclerView;
    private RecyclerView.Adapter bxAdapter;
    private RecyclerView.LayoutManager bxLayoutManager;
    private LinearLayout pickphotos;
    private List<String> photos = null;//已选择照片的张数
    private List<String> xiaofeileixinglist = new ArrayList<String>();//创建一个String类型的数组列表。
    private SharedPreferencesUtil spu;
    private ImageView backbt;
    private TextView photo_amount, saveandaddtv, addandbacktv;
    private Spinner xflxsp;
    private ArrayAdapter<String> xflxadapter;//创建一个数组适配器
    private LinearLayout saveandadd, addandback;
    private int requestcode = 0;
    private List<DeptCategoryItemVoExtend> ERFormList;
    private String expenseCategoryId;

    private JSONArray jsonresult;
    private JSONObject jsonobj;
    private int upamount = 0;
    private Byte expenseItem, expenseCategory;
    private Integer formId;
    private Double amount;
    private String remark;
    private String path;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(AddExpenseActivity.this, "上传提示", "正在提交中");

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

    private LinearLayout llExpenseType;
    //三级类别集合
    //三级类别集合
    private List<ThirdExpenseCategoryBean> thirdExpenseCategoryBeanList = new ArrayList<>();
    private List<KeyValueBean> thirdList = new ArrayList<>();
    private ImageView ivPhoto;

    private PopTwoListView popTwoListView;
    private Handler expenseCategoryback = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (pdu.getMypDialog().isShowing()) {
                pdu.dismisspd();
            }
            if (msg.what == 1) {
                // 获取所有二级报销类别
                ToastUtil.showToast(AddExpenseActivity.this, "加载完毕", Toast.LENGTH_LONG);
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

                ToastUtil.showToast(AddExpenseActivity.this, "提交成功！", Toast.LENGTH_LONG);
            } else if (msg.what == 0) {
                ToastUtil.showToast(AddExpenseActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(AddExpenseActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };
    private String selectSecondType;
    private int expenseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_add_expanse);
        if (requestcode == 2) {
            //bxid = getIntent().getStringExtra("biiid");
        }
        ERFormList = new ArrayList<DeptCategoryItemVoExtend>();
        spu = new SharedPreferencesUtil(AddExpenseActivity.this);

        //二级菜单
        expandTabView = (ExpandPopTabView) findViewById(R.id.expandable_list_view);
        llExpenseType = (LinearLayout) findViewById(R.id.ll_expense_type);
        llRemark = (LinearLayout) findViewById(R.id.ll_remark);
        edtRemark = (EditText) findViewById(R.id.edt_remark);
        tvRemarkTitle = (TextView) findViewById(R.id.tv_remark_title);
        tvRemarkFinish = (TextView) findViewById(R.id.tv_finish_remark);
        edtFee = (EditText) findViewById(R.id.edt_fee);
        ivPhoto = (ImageView) findViewById(R.id.iv_photo);
        tvRemarkFinish.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
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

        addItem(expandTabView, parentsList, childList, "", "合江亭", "区域");
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


    public void addItem(ExpandPopTabView expandTabView, List<KeyValueBean> parentLists,
                        List<ArrayList<KeyValueBean>> childrenListLists, String defaultParentSelect, final String defaultChildSelect, String defaultShowText) {
        popTwoListView = new PopTwoListView(this);
        popTwoListView.setDefaultSelectByValue(defaultParentSelect, defaultChildSelect);
        //distanceView.setDefaultSelectByKey(defaultParent, defaultChild);
        popTwoListView.setCallBackAndData(expandTabView, parentLists, childrenListLists, new PopTwoListView.OnSelectListener() {
            @Override
            public void getValue(String showText, String parentKey, String childrenKey) {
                Log.e("tag", "thirdshowText :" + showText + " ,parentKey :" + parentKey + " ,childrenKey :" + childrenKey);
                //三级ID

                expenseItem=Byte.valueOf(childrenKey);
                //三级为空，则显示二级类别
                if (thirdList.size() == 0) {
                    tvRemarkTitle.setText(selectSecondType);
                } else {
                    tvRemarkTitle.setText(showText);
                }
                llRemark.setVisibility(View.VISIBLE);
//                ivPhoto.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Log.e("---", "onclick photo");
//                    }
//                });

//                PhotoPicker.builder()
//                        .setPhotoCount(1)
//                        .setShowCamera(true)
//                        .setPreviewEnabled(false)
//                        .setSelected(selectedPhotos)
//                        .start(AddExpenseActivity.this);

                edtRemark.setHint("plean fullfill it");

            }

            @Override
            public void getParentValue(int position, String showText, String key) {
                Log.e("tag", "sencondshowText :" + showText + " ,parentKey :" + key);
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

        expenseCategoryId = key;
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
            path=selectedPhotos.get(0);
            //photoAdapter.notifyDataSetChanged();
            Uri uri = Uri.fromFile(new File(selectedPhotos.get(0)));

            Glide.with(this)
                    .load(uri)
                    .thumbnail(0.1f)
                    .into(ivPhoto);
        }
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
                JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(expenseItem, expenseCategory, formId, amount, remark);
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
                if (expandTabView.isShown()) {
                    expandTabView.onExpandPopView();
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
                        .start(AddExpenseActivity.this);
                break;

            case R.id.tv_finish_remark:
                //判空
                checkData();
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
        amount=Double.parseDouble(edtFeeStr);
        remark=edtRemarkStr;
        SubmitbillInfo();

        ExpenseItemBean bean = new ExpenseItemBean(tvTitleStr, edtFeeStr, edtRemarkStr, selectedPhotos.get(0));
        addExpenseItemList.add(bean);
        //刷新数据
        if (addExpenseRecycleViewAdapter == null) {
            addExpenseRecycleViewAdapter = new AddExpenseRecycleViewAdapter(this, addExpenseItemList);
        }
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

