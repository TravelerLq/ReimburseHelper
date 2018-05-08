package com.sas.rh.reimbursehelper.newactivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import com.liqing.myapplication.sortlist.CharacterParser;
//import com.liqing.myapplication.sortlist.SideBar;
//import com.liqing.myapplication.sortlist.SortModel;

import com.alibaba.fastjson.JSONArray;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.NetworkUtil.UserUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Sortlist.CharacterParser;
import com.sas.rh.reimbursehelper.Sortlist.ClearEditText;
import com.sas.rh.reimbursehelper.Sortlist.PinyinComparator;
import com.sas.rh.reimbursehelper.Sortlist.SideBar;
import com.sas.rh.reimbursehelper.Sortlist.SortAdapter;
import com.sas.rh.reimbursehelper.Sortlist.SortModel;
import com.sas.rh.reimbursehelper.Util.ConstactUtil;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.Util.ProgressDialogUtil;
import com.sas.rh.reimbursehelper.Util.ToastUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.sas.rh.reimbursehelper.view.activity.DepartmentsManageAddItemActivity.DEPART;
//
//import com.finddreams.sortedcontact.sortlist.CharacterParser;
//import com.finddreams.sortedcontact.sortlist.SideBar;
//import com.finddreams.sortedcontact.sortlist.SortModel;
//import com.finddreams.sortedcontact.sortlist.SideBar.OnTouchingLetterChangedListener;


/**
 * @author http://blog.csdn.net/finddreams
 * @Description:联系人显示界面
 */
