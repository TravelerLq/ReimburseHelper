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
import com.sas.rh.reimbursehelper.App;
import com.sas.rh.reimbursehelper.AppInitConfig.SharedPreferencesUtil;
import com.sas.rh.reimbursehelper.NetworkUtil.FormUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.ImageCaptureManager;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.newactivity.AddExpenseItemActivtity;
import com.sas.rh.reimbursehelper.newactivity.ApprovalProcessRecyActvity;
import com.sas.rh.reimbursehelper.newactivity.ExpenseProcessRecyActvity;
import com.sas.rh.reimbursehelper.newactivity.MyExpenseProcessActivity;
import com.sas.rh.reimbursehelper.newactivity.TestActivity;
import com.sas.rh.reimbursehelper.view.activity.MainActivity;
import com.sas.rh.reimbursehelper.view.activity.PersonalDetailActivity;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.InjectView;
import me.iwf.photopicker.PhotoPicker;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * Created by liqing on 18/4/23.
 * 主页Fragment
 */


public class HomeFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.tv_bar_title)
    TextView tvTitle;
    @InjectView(R.id.iv_back)
    ImageView ivBack;
    @InjectView(R.id.iv_add_expense)
    ImageView ivAddEpense;
    @InjectView(R.id.iv_home_pic)
    ImageView ivHomePic;
    @InjectView(R.id.rl_my_expense)
    RelativeLayout rlMyExpense;
    @InjectView(R.id.rl_my_approval)
    RelativeLayout rlMyApproval;
    @InjectView(R.id.header)
    ImageView header;

    //  private ImageView personaldetail_btn;
    public static final int REQUEST_CODE_TAKE_PIC = 234;
    private ArrayList<String> selectedPhotos;
    private SharedPreferencesUtil spu;
    private int formId;
    private JSONObject joFormId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {

                int status = joFormId.getIntValue("status");
                if (status == 200) {
                    formId = joFormId.getIntValue("formId");
                    spu.setFormId(formId);
                    Loger.e("add item formid--" + spu.getFormId());
                    Intent it = new Intent(getActivity(), AddExpenseItemActivtity.class);
                    // it.putExtra("type", "home");
                    startActivity(it);
//                    level = 1;
                } else if (status == 208) {
                    //  level = 0;
                    msgFormid = joFormId.getString("description");
                }
            }
        }
    };
    private int status;
    private boolean hasFormId = false;
    private String msgFormid;

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
        header.setOnClickListener(this);
    }

    @Override
    protected void initData() {

        spu = new SharedPreferencesUtil(HomeFragment.this.getActivity());
        tvTitle.setText("报销");
        ivBack.setVisibility(View.GONE);
        header.setVisibility(View.VISIBLE);
        //   getFormId();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add_expense:
                Loger.e("----ivAdd--click");

                getFormId();
//                Intent it = new Intent(getActivity(), AddExpenseItemActivtity.class);
//                // it.putExtra("type", "home");
//                startActivity(it);

                break;
            case R.id.iv_home_pic:
//                PhotoPicker.builder()
//                        .setPhotoCount(1)
//                        .setShowCamera(true)
//                        .setPreviewEnabled(false)
//                        .setSelected(selectedPhotos)
//                        .start(HomeFragment.this.getActivity());
                //拍照
                takePic(REQUEST_CODE_TAKE_PIC);

                //   toActivity(HomeFragment.this.getActivity(), TestActivity.class);
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
            case R.id.header:
                //报销
                Intent intent1 = new Intent(HomeFragment.this.getActivity(), PersonalDetailActivity.class);
                startActivity(intent1);
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
                    joFormId=jo;

                    handler.sendEmptyMessage(1);
//                    formId = FormUtil.returnFormId();
//                    status = jo.getIntValue("status");
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
                    openCamera(requestCode, getActivity().getApplicationContext());
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TAKE_PIC) {
            if (resultCode == RESULT_OK) {
                captureManager.galleryAddPic();
                takePic(REQUEST_CODE_TAKE_PIC);
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Loger.e("---RESULT_CANCELED");
            }


        }
    }
}
