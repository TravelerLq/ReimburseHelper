package com.sas.rh.reimbursehelper.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by liqing on 18/3/26.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected Context baseContext;
   protected SharedPreferencesUtil spu;
    // 管理运行的所有的activity
    protected final static List<BaseActivity> mActivitys = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());
        synchronized (mActivitys) {
            mActivitys.add(this);
        }

        initToolbars();
        this.initData();
        this.initListeners();
        baseContext = BaseActivity.this;
        spu = new SharedPreferencesUtil(baseContext);
    }

    private void initToolbars() {
        // View view =findViewById(R.layout.include_bar_title)
    }

    //
    protected abstract int getLayoutId();

    protected abstract void initData();

    protected abstract void initListeners();

    protected <T> void toActivity(Context context, Class<T> tClass) {
        Intent intent = new Intent(context, tClass);
        startActivity(intent);
    }

    protected <T> void toActivityWithType(Context context, Class<T> tClass, String data) {
        Intent intent = new Intent(context, tClass);
        intent.putExtra("type", data);
        startActivity(intent);
    }

    protected <T> void toActivityWithData(Context context, Class<T> tClass, String key, String data) {
        Intent intent = new Intent(context, tClass);
        intent.putExtra(key, data);
        startActivity(intent);
    }

    public void addFragmentNotToStack(int layoutId, Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutId,fragment);
        fragmentTransaction.commit();
    }

    public void addFragmentByTagNotToStack(int layoutId, Fragment fragment,String tag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layoutId,fragment,tag);
        fragmentTransaction.commit();
    }

    /**
     * 提示对话框
     * @param message
     */
    protected void warningDialog(String message){
        new AlertDialog.Builder(BaseActivity.this)
                .setTitle(getResources().getString(R.string.notice))
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //清空之前扫描的料单数据
                       //  VsdApplication.getInstance().getWaitStoreMaterialBeanList().clear();
                        finish();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .create().show();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        synchronized (mActivitys) {
            mActivitys.remove(this);
        }
    }

    /**
     * 关闭所有页面
     */
    protected void exit() {
        for (int i = 0; i < mActivitys.size(); i++) {
            mActivitys.get(i).finish();
        }
    }
    @Override
    public void finish() {
        super.finish();
    }

}
