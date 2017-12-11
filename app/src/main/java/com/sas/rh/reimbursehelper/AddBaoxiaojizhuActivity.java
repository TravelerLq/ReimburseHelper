package com.sas.rh.reimbursehelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.sas.rh.reimbursehelper.Adapter.PhotoAdapter;
import com.sas.rh.reimbursehelper.Dao.BaoxiaoItem;
import com.sas.rh.reimbursehelper.Listener.RecyclerItemClickListener;
import com.sas.rh.reimbursehelper.Util.DataHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

public class AddBaoxiaojizhuActivity extends AppCompatActivity {

    private PhotoAdapter photoAdapter;//选取照片适配器
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private RecyclerView recyclerView ;
    private LinearLayout pickphotos;
    private List<String> photos = null;//已选择照片的张数

    private List<String> xiaofeileixinglist ;//创建一个String类型的数组列表。
    private List<String> fapiaoleixinglist ;//创建一个String类型的数组列表。

    private ImageView backbt;
    private TextView datepicker,textlenshower,photo_amount,saveandaddtv,addandbacktv;
    private EditText sum,remarket;
    private Spinner xflxsp;
    private Spinner fplxsp;
    private ArrayAdapter<String> xflxadapter;//创建一个数组适配器
    private ArrayAdapter<String> fplxadapter;//创建一个数组适配器
    private LinearLayout saveandadd,addandback ;
    private int requestcode = 0;
    private String bxid;