public class PersonSortActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private View mBaseView;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog;
    private SortAdapter adapter;
    private ClearEditText mClearEditText;
    private Map<String, String> callRecords;

    private CharacterParser characterParser;
    private List<SortModel> SourceDateList;

    private PinyinComparator pinyinComparator;

    private TextView tvTilte;
    private ImageView ivBack;
    private TextView tvNobody;
    private SharedPreferencesUtil spu;
    private Context context;
    private ProgressDialogUtil pdu = new ProgressDialogUtil(PersonSortActivity.this, "提示", "正在加载...");
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (pdu.getMypDialog().isShowing()) {
                pdu.dismisspd();
            }
            if (msg.what == 1) {
                List<SortModel> list = JSONArray.parseArray(jsonArray.toJSONString(), SortModel.class);
                SortModel sortModel;
                for (int i = 0; i < list.size(); i++) {
                    sortModel = list.get(i);
                    if (sortModel.getName() == null) {
                        list.remove(i);
                    }
                }
                SourceDateList.clear();
                SourceDateList.addAll(list);
                for (int i = 0; i < SourceDateList.size(); i++) {
                    Loger.e("source---" + SourceDateList.get(i).getName());
                    Loger.e("source---" + SourceDateList.get(i).getUserId());
                }


//                if (SourceDateList.size() == 0) {
//                    initTestData();
//                }

//                setLetter(SourceDateList);
                if (adapter == null) {
                    new SortAdapter(context, SourceDateList);
                }
                adapter.notifyDataSetChanged();
                setLetter(SourceDateList);
                sortData();
            }
        }
    };
    private Boolean isIntent;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contact);
        SourceDateList = new ArrayList<>();
        adapter = new SortAdapter(PersonSortActivity.this, SourceDateList);
        context = PersonSortActivity.this;
        spu = new SharedPreferencesUtil(context);

        tvTilte = (TextView) findViewById(R.id.tv_bar_title);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvNobody = (TextView) findViewById(R.id.tv_nobody);
        tvTilte.setText("人员");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent() != null) {
            Loger.e("person--getIntent");
            isIntent = true;
            type = getIntent().getStringExtra("type");
            Loger.e("type---" + type);


        } else {
            isIntent = false;
        }
        initView();
        initData();
        checkPermission();

        getData();


    }

    private void getData() {
        pdu.showpd();
        new Thread(GetAllMembersThread).start();
    }

    private JSONArray jsonArray;
    Runnable GetAllMembersThread = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONArray jo = new UserUtil().getALlUser(spu.getUidNum());
                if (jo != null) {
                    jsonArray = jo;
                    Loger.e("--response--" + jsonArray.toJSONString());
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

    private void checkPermission() {

        if (ContextCompat.checkSelfPermission(PersonSortActivity.this,
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(PersonSortActivity.this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(PersonSortActivity.this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    private void initView() {
        sideBar = (SideBar) this.findViewById(R.id.sidrbar);
        dialog = (TextView) this.findViewById(R.id.dialog);
        sortListView = (ListView) this.findViewById(R.id.sortlist);

    }

    private void initData() {
        // 实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar.setTextView(dialog);

        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }
            }
        });

        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 这里要利用adapter.getItem(position)来获取当前position所对应的对象
                // Toast.makeText(getApplication(),
                // ((SortModel)adapter.getItem(position)).getName(),
//                // Toast.LENGTH_SHORT).show();
//                String number = callRecords.get(((SortModel) adapter
//                        .getItem(position)).getName());
                if (type != null) {
                    // if()
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", SourceDateList.get(position));
                    Loger.e("pos--" + position + "clickId" + SourceDateList.get(position).getUserId());
//                intent.putExtra("userId", mData.get(pos).getKey());
//                intent.putExtra("thirdValue", mData.get(pos).getValue());
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    Loger.e("pos--" + position + "clickId" + SourceDateList.get(position).getUserId());
                    ToastUtil.showToast(PersonSortActivity.this, "点击了第" + position + "个",
                            Toast.LENGTH_SHORT);
                }


            }
        });

        //  initTestData();


        //new ConstactAsyncTask().execute(0);

    }

    SortModel sortModel;

    private void initTestData() {


        sortModel = new SortModel();
        sortModel.setUserId(1);
        sortModel.setName("巍然");
        SourceDateList.add(0, sortModel);

        sortModel = new SortModel();
        sortModel.setUserId(2);
        sortModel.setName("陈一");
        SourceDateList.add(1, sortModel);
        sortModel = new SortModel();
        sortModel.setUserId(3);
        sortModel.setName("胡一");
        SourceDateList.add(2, sortModel);

        sortModel = new SortModel();
        sortModel.setUserId(4);
        sortModel.setName("丰子恺");
        SourceDateList.add(3, sortModel);

        sortModel = new SortModel();
        sortModel.setUserId(3);
        sortModel.setName("丁伟");
        SourceDateList.add(4, sortModel);
        setLetter(SourceDateList);
        sortData();


    }

    private class ConstactAsyncTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... arg0) {
            int result = -1;
            callRecords = ConstactUtil.getAllCallRecords(PersonSortActivity.this);

            result = 1;
            return result;
        }


        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 1) {
                List<String> constact = new ArrayList<String>();
                for (Iterator<String> keys = callRecords.keySet().iterator(); keys
                        .hasNext(); ) {
                    String key = keys.next();
                    constact.add(key);
                }
                String[] names;
                names = new String[]{"李四", "张三", "历史"};
                //  names = constact.toArray(names);

                SourceDateList = filledData(names);


                // 根据a-z进行排序源数据
                sortData();

            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

    }

    private void sortData() {

        Collections.sort(SourceDateList, pinyinComparator);
        if (adapter == null) {
            adapter = new SortAdapter(PersonSortActivity.this, SourceDateList);
        }
        sortListView.setAdapter(adapter);

        mClearEditText = (ClearEditText) PersonSortActivity.this
                .findViewById(R.id.filter_edit);
        mClearEditText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                mClearEditText.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

            }
        });
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 为ListView填充数据
     *
     * @param date
     * @return
     */
    private List<SortModel> filledData(String[] date) {
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < date.length; i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(date[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }


    /**
     * 为数据自动设置首字拼音
     *
     * @param date
     * @return
     */

    /**
     * 为ListView填充数据
     *
     * @param mSortList
     * @return
     */
    private void setLetter(List<SortModel> mSortList) {
        // List<SortModel> mSortList = new ArrayList<SortModel>();

        for (int i = 0; i < mSortList.size(); i++) {
            SortModel sortModel = mSortList.get(i);
            //  sortModel.setName(date[i]);
            // 汉字转换成拼音
            String pinyin = characterParser.getSelling(mSortList.get(i).getName());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
            } else {
                sortModel.setSortLetters("#");
            }


        }


    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = SourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : SourceDateList) {
                String name = sortModel.getName();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);

        if (filterDateList.size() == 0) {
            tvNobody.setVisibility(View.VISIBLE);
        } else {
            tvNobody.setVisibility(View.GONE);
        }
    }

}
