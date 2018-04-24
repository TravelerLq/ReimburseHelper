package com.sas.rh.reimbursehelper.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.sas.rh.reimbursehelper.Adapter.AddExpenseRecycleViewAdapter;
import com.sas.rh.reimbursehelper.Adapter.PhotoAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.DeptCategoryItemVoExtend;
import com.sas.rh.reimbursehelper.Bean.ExpenseThirdTypeBean;
import com.sas.rh.reimbursehelper.Bean.SecondCategoryBean;
import com.sas.rh.reimbursehelper.Bean.ExpenseItemBean;
import com.sas.rh.reimbursehelper.Bean.SingleReimbursement;
import com.sas.rh.reimbursehelper.Bean.ThirdExpenseCategoryBean;
import com.sas.rh.reimbursehelper.NetworkUtil.DownloadFileUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseCategoryUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseItemUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.UploadFileUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.FileToBase64Util;
import com.sas.rh.reimbursehelper.Util.FileUtils;
import com.sas.rh.reimbursehelper.Util.IntentUtils;
import com.sas.rh.reimbursehelper.Util.JsonStringUtil;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;
import com.warmtel.expandtab.ExpandPopTabView;
import com.warmtel.expandtab.KeyValueBean;
import com.warmtel.expandtab.PopTwoListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

public class AddExpenseActivity extends AppCompatActivity implements View.OnClickListener {
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
    private ProgressDialogUtil pdu = new ProgressDialogUtil(AddExpenseActivity.this, "提示", "正在加载中");

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
    private int index = 1;
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
    private String selectSecondType;
    private int expenseId;
    private String parentKey;
    private String base64CodeTwo;
    private String base64Code;


    private Uri uri;
    private String signJson1;
    private TextView tvPdfForm;
    private TextView tvSubmitPdf;
    private ImageView ivPdfIcon;
    private ImageView ivBack;
    private String pdfPath = "/storage/emulated/0/Download/";
    private Context context;
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