    int mYear, mMonth, mDay;
    Button btn;
    final int DATE_DIALOG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_add_baoxiaojizhu);
        requestcode = getIntent().getIntExtra("rcode",0);
        if(requestcode == 2){
            bxid = getIntent().getStringExtra("biiid");
        }
        backbt = (ImageView)findViewById(R.id.backbt) ;
        photo_amount = (TextView)findViewById(R.id.photo_amount);
        datepicker = (TextView) findViewById(R.id.datepicker);//日期
        sum = (EditText)findViewById(R.id.sum) ;//金额
        remarket = (EditText) findViewById(R.id.remarket);//备注
        textlenshower = (TextView) findViewById(R.id.textlenshower);
        xflxsp = (Spinner) findViewById(R.id.xflxsp) ;//消费类型
        fplxsp = (Spinner) findViewById(R.id.fplxsp) ;//发票类型
        recyclerView = (RecyclerView)findViewById(R.id.photo_recycler_view);
        pickphotos = (LinearLayout) findViewById(R.id.pickphotos);
        addandback = (LinearLayout)findViewById(R.id.addandback);
        saveandadd = (LinearLayout)findViewById(R.id.saveandadd);
        saveandaddtv = (TextView)findViewById(R.id.saveandaddtv);
        addandbacktv = (TextView)findViewById(R.id.addandbacktv);

        sum.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable edt)
            {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2)
                {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
        });

        initxflxsp();
        fplxsp();


        if(requestcode == 1){
            saveandadd.setVisibility(View.GONE);
            //addandback
            addandback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    savebill();
                }
            });
        }else if(requestcode == 2){
            addandback.setVisibility(View.GONE);
            saveandaddtv.setText("修改并保存");
            BaoxiaoItem bi = getabill(bxid);
            if(bi != null ){
                sum.setText(bi.getSum());
                setSpinnerItemSelectedByValue(xflxsp,bi.getXflxsp().trim());
                setSpinnerItemSelectedByValue(fplxsp,bi.getFplxsp().trim());
                datepicker.setText(bi.getDatepicker());
                remarket.setText(bi.getRemarket());
                if(!bi.getP1().trim().equals("")){
                    selectedPhotos.add(bi.getP1().trim());
                }
                if(!bi.getP2().trim().equals("")){
                    selectedPhotos.add(bi.getP2().trim());
                }
                if(!bi.getP3().trim().equals("")){
                    selectedPhotos.add(bi.getP3().trim());
                }
                if(!bi.getP4().trim().equals("")){
                    selectedPhotos.add(bi.getP4().trim());
                }
                if(!bi.getP5().trim().equals("")){
                    selectedPhotos.add(bi.getP5().trim());
                }
                if(!bi.getP6().trim().equals("")){
                    selectedPhotos.add(bi.getP6().trim());
                }
                if(!bi.getP7().trim().equals("")){
                    selectedPhotos.add(bi.getP7().trim());
                }
                if(!bi.getP8().trim().equals("")){
                    selectedPhotos.add(bi.getP8().trim());
                }
                if(!bi.getP9().trim().equals("")){
                    selectedPhotos.add(bi.getP9().trim());
                }

                photo_amount.setText("已选择"+selectedPhotos.size()+"张发票照片");

                saveandadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alterabill();
                    }
                });
            }
        }


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

        datepicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialog(DATE_DIALOG);
            }
        });



        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);

        remarket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textlenshower.setText(remarket.getText().length()+"/100");
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                photo_amount.setText("已选择"+photos.size()+"张发票照片");
            }
            photoAdapter.notifyDataSetChanged();
        }
    }

    private void initxflxsp(){
        xiaofeileixinglist = new ArrayList<String>();
        xiaofeileixinglist.add("--请选择--");
        xiaofeileixinglist.add("差旅补助");
        xiaofeileixinglist.add("房租水电");
        xiaofeileixinglist.add("通讯费用");
        xiaofeileixinglist.add("采购补贴");
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        xflxadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, xiaofeileixinglist);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
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
                //myTextView.setText("您选择的是：" + xflxadapter.getItem(arg2));//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });

    }

    private void fplxsp(){
        fapiaoleixinglist = new ArrayList<String>();
        fapiaoleixinglist.add("--请选择--");
        fapiaoleixinglist.add("普通发票");
        fapiaoleixinglist.add("增值发票");
        fapiaoleixinglist.add("电子发票");
        //1.为下拉列表定义一个数组适配器，这个数组适配器就用到里前面定义的list。装的都是list所添加的内容
        fplxadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, fapiaoleixinglist);//样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        fplxadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        fplxsp.setAdapter(fplxadapter);
        //4.为下拉列表设置各种点击事件，以响应菜单中的文本item被选中了，用setOnItemSelectedListener
        fplxsp.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
                //myTextView.setText("您选择的是：" + xflxadapter.getItem(arg2));//文本说明
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                //myTextView.setText("Nothing");
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        datepicker.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    //填写完信息后，把数据保存到sqlite数据库
    public void savebill(){


        if(selectedPhotos.size() == 0){
            Toast.makeText(getApplicationContext(), "请选择票据原照",Toast.LENGTH_SHORT).show();
            return;
        }
        if(sum.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请填写报销金额",Toast.LENGTH_SHORT).show();
            return;
        }
        if(xflxsp.getSelectedItem().toString().trim().equals("--请选择--")){
            Toast.makeText(getApplicationContext(), "请选择消费类型",Toast.LENGTH_SHORT).show();
            return;
        }
        if(fplxsp.getSelectedItem().toString().trim().equals("--请选择--")){
            Toast.makeText(getApplicationContext(), "请选择发票类型",Toast.LENGTH_SHORT).show();
            return;
        }
        if(datepicker.getText().toString().trim().equals("点此选择日期")){
            Toast.makeText(getApplicationContext(), "请选择消费日期",Toast.LENGTH_SHORT).show();
            return;
        }
        if(remarket.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请填写备注",Toast.LENGTH_SHORT).show();
            return;
        }
        DecimalFormat df = new DecimalFormat( "#####0.00 ");
        String newsum = df.format(Double.parseDouble(sum.getText().toString().trim()));
        BaoxiaoItem bi =new BaoxiaoItem(getSerriesNumber().trim(),
                newsum,
                xflxsp.getSelectedItem().toString().trim(),
                fplxsp.getSelectedItem().toString().trim(),
                datepicker.getText().toString().trim(),
                remarket.getText().toString().trim(),
                selectedPhotos.size() >= 1 ? selectedPhotos.get(0) : "",
                selectedPhotos.size() >= 2 ? selectedPhotos.get(1) : "",
                selectedPhotos.size() >= 3 ? selectedPhotos.get(2) : "",
                selectedPhotos.size() >= 4 ? selectedPhotos.get(3) : "",
                selectedPhotos.size() >= 5 ? selectedPhotos.get(4) : "",
                selectedPhotos.size() >= 6 ? selectedPhotos.get(5) : "",
                selectedPhotos.size() >= 7 ? selectedPhotos.get(6) : "",
                selectedPhotos.size() >= 8 ? selectedPhotos.get(7) : "",
                selectedPhotos.size() >= 9 ? selectedPhotos.get(8) : "");

            Boolean addsuc = DataHelper.addBaoxiaoItem(new DataHelper(AddBaoxiaojizhuActivity.this,"BaoxiaoItem_DB",null,1),bi);
            if(addsuc == true){
                Toast.makeText(getApplicationContext(), "添加成功",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "添加失败",Toast.LENGTH_SHORT).show();
            }

    }

    public BaoxiaoItem getabill(String billid){
        BaoxiaoItem bi =  DataHelper.getABaoxiaoItem(new DataHelper(AddBaoxiaojizhuActivity.this,"BaoxiaoItem_DB",null,1),billid);
        if(bi == null){
            Toast.makeText(getApplicationContext(), "无此数据，请返回主界面刷新后重试！",Toast.LENGTH_SHORT).show();
        }
        return bi;
    }

    public void alterabill(){


        if(selectedPhotos.size() == 0){
            Toast.makeText(getApplicationContext(), "请选择票据原照",Toast.LENGTH_SHORT).show();
            return;
        }
        if(sum.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请填写报销金额",Toast.LENGTH_SHORT).show();
            return;
        }
        if(xflxsp.getSelectedItem().toString().trim().equals("--请选择--")){
            Toast.makeText(getApplicationContext(), "请选择消费类型",Toast.LENGTH_SHORT).show();
            return;
        }
        if(fplxsp.getSelectedItem().toString().trim().equals("--请选择--")){
            Toast.makeText(getApplicationContext(), "请选择发票类型",Toast.LENGTH_SHORT).show();
            return;
        }
        if(datepicker.getText().toString().trim().equals("点此选择日期")){
            Toast.makeText(getApplicationContext(), "请选择消费日期",Toast.LENGTH_SHORT).show();
            return;
        }
        if(remarket.getText().toString().trim().equals("")){
            Toast.makeText(getApplicationContext(), "请填写备注",Toast.LENGTH_SHORT).show();
            return;
        }

        BaoxiaoItem bi =new BaoxiaoItem(bxid,
                sum.getText().toString().trim(),
                xflxsp.getSelectedItem().toString().trim(),
                fplxsp.getSelectedItem().toString().trim(),
                datepicker.getText().toString().trim(),
                remarket.getText().toString().trim(),
                selectedPhotos.size() >= 1 ? selectedPhotos.get(0) : "",
                selectedPhotos.size() >= 2 ? selectedPhotos.get(1) : "",
                selectedPhotos.size() >= 3 ? selectedPhotos.get(2) : "",
                selectedPhotos.size() >= 4 ? selectedPhotos.get(3) : "",
                selectedPhotos.size() >= 5 ? selectedPhotos.get(4) : "",
                selectedPhotos.size() >= 6 ? selectedPhotos.get(5) : "",
                selectedPhotos.size() >= 7 ? selectedPhotos.get(6) : "",
                selectedPhotos.size() >= 8 ? selectedPhotos.get(7) : "",
                selectedPhotos.size() >= 9 ? selectedPhotos.get(8) : "");

        Boolean altersuc = DataHelper.alterABill(new DataHelper(AddBaoxiaojizhuActivity.this,"BaoxiaoItem_DB",null,1),bi);
        if(altersuc == true){
            Toast.makeText(getApplicationContext(), "修改成功",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "修改失败",Toast.LENGTH_SHORT).show();
        }
    }

    public String getSerriesNumber(){
        //method 1
        Calendar nowtime = new GregorianCalendar();
        //String strDateTime =
        String strDateTime = String.format("%04d", nowtime.get(Calendar.YEAR))+String.format("%02d", nowtime.get(Calendar.MONTH)+1) + String.format("%02d", nowtime.get(Calendar.DATE)) +
                String.format("%02d", nowtime.get(Calendar.HOUR)) + String.format("%02d", nowtime.get(Calendar.MINUTE)) + String.format("%02d", nowtime.get(Calendar.SECOND)) +
                String.format("%03d", nowtime.get(Calendar.MILLISECOND));
        //System.out.println("Time:>>>>>>>>>>>>>>>>>>>>>"+strDateTime);
        return "00000020170701"+strDateTime;
    }

    public void setSpinnerItemSelectedByValue(Spinner spinner,String value){
        SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
        int k= apsAdapter.getCount();
        for(int i=0;i<k;i++){
            if(value.equals(apsAdapter.getItem(i).toString())){
                spinner.setSelection(i,true);// 默认选中项
                break;
            }
        }
    }


}
