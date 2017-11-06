package com.sas.rh.reimbursehelper;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CountFragment extends Fragment implements View.OnClickListener {

    private Button title_left_btn , title_right_btn;



    /**
     * Fragment管理器
     */
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    /**
     * 两个Fragment
     */
    private PersonalStatisticFragment mLFragment ;
    private EnterpriseStatisticFragment mRFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_count, container, false);
        initView(view);
        return view;
    }


    private void initView(View v) {
        // TODO Auto-generated method stub
        title_left_btn = (Button)v.findViewById(R.id.constact_group);
        title_right_btn = (Button)v.findViewById(R.id.constact_all);

        title_left_btn.setOnClickListener(this);
        title_left_btn.performClick();//模拟点击事件，使左边按钮被点击

        mFragmentManager = getFragmentManager();
        mTransaction = mFragmentManager.beginTransaction();

        mLFragment = new PersonalStatisticFragment();
        mTransaction.replace(R.id.id_content, mLFragment);
        mTransaction.commit();


        title_right_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.constact_group:
                if(title_left_btn.isEnabled()){
                    title_left_btn.setEnabled(false);
                    title_right_btn.setEnabled(true);
                }
                mFragmentManager = getFragmentManager();
                mTransaction = mFragmentManager.beginTransaction();
                if(mLFragment == null){
                    mLFragment = new PersonalStatisticFragment();

                }
                mTransaction.replace(R.id.id_content, mLFragment);
                mTransaction.commit();
                break;

            case R.id.constact_all:
                if(title_right_btn.isEnabled()){
                    title_left_btn.setEnabled(true);
                    title_right_btn.setEnabled(false);
                }
                mFragmentManager = getFragmentManager();
                mTransaction = mFragmentManager.beginTransaction();
                if(mRFragment == null){
                    mRFragment = new EnterpriseStatisticFragment();
                }
                mTransaction.replace(R.id.id_content, mRFragment);
                mTransaction.commit();
                break;
        }
    }
}
