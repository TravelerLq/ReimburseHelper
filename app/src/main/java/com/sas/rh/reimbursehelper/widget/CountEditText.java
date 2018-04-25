package com.sas.rh.reimbursehelper.widget;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sas.rh.reimbursehelper.R;

import butterknife.InjectView;


/**
 * 动态计算Editext输入文字个数
 * Created by liqing on 17/5/2.
 */

public class CountEditText extends RelativeLayout {

    //类型1(单数类型)：TextView显示总字数，然后根据输入递减.例：100，99，98
    //类型2(百分比类型)：TextView显示总字数和当前输入的字数，例：0/100，1/100，2/100
    public static final String SINGULAR = "Singular";//类型1(单数类型)
    public static final String PERCENTAGE = "Percentage";//类型2(百分比类型)
    public static final String TAG = "CountEditText";
    @InjectView(R.id.count_Num)
    TextView countNum;//字数显示TextView
    @InjectView(R.id.etContent)
    EditText etContent;//文本框

    private View vLine;//底部横线
    private String TYPES = PERCENTAGE;//类型
    private int MaxNum = 100;//最大字符

    public CountEditText(Context context) {
        this(context, null);
    }

    public CountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.edit_count, this, true);
        etContent = (EditText) findViewById(R.id.etContent);
        countNum = (TextView) findViewById(R.id.count_Num);

    }

    /**
     * 设置显示
     *
     * @return
     */
    public CountEditText show() {
        if (TYPES.equals(SINGULAR)) {//类型1
            countNum.setText(String.valueOf(MaxNum));
        } else if (TYPES.equals(PERCENTAGE)) {//类型2
            countNum.setText(0 + "/" + MaxNum);
        }
        //设置长度
        etContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MaxNum)});
        //监听输入
        etContent.addTextChangedListener(mTextWatcher);
        return this;
    }

    /**
     * 设置横线颜色
     *
     * @param color --颜色值
     * @return
     */
    public CountEditText setLineColor(String color) {
        vLine.setBackgroundColor(Color.parseColor(color));
        return this;
    }

    /**
     * 设置类型
     *
     * @param type --类型
     * @return
     */
    public CountEditText setType(String type) {
        TYPES = type;
        return this;
    }

    /**
     * 设置最大字数
     *
     * @param num --字数
     * @return
     */
    public CountEditText setLength(int num) {
        Log.d(TAG, "setLength: num=" + num);
        this.MaxNum = num;
        return this;
    }

    /**
     * 设置文本框的Hint
     *
     * @param str --设置内容
     * @return
     */
    public CountEditText setEtHint(String str) {
        etContent.setHint(str);
        return this;
    }

    /**
     * 设置文本框的最小高度
     *
     * @param px --最小高度(单位px)
     * @return
     */
    public CountEditText setEtMinHeight(int px) {
        etContent.setMinHeight(px);
        return this;
    }

    /**
     * 获取输入内容
     *
     * @return 内容
     */
    public String getText() {
        return etContent.getText().toString();
    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        private int editStart;
        private int editEnd;

        public void afterTextChanged(Editable s) {
            editStart = etContent.getSelectionStart();
            editEnd = etContent.getSelectionEnd();
            // 先去掉监听器，否则会出现栈溢出
            etContent.removeTextChangedListener(mTextWatcher);
            // 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
            // 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
            while (calculateLength(s.toString()) > MaxNum) { // 当输入字符个数超过限制的大小时，进行截断操作
                s.delete(editStart - 1, editEnd);
                editStart--;
                editEnd--;
            }
            // 恢复监听器
            etContent.addTextChangedListener(mTextWatcher);
            setLeftCount();
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    };

    /**
     * 刷新剩余输入字数
     */
    private void setLeftCount() {
        if (TYPES.equals(SINGULAR)) {//类型1
            countNum.setText(String.valueOf((MaxNum - getInputCount())));
        } else if (TYPES.equals(PERCENTAGE)) {//类型2
            countNum.setText(MaxNum - (MaxNum - getInputCount()) + "/" + MaxNum);
            Log.d(TAG, "setLeftCount:刷新剩余输入字数 ==" + countNum.getText());
        }

    }

    /**
     * 获取用户输入内容字数
     */
    private long getInputCount() {
        Log.d(TAG, "getInputCount:== " + calculateLength(etContent.getText().toString()));
        return calculateLength(etContent.getText().toString());
    }

    /**
     * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点
     * 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
     *
     * @param cs
     * @return
     */
    public static long calculateLength(CharSequence cs) {
        double len = 0;
        for (int i = 0; i < cs.length(); i++) {
            int tmp = (int) cs.charAt(i);
            if (tmp > 0 && tmp < 127) {
                len += 1;
            } else {
                len++;
            }
        }
        return Math.round(len);
    }
}