                if (expenseReimbursementFormList.size() == 0) {
                    ToastUtil.showToast(AddExpenseActivity.this, "暂无数据，请重试！", Toast.LENGTH_LONG);
                    return;
                }
                xiaofeileixinglist = new ArrayList<String>();
                //是为了一进入，就默认获得第一个二级 的三级
                int i = 0;
                for (SecondCategoryBean object : expenseReimbursementFormList) {
                    i++;
                    //
                    xiaofeileixinglist.add(object.getExpenseCategoryName());

                    if (i == 1) {
                        String defaultParentId = object.getExpenseCategoryId();
                        getThirdCategory(0, defaultParentId);
                    }
                    parentsList.add(new KeyValueBean(object.getExpenseCategoryId(), object.getExpenseCategoryName()));


                }
                initExpandaTabView();

            } else if (msg.what == 2) {
                thirdList.clear();
                Loger.e("third type--jsonresult-----"+jsonresult);
                //   ERFormList = new ArrayList<DeptCategoryItemVoExtend>();
                List<ExpenseThirdTypeBean> thirdExpenseCategoryList =
                        JSONArray.parseArray(jsonresult.toJSONString(), ExpenseThirdTypeBean.class);
                Loger.e("thirdExpenseCategoryList--get"+thirdExpenseCategoryList.size());

                for (int i = 0; i < thirdExpenseCategoryList.size(); i++) {
                    Loger.e("thirdExpenseCategoryList-name"+thirdExpenseCategoryList.get(i).getExpenseItemName());
                    if (thirdExpenseCategoryList.get(i).getExpenseItemId() != null) {
                        ExpenseThirdTypeBean bean = thirdExpenseCategoryList.get(i);
                        KeyValueBean keyValueBean = new KeyValueBean(bean.getExpenseItemId().toString(), bean.getExpenseItemName());
                        thirdList.add(keyValueBean);
                    }


                }
                for (int i = 0; i < thirdList.size(); i++) {
                    Log.e("thirdListNewGet==", "" + thirdList.get(i).getValue());
                }
                popTwoListView.refreshChild(thirdList);


                // initBxRclv();
            } else if (msg.what == 3) {
                //开始上传照片 提交报销项成功－返回expenseId 进行图片验签
                Loger.e("pic submitJson-expenseId--" + expenseId);
                if (expenseId != 0) {
                    submitSignJsonstring();
                }


                //ToastUtil.showToast(AddExpenseActivity.this, "提交成功！", Toast.LENGTH_LONG);
            } else if (msg.what == 4) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                //上传 签名成功
                int code = jsonobj.getIntValue("code");

                if (code == 200) {
                    if (signType.equals("pic")) {
                        ToastUtil.showToast(AddExpenseActivity.this, "提交成功！", Toast.LENGTH_LONG);

                        ExpenseItemBean bean = new ExpenseItemBean(tvTitleStr, edtFeeStr, edtRemarkStr, selectedPhotos.get(0));
                        addExpenseItemList.add(bean);
                        //刷新数据
                        if (addExpenseRecycleViewAdapter == null) {
                            addExpenseRecycleViewAdapter = new AddExpenseRecycleViewAdapter(AddExpenseActivity.this, addExpenseItemList);
                        }
                        Log.e("addList", "size()=" + addExpenseItemList.size());
                        // bxRecyclerView.setAdapter(addExpenseRecycleViewAdapter);
                        addExpenseRecycleViewAdapter.notifyDataSetChanged();
                        llRemark.setVisibility(View.GONE);


//                        Glide.with(AddExpenseActivity.this)
//                                .load(uri)
//                                .thumbnail(0.1f)
//                                .into(ivPhoto);
                    } else {
                        ToastUtil.showToast(AddExpenseActivity.this, "文件提交成功！", Toast.LENGTH_LONG);
//                        if(file.exists()){
//                            startActivity(IntentUtils.getPdfFileIntent(file,AddExpenseActivity.this));
//                        }

                        // startActivity(IntentUtils.getPdfIntent(file));

                    }

                } else {
                    ToastUtil.showToast(AddExpenseActivity.this, "提交失败，请重试！", Toast.LENGTH_LONG);
                }


            } else if (msg.what == 5) {
                if (annexId != null) {
                    //去下载pdf
                    toDownLoadPdf();
                }

            } else if (msg.what == 6) {
                //download finish
                pdfBase64Str = pdfJsonObjec.getString("file");

                if (!TextUtils.isEmpty(pdfBase64Str)) {
                    //签名
                    signType = "pdf";
                    signVerifyP1(pdfBase64Str);
                }
                String originalFilename = pdfJsonObjec.getString("originalFilename");
                pdfPath = FileUtils.getExternalFilesDirPath(context);

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
                    ivPdfIcon.setVisibility(View.VISIBLE);
                    //是否有pdf生成的标志


                } else {
                    ivPdfIcon.setVisibility(View.GONE);
                    Toast.makeText(AddExpenseActivity.this, "生成pdf出错", Toast.LENGTH_SHORT).show();
                }
                pdfPathName = file.getPath();

                //   startActivity(IntentUtils.getPdfFileIntent(file,AddExpenseActivity.this));
                // startActivity(IntentUtils.getPdfIntent(file));
            } else if (msg.what == 7) {

                if (TextUtils.isEmpty(FormUtil.getBxdid())) {
                    Log.e("---formId=null", "null");
                }
                formId = Integer.parseInt(FormUtil.getBxdid());
            } else if (msg.what == 0) {
                ToastUtil.showToast(AddExpenseActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(AddExpenseActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };


    private Integer annexId;
    private JSONObject pdfJsonObjec;
    private String pdfBase64Str;
    private File pdfFile;
    private File file;
    private String tvTitleStr;
    private String edtFeeStr;
    private String edtRemarkStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        isFirst = true;
        context = AddExpenseActivity.this;
        setContentView(R.layout.activity_add_expanse);
        if (requestcode == 2) {
            //bxid = getIntent().getStringExtra("biiid");s
        }
        //  ERFormList = new ArrayList<DeptCategoryItemVoExtend>();
        spu = new SharedPreferencesUtil(AddExpenseActivity.this);
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
        tvPdfForm = (TextView) findViewById(R.id.tv_pdf_form);

        tvSubmitPdf = (TextView) findViewById(R.id.tv_submit_pdf);
        tvSubmitPdf.setOnClickListener(this);

        ivPdfIcon = (ImageView) findViewById(R.id.iv_pdf_icon);
        ivBack = (ImageView) findViewById(R.id.backbt);

        tvPdfForm.setOnClickListener(this);
        tvRemarkFinish.setOnClickListener(this);
        ivPhoto.setOnClickListener(this);
        tvAddExpense.setOnClickListener(this);
        ivPdfIcon.setOnClickListener(this);
        ivBack.setOnClickListener(this);


//        ivPdfIcon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                 startActivity(IntentUtils.getPdfFileIntent(file,AddExpenseActivity.this));
//                     startActivity(IntentUtils.getPdfIntent(file));
//            }
//        });


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

    //   String defaultabTitle = "请选择";

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
                expenseItem = Byte.valueOf(childrenKey);


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
                isFirst = true;
                parentKey = key;
                selectSecondType = showText;
                //报销科目2级ID
                expenseCategory = Byte.valueOf(key);
                if (isFirst) {
                    getFormId();
                }
//

                //根据 parentID，获取三级

                getThirdCategory(position, key);


            }

        });
        expandTabView.addItemToExpandTab(defaultShowText, popTwoListView);
    }

    private void getFormId() {

        new Thread(GetFormIdRunable).start();
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
            String compressPath = compress(path);

            //    Log.e("path=", "=" + path + "---comPressPAth--" + compressPath);

            //photoAdapter.notifyDataSetChanged();

            //compress pic
            //  uri = uri = Uri.fromFile(new File(compressPath));
            uri = Uri.fromFile(new File(selectedPhotos.get(0)));
            try {

                String base64CodePic = FileToBase64Util.encodeBase64File(path);
                //   String base64CodePic = FileToBase64Util.encodeBase64File(compressPath);
                signType = "pic";
                signVerifyP1(base64CodePic);

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
//                e.printStackTrace();sh
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

                if (!TextUtils.isEmpty(thirdKey)) {
                    expenseItem = Byte.valueOf(thirdKey);

                    Log.e("expenseItem==", "=" + expenseItem);
                } else {
                    Toast.makeText(AddExpenseActivity.this, "请选择三级报销类别", Toast.LENGTH_SHORT).show();
                }


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

        AddExpenseActivity.this.getIntent().putExtra("data", plantext);
        AddExpenseActivity.this.getIntent().putExtra("type", DataProcessType.SIGNATURE_P1.name());
        SignatureP1Service signatureP1Service = new SignatureP1Service(AddExpenseActivity.this, new ProcessListener<DataProcessResponse>() {
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
                    Toast.makeText(AddExpenseActivity.this, "图片签名失败" + dataProcessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void doException(CmSdkException e) {
                Toast.makeText(AddExpenseActivity.this, "图片签名失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    //签名验证
    private void signVerifyP1(final String base64Code1) {
        pdu.showpd();

        String plantext = base64Code1;
        base64Code = base64Code1;

        AddExpenseActivity.this.getIntent().putExtra("data", plantext);
        AddExpenseActivity.this.getIntent().putExtra("type", DataProcessType.SIGNATURE_P1.name());
        SignatureP1Service signatureP1Service = new SignatureP1Service(AddExpenseActivity.this, new ProcessListener<DataProcessResponse>() {
            @Override
            public void doFinish(DataProcessResponse dataProcessResponse, String certificate) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                if (dataProcessResponse.getRet() == 0) {
                    Log.e("密钥", "= " + dataProcessResponse.getResult());
                    spu.setCertKey(dataProcessResponse.getResult());
                    //获得就是签名证书
                    Log.e("cert", "= " + certificate);
                    spu.setCert(certificate);
//                    signJson1 = SingleReimbursementUtil.jsontoStr(base64Code1, certificate, dataProcessResponse.getResult(), path, 1, expenseId);
//                    if (!TextUtils.isEmpty(signJson1)) {
//
//                        singTwoVerifyP1(signJson1);
//                    }

                    //只有点击生成报销单时，才去上传签了名字的JsonString
                    if (signType.equals("pdf")) {

                        submitSignJsonstring();
                    } else {
                        // if pic -保存picCert 和picKey  显示图片即可

                        Glide.with(AddExpenseActivity.this)
                                .load(uri)
                                .thumbnail(0.1f)
                                .into(ivPhoto);

                    }


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
                    if (pdu.getMypDialog().isShowing()) {
                        pdu.dismisspd();
                    }
                    Toast.makeText(AddExpenseActivity.this, "图片签名失败" + dataProcessResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void doException(CmSdkException e) {
                if (pdu.getMypDialog().isShowing()) {
                    pdu.dismisspd();
                }
                Toast.makeText(AddExpenseActivity.this, "图片签名失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitSignJsonstring() {
        pdu.showpd();
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


    private String signType;
    private String signPath;
    private String pdfPathName;
    private String certStr;
    private String keyStr;
    //上传签名JsonStr
    Runnable SubmitSignThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (signType.equals("pic")) {
                signPath = path;
            } else {
                base64Code = pdfBase64Str;
                signPath = pdfPathName;
            }
            Log.e("signPath--", "--" + signPath);
            try {

                //  String result=  SingleReimbursementUtil.signJsonStringPdfTest(base64Code, spu.getCert(), spu.getKey(), signPath, index, expenseId);
                JSONObject jo = SingleReimbursementUtil.signJsonStringNew(base64Code, spu.getCert(), spu.getKey(), signPath, index, expenseId);

                //  JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(expenseItem, expenseCategory, formId, amount, remark);
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
                JSONArray jo = ExpenseItemUtil.getThirdCategory(spu.getUidNum(), expenseCategoryId);
                if (jo != null) {
                    jsonresult = jo;
                    expenseCategoryback.sendEmptyMessage(2);
                    Loger.e("third type--send");
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
                Log.e("expenseItem=", "" + expenseItem);

                JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(spu.getUidNum(), expenseItem, expenseCategory, formId, amount, remark);
                if (jo != null) {
                    jsonobj = jo;
                    expenseId = jo.getIntValue("expenseId");
                    Loger.e("expendId--" + expenseId);

                    // new Thread(SubmitfileThread).start();
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


    private JSONArray jsonFormId;
    // 获取formId
    Runnable GetFormIdRunable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = FormUtil.addForm(spu.getUidNum(), expenseCategory);
                if (jo != null) {
                    jsonFormId = jo;
                    expenseCategoryback.sendEmptyMessage(7);
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

    //得到需要生成pdf的 annexId
    Runnable GetPdfFormRunnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = FormUtil.getFormPdf(formId, spu.getUidNum());
                if (jo != null) {
                    jsonobj = jo;
                    annexId = jsonobj.getInteger("annexId");
                    expenseCategoryback.sendEmptyMessage(5);
                } else {
                    expenseCategoryback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                expenseCategoryback.sendEmptyMessage(-1);
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
                    expenseCategoryback.sendEmptyMessage(6);
                } else {
                    expenseCategoryback.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                expenseCategoryback.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };

    private void getPdfForm() {
        new Thread(GetPdfFormRunnable).start();
    }

    private void toDownLoadPdf() {

        new Thread(DownLoadPdfRunnable).start();

    }


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
                        .start(AddExpenseActivity.this);
                break;

            case R.id.tv_finish_remark:
                //判空
                Log.e("strToHex=", "=" + FileToBase64Util.str2HexStr("1928"));
                Log.e("hexToStr=", "=" + FileToBase64Util.hexStr2Str("3338"));
                checkData();
                break;
            case R.id.tv_add_expense:
                //select third type
                Intent intent = new Intent(AddExpenseActivity.this, ThirdExpenseTypeActivity.class);
                intent.putExtra("id", parentKey);
                startActivityForResult(intent, REQUEST_THIRD_TYPE);
                break;
            case R.id.tv_pdf_form:
                getPdfForm();
                break;
            case R.id.tv_submit_pdf:
                //pdfbase64 签名 －生成key后 组合一下objectStr提交,成功后 finish 回到主页面
                if (ivPdfIcon.getVisibility() == View.VISIBLE) {
                    finish();
                } else {
                    Toast.makeText(context, "您还未去生成报销单！", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_pdf_icon:
                startActivity(IntentUtils.getPdfFileIntent(file, AddExpenseActivity.this));
                // startActivity(IntentUtils.getPdfIntent(file));
                break;
            case R.id.backbt:
                finish();
                break;

            default:
                break;
        }
    }


    private void checkData() {
        tvTitleStr = tvRemarkTitle.getText().toString();
        edtFeeStr = edtFee.getText().toString();
        edtRemarkStr = edtRemark.getText().toString();
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

//        ExpenseItemBean bean = new ExpenseItemBean(tvTitleStr, edtFeeStr, edtRemarkStr, selectedPhotos.get(0));
//        addExpenseItemList.add(bean);
//        //刷新数据
//        if (addExpenseRecycleViewAdapter == null) {
//            addExpenseRecycleViewAdapter = new AddExpenseRecycleViewAdapter(this, addExpenseItemList);
//        }
//        Log.e("addList", "size()=" + addExpenseItemList.size());
//        // bxRecyclerView.setAdapter(addExpenseRecycleViewAdapter);
//        addExpenseRecycleViewAdapter.notifyDataSetChanged();
//        llRemark.setVisibility(View.GONE);
    }


    //destroy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (expandTabView != null) {
            expandTabView.onExpandPopView();
        }
    }

    //图片压缩
    private DisplayMetrics dm;

    public String compress(String srcPath) {
        String newPath = srcPath;
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }
        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > 45 * 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 20;
            System.out.println(baos.toByteArray().length);
        }
        try {

            baos.writeTo(new FileOutputStream(newPath));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newPath;

    }
}

