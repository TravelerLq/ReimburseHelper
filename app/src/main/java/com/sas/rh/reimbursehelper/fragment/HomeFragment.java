package com.sas.rh.reimbursehelper.fragment;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.newactivity.AddExpenseItemActivtity;
import com.sas.rh.reimbursehelper.newactivity.ApprovalProcessRecyActvity;
import com.sas.rh.reimbursehelper.newactivity.ExpenseProcessRecyActvity;
import com.sas.rh.reimbursehelper.newactivity.MyExpenseProcessActivity;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.InjectView;
import me.iwf.photopicker.utils.ImageCaptureManager;


/**
 * Created by liqing on 18/4/23.
 * 主页Fragment
 */

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.tv_bar_title)
    TextView tvTitle;
    @InjectView(R.id.iv_add_expense)
    ImageView ivAddEpense;
    @InjectView(R.id.iv_home_pic)
    ImageView ivHomePic;
    @InjectView(R.id.rl_my_expense)
    RelativeLayout rlMyExpense;
    @InjectView(R.id.rl_my_approval)
    RelativeLayout rlMyApproval;
    public static final int REQUEST_CODE_TAKE_PIC = 234;
    private ArrayList<String> selectedPhotos;
    private SharedPreferencesUtil spu;
    private int formId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (status == 200) {
                    spu.setFormId(formId);
                    hasFormId = true;
                }
            }
        }
    };
    private int status;
    private boolean hasFormId = false;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initViews(View self, Bundle savedInstanceState) {

    }

    @Override
    protected void initListeners() {
        ivAddEpense.setOnClickListener(this);
        ivHomePic.setOnClickListener(this);
        rlMyExpense.setOnClickListener(this);
        rlMyApproval.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        spu = new SharedPreferencesUtil(HomeFragment.this.getActivity());
        tvTitle.setText("报销");
        //   getFormId();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_expense:
                Loger.e("----ivAdd--click");

                Intent it = new Intent(getActivity(), AddExpenseItemActivtity.class);
                it.putExtra("type", "home");
                startActivity(it);


                break;
            case R.id.iv_home_pic:
//                PhotoPicker.builder()
//                        .setPhotoCount(1)
//                        .setShowCamera(true)
//                        .setPreviewEnabled(false)
//                        .setSelected(selectedPhotos)
//                        .start(HomeFragment.this.getActivity());
                takePic(REQUEST_CODE_TAKE_PIC);
                break;
            case R.id.rl_my_approval:
                //审批
                Intent intentApproval = new Intent(HomeFragment.this.getActivity(), ApprovalProcessRecyActvity.class);
                startActivity(intentApproval);
                break;

            case R.id.rl_my_expense:
                //报销
                Intent intent = new Intent(HomeFragment.this.getActivity(), MyExpenseProcessActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(HomeFragment.this.getActivity(), ExpenseItemListActivity.class);
//                startActivity(intent);
                break;
            default:
                break;
        }

    }

    private void getFormId() {
        new Thread(GetFormIdRunable).start();
    }


    Runnable GetFormIdRunable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub

            try {
                JSONObject jo = FormUtil.getFormId(spu.getUidNum());
                if (jo != null) {
                    handler.sendEmptyMessage(1);
                    formId = FormUtil.returnFormId();
                    status = jo.getIntValue("status");
                } else {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                handler.sendEmptyMessage(-1);
                e.printStackTrace();
            }
        }

    };


    private void takePic(final int requestCode) {

        if (PermissionsUtil.hasPermission(HomeFragment.this.getActivity(), Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            openCamera(requestCode, HomeFragment.this.getActivity());
        } else {
            PermissionsUtil.requestPermission(HomeFragment.this.getActivity(), new PermissionListener() {
                @Override
                public void permissionGranted(@NonNull String[] permission) {
                    Log.e("--", "permissionGranted: 用户授予了访问外部存储的权限");
                    openCamera(requestCode, HomeFragment.this.getActivity());
                }

                @Override
                public void permissionDenied(@NonNull String[] permission) {
                    Log.e("--", "permissionDenied: 用户拒绝了访问外部存储的申请");
                    // needPermissionTips();

                }
            }, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    private ImageCaptureManager captureManager;

    private void openCamera(int requestCode, Context context) {
        captureManager = new ImageCaptureManager(context);
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            startActivityForResult(intent, requestCode);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ActivityNotFoundException e) {
            // TODO No Activity Found to handle Intent
            e.printStackTrace();
        }
    }
}
