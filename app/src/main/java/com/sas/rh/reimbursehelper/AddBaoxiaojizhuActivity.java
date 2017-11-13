package com.sas.rh.reimbursehelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddBaoxiaojizhuActivity extends AppCompatActivity {

    private List<String> xiaofeileixinglist ;//创建一个String类型的数组列表。
    private List<String> fapiaoleixinglist ;//创建一个String类型的数组列表。

    private ImageView backbt;
    private TextView datepicker,textlenshower;
    private EditText remarket;
    private Spinner xflxsp;
    private Spinner fplxsp;
    private ArrayAdapter<String> xflxadapter;//创建一个数组适配器
    private ArrayAdapter<String> fplxadapter;//创建一个数组适配器


    int mYear, mMonth, mDay;
    Button btn;
    final int DATE_DIALOG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_baoxiaojizhu);
        backbt = (ImageView)findViewById(R.id.backbt) ;
        datepicker = (TextView) findViewById(R.id.datepicker);
        remarket = (EditText) findViewById(R.id.remarket);
        textlenshower = (TextView) findViewById(R.id.textlenshower);
        xflxsp = (Spinner) findViewById(R.id.xflxsp) ;
        fplxsp = (Spinner) findViewById(R.id.fplxsp) ;

        initxflxsp();
        fplxsp();

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
    }

    private void initxflxsp(){
        xiaofeileixinglist = new ArrayList<String>();
        xiaofeileixinglist.add("--请选择--");
        xiaofeileixinglist.add("北京");
        xiaofeileixinglist.add("上海");
        xiaofeileixinglist.add("广州");
        xiaofeileixinglist.add("深圳");
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
}
