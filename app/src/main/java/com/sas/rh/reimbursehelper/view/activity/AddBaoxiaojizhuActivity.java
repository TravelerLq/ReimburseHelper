package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
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
import com.crashlytics.android.Crashlytics;
import com.sas.rh.reimbursehelper.Adapter.PhotoAdapter;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.DeptCategoryItemVo;
import com.sas.rh.reimbursehelper.Bean.DeptCategoryItemVoExtend;
import com.sas.rh.reimbursehelper.Listener.RecyclerItemClickListener;
import com.sas.rh.reimbursehelper.Bean.SecondCategoryBean;
import com.sas.rh.reimbursehelper.NetworkUtil.ExpenseCategoryUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.SingleReimbursementUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.UploadFileUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.Utils;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

import static com.sas.rh.reimbursehelper.NetworkUtil.FormUtil.addForm;

public class AddBaoxiaojizhuActivity extends AppCompatActivity {

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
    private Byte expenseCategoryId;
    private JSONArray jsonresult;
    private JSONObject jsonobj;
    private int upamount = 0;
    private Byte expenseItem, expenseCategory;
    private Integer formId;
    private Double amount;
    private String remark;
    private String path;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(AddBaoxiaojizhuActivity.this, "上传提示", "正在提交中");


    private Handler expenseCategoryback = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (pdu.getMypDialog().isShowing()) {
                pdu.dismisspd();
            }
            if (msg.what == 1) {
                // 获得所有二级报销类别
//                    System.out.println("ResultCode:" + jsonresult.get("ResultCode") + "\t" + "HostTime:"
//            + jsonresult.get("HostTime") + "\t" + "Note:" + jsonresult.get("Note"));
                ToastUtil.showToast(AddBaoxiaojizhuActivity.this, "加载完毕", Toast.LENGTH_LONG);
                List<SecondCategoryBean> expenseReimbursementFormList = JSONArray.parseArray(jsonresult.toJSONString(), SecondCategoryBean.class);
                xiaofeileixinglist = new ArrayList<String>();
                for (SecondCategoryBean object : expenseReimbursementFormList) {
                    xiaofeileixinglist.add(object.getExpenseCategoryName());
                }
                //System.out.println("========="+xiaofeileixinglist.size());
                initxflxsp();
//                if(jsonresult != null){
//                    finish();
//                }
            } else if (msg.what == 2) {
                ERFormList = new ArrayList<DeptCategoryItemVoExtend>();
                List<DeptCategoryItemVo> deptCategoryItemVoList =
                        JSONArray.parseArray(jsonresult.toJSONString(), DeptCategoryItemVo.class);
                for (int i = 0; i < deptCategoryItemVoList.size(); i++) {
                    if (deptCategoryItemVoList.get(i).getItem() != null) {
                        DeptCategoryItemVoExtend dcive = new DeptCategoryItemVoExtend();
                        dcive.setItem(deptCategoryItemVoList.get(i).getItem());
                        dcive.setItemName(deptCategoryItemVoList.get(i).getItemName());
                        dcive.setCategory(deptCategoryItemVoList.get(i).getCategory());
                        dcive.setCategoryName(deptCategoryItemVoList.get(i).getCategoryName());
                        dcive.setRemark(deptCategoryItemVoList.get(i).getRemark());
                        ERFormList.add(dcive);
                    }
                }
                initBxRclv();
            } else if (msg.what == 3) {

                ToastUtil.showToast(AddBaoxiaojizhuActivity.this, "提交成功！", Toast.LENGTH_LONG);
            } else if (msg.what == 0) {
                ToastUtil.showToast(AddBaoxiaojizhuActivity.this, "通信异常，请检查网络连接！", Toast.LENGTH_LONG);
            } else if (msg.what == -1) {
                ToastUtil.showToast(AddBaoxiaojizhuActivity.this, "通信模块异常！", Toast.LENGTH_LONG);
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_add_baoxiaojizhu);
        if (requestcode == 2) {
            //bxid = getIntent().getStringExtra("biiid");
        }
        ERFormList = new ArrayList<DeptCategoryItemVoExtend>();
        spu = new SharedPreferencesUtil(AddBaoxiaojizhuActivity.this);
        backbt = (ImageView) findViewById(R.id.backbt);
        photo_amount = (TextView) findViewById(R.id.photo_amount);
        xflxsp = (Spinner) findViewById(R.id.xflxsp);//消费类型
        recyclerView = (RecyclerView) findViewById(R.id.photo_recycler_view);
        pickphotos = (LinearLayout) findViewById(R.id.pickphotos);
        addandback = (LinearLayout) findViewById(R.id.addandback);
        saveandadd = (LinearLayout) findViewById(R.id.saveandadd);
        saveandaddtv = (TextView) findViewById(R.id.saveandaddtv);
        addandbacktv = (TextView) findViewById(R.id.addandbacktv);

        GetallDepartmentInfo();


