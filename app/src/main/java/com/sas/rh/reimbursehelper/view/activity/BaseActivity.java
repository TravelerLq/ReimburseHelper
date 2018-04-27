package com.sas.rh.reimbursehelper.view.activity;

import android.content.Context;
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

/**
 * Created by liqing on 18/3/26.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected Context baseContext;
   protected SharedPreferencesUtil spu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(getLayoutId());

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

}
