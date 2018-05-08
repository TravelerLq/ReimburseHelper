package com.sas.rh.reimbursehelper.view.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.sas.rh.reimbursehelper.App;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.fragment.CountFragment;
import com.sas.rh.reimbursehelper.fragment.EnterpriseFragment;
import com.sas.rh.reimbursehelper.HomepageFragment;
import com.sas.rh.reimbursehelper.fragment.HomeFragment;
import com.sas.rh.reimbursehelper.fragment.MessageFragment;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.fragment.SelectViewExpenseFragment;
import com.sas.rh.reimbursehelper.newactivity.*;
import com.sas.rh.reimbursehelper.service.NoticeMsgService;

import java.util.ArrayList;
import java.util.List;

import cn.com.syan.spark.client.sdk.SparkApplication;
import cn.unitid.spark.cm.sdk.business.CBSCertificateStore;
import cn.unitid.spark.cm.sdk.data.entity.Certificate;
import io.github.leibnik.wechatradiobar.WeChatRadioButton;
import io.github.leibnik.wechatradiobar.WeChatRadioGroup;


public class MainActivity extends AppCompatActivity {

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;
    private ViewPager viewPager;
    private WeChatRadioGroup gradualRadioGroup;
    private ArrayList<Certificate> certificateArrayList;//证书列表
    private Intent serviceIntenta;
    private LinearLayout ll_log_out;
    private SharedPreferencesUtil spu;
    private String type;
    private DemoPagerAdapter demoPagerAdapter;
    private WeChatRadioButton radioButton;
    private WeChatRadioButton rbMsg, rbCompany;
    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        spu = new SharedPreferencesUtil(MainActivity.this);
        setContentView(R.layout.activity_main);
        rbMsg = (WeChatRadioButton) findViewById(R.id.rb_msg);
        rbCompany = (WeChatRadioButton) findViewById(R.id.rb_company);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        gradualRadioGroup = (WeChatRadioGroup) findViewById(R.id.radiogroup);

        //  initService();
        List<Fragment> list = new ArrayList<Fragment>();
        // list.add(new HomepageFragment());
        list.add(new HomeFragment());
        // list.add(new SelectViewExpenseFragment());
        list.add(new MessageFragment());

        role = getIntent().getStringExtra("data");
        role = spu.getRole();
        Loger.e("---spu.getrole==" + role);
        //2 公司领导层 3.
        if (role != null) {
            Loger.e("role---" + role);
            if (role.equals("2")) {
                rbCompany.setVisibility(View.VISIBLE);
                list.add(new EnterpriseFragment());

            } else {
                rbCompany.setVisibility(View.GONE);
            }

        }


        // list.add(new CountFragment());

        demoPagerAdapter = new DemoPagerAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(demoPagerAdapter);
        gradualRadioGroup.setViewPager(viewPager);
        //初始Application

        if (getIntent() != null) {

            type = getIntent().getStringExtra("type");
            if (type != null && type.equals("service")) {
                Loger.e("type---" + type);
                viewPager.setCurrentItem(1);
                setClickedViewChecked(1, gradualRadioGroup);

            } else {

            }

        }
        SparkApplication.init(getApplication());
        CBSCertificateStore store = CBSCertificateStore.getInstance();
        //查询本地库证书列表,没注册则注册一张
        certificateArrayList = store.getAllCertificateList();
        if (certificateArrayList.size() == 0) {
            //  Intent it = new Intent(MainActivity.this, RegFirstStepActivity.class);   Intent it
            Intent it = null;

            if (spu.getUidNum() != -1) {
                it = new Intent(MainActivity.this, com.sas.rh.reimbursehelper.newactivity.RegCertActivity.class);
            } else {
                it = new Intent(MainActivity.this, LoginOrRegisterActivity.class);
            }

            startActivity(it);
            finish();
        } else {
            //有证书
            //  store.deleteCertificate(certificateArrayList.get(0).getId());
            Loger.e("certificateArrayList.get(0).getId()=" + certificateArrayList.get(0).getId());
        }

    }

    private void setClickedViewChecked(int position, WeChatRadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); ++i) {
            ((WeChatRadioButton) radioGroup.getChildAt(i)).setRadioButtonChecked(false);
        }

        ((WeChatRadioButton) radioGroup.getChildAt(position)).setRadioButtonChecked(true);
    }

    private void initService() {
        serviceIntenta = new Intent(MainActivity.this, NoticeMsgService.class);
        startService(serviceIntenta);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        finish();
//    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
//
//    public void refreshpage(){
//        viewPager.setCurrentItem(0);
//        gradualRadioGroup.onPageSelected(0);
//    }

    class DemoPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> mData;

        public DemoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public DemoPagerAdapter(FragmentManager fm, List<Fragment> data) {
            super(fm);
            mData = data;
        }

        @Override
        public Fragment getItem(int position) {
            return mData.get(position);
        }

        @Override
        public int getCount() {
            return mData.size();
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                //System.exit(0);
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  stopService(serviceIntenta);
    }
}