        saveandadd.setVisibility(View.GONE);
        //addandback
        addandback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubmitbillInfo();
            }
        });

        //照片选择器初始化
        photoAdapter = new PhotoAdapter(this, selectedPhotos);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        recyclerView.setAdapter(photoAdapter);

        pickphotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPicker.builder()
                        .setPhotoCount(9)
                        .setGridColumnCount(4)
                        .start(AddBaoxiaojizhuActivity.this);
            }
        });
        backbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //照片展示区
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (photoAdapter.getItemViewType(position) == PhotoAdapter.TYPE_ADD) {
                            PhotoPicker.builder()
                                    .setPhotoCount(PhotoAdapter.MAX)
                                    .setShowCamera(true)
                                    .setPreviewEnabled(false)
                                    .setSelected(selectedPhotos)
                                    .start(AddBaoxiaojizhuActivity.this);
                        } else {
                            PhotoPreview.builder()
                                    .setPhotos(selectedPhotos)
                                    .setCurrentItem(position)
                                    .start(AddBaoxiaojizhuActivity.this);
                        }
                    }
                }));
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
                photo_amount.setText("已选择" + photos.size() + "张发票照片");
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    private void initBxRclv() {
        bxLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        bxAdapter = new MyAdapter();
        bxRecyclerView = (RecyclerView) findViewById(R.id.billlist);
        // 设置布局管理器
        bxRecyclerView.setLayoutManager(bxLayoutManager);
        // 设置adapter
        bxRecyclerView.setAdapter(bxAdapter);
    }

    private void initxflxsp() {

        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        xflxadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, xiaofeileixinglist);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        xflxadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        xflxsp.setAdapter(xflxadapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        xflxsp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                // expenseCategoryId = (byte)(arg2+1);

                //GetallFormInfo();
                //myTextView.setText("您选择的是：" + xflxadapter.getItem(arg2));//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });

    }

    public void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

    private void GetallDepartmentInfo() {
        pdu.showpd();
        new Thread(GetExpenseCategoryInfoThread).start();
    }

    private void SubmitbillInfo() {
        pdu.showpd();
        for (upamount = 0; upamount < ERFormList.size(); upamount++) {
            expenseItem = ERFormList.get(upamount).getItem();
            expenseCategory = ERFormList.get(upamount).getCategory();
            formId = Integer.parseInt(FormUtil.getBxdid());
            amount = ERFormList.get(upamount).getAmount();
            remark = ERFormList.get(upamount).getRemark_written();
            new Thread(SubmitbillThread).start();
        }
        //pdu.showpd();
//        for(upamount = 0;upamount < photos.size();upamount++){
//            formId = Integer.parseInt(FormUtil.getBxdid());
//            path = photos.get(upamount);
        new Thread(SubmitfileThread).start();
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

    Runnable SubmitbillThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub


            try {
                JSONObject jo = SingleReimbursementUtil.addSingleReimbursement(spu.getUidNum(), expenseItem, expenseCategory,
                        formId, amount, remark, Utils.strToDate("2018-1-20"));
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

    Runnable SubmitfileThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = UploadFileUtil.upload("" + formId, path);
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

    //填写报销内容的内部类
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        //private List<DeptCategoryItemVoExtend>  mDataset;

//        public MyAdapter(List<DeptCategoryItemVoExtend> myDataset) {
//           // mDataset = myDataset;
//        }

        public MyAdapter() {
            // mDataset = myDataset;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baoxiao_item, parent, false);
            // pass MyCustomEditTextListener to viewholder in onCreateViewHolder
            // so that we don't have to do this expensive allocation in onBindViewHolder
            ViewHolder vh = new ViewHolder(v, new BxAmountEditTextListener(), new BxContentEditTextListener());

            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            // update MyCustomEditTextListener every time we bind a new item
            // so that it knows what item in mDataset to update
            holder.bxamountEditTextListener.updatePosition(holder.getAdapterPosition());
            holder.bxcontentEditTextListener.updatePosition(holder.getAdapterPosition());
            if (ERFormList.get(holder.getAdapterPosition()).getItem().toString().equals("0")) {
                holder.bxtitle.setText(ERFormList.get(holder.getAdapterPosition()).getCategoryName());
                holder.bxcontent.setHint(ERFormList.get(holder.getAdapterPosition()).getRemark());
            } else {
                holder.bxtitle.setText(ERFormList.get(holder.getAdapterPosition()).getItemName());
                holder.bxcontent.setHint(ERFormList.get(holder.getAdapterPosition()).getRemark());
            }

            //holder.bxcontent.setText(mDataset[holder.getAdapterPosition()]);
        }

        @Override
        public int getItemCount() {
            return ERFormList.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public EditText bxcontent;
            public TextView bxtitle, bxamount;
            public BxContentEditTextListener bxcontentEditTextListener;
            public BxAmountEditTextListener bxamountEditTextListener;

            public ViewHolder(View v, BxAmountEditTextListener bxamountEditTextListener, BxContentEditTextListener bxcontentEditTextListener) {
                super(v);
                this.bxtitle = (TextView) v.findViewById(R.id.bxtitle);
                this.bxamount = (TextView) v.findViewById(R.id.bxamount);
                this.bxcontent = (EditText) v.findViewById(R.id.bxcontent);
                this.bxamountEditTextListener = bxamountEditTextListener;
                this.bxcontentEditTextListener = bxcontentEditTextListener;
                this.bxamount.addTextChangedListener(bxamountEditTextListener);
                this.bxcontent.addTextChangedListener(bxcontentEditTextListener);

            }
        }

        // we make TextWatcher to be aware of the position it currently works with
        // this way, once a new item is attached in onBindViewHolder, it will
        // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
        private class BxAmountEditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // no op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //mDataset[position] = charSequence.toString();
                ERFormList.get(position).setAmount(Double.parseDouble(charSequence.toString()));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // no op
            }
        }

        private class BxContentEditTextListener implements TextWatcher {
            private int position;

            public void updatePosition(int position) {
                this.position = position;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                // no op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //mDataset[position] = charSequence.toString();
                ERFormList.get(position).setRemark_written(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // no op
            }
        }
    }


}

