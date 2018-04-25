package com.sas.rh.reimbursehelper.Util;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.sas.rh.reimbursehelper.view.activity.AddExpenseActivity;

import java.io.IOException;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.utils.ImageCaptureManager;

/**
 * Created by liqing on 18/4/24.
 */

public class TakePicsUtils {

//     PhotoPicker.builder()
//             .setPhotoCount(1)
//                        .setShowCamera(true)
//                        .setPreviewEnabled(false)
//                        .setSelected(selectedPhotos)
//                        .start(AddExpenseActivity.this);


//            Glide.with(this)
//                    .load(uri)
//                    .thumbnail(0.1f)
//                    .into(ivPhoto);


//    private void takePic(final int requestCode, Context mContext) {
//
//        if (PermissionsUtil.hasPermission(mContext, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE)) {
//
//            openCamera(requestCode);
//        } else {
//            PermissionsUtil.requestPermission(mContext, new PermissionListener() {
//                @Override
//                public void permissionGranted(@NonNull String[] permission) {
//                    Log.e("--", "permissionGranted: 用户授予了访问外部存储的权限");
//                    openCamera(requestCode);
//                }
//
//                @Override
//                public void permissionDenied(@NonNull String[] permission) {
//                    Log.e("--", "permissionDenied: 用户拒绝了访问外部存储的申请");
//                    // needPermissionTips();
//
//                }
//            }, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE});
//        }
//    }

//    private ImageCaptureManager captureManager;
//
//    private void openCamera(int requestCode, Context context) {
//        captureManager = new ImageCaptureManager(context);
//        try {
//            Intent intent = captureManager.dispatchTakePictureIntent();
//            startActivityForResult(intent, requestCode);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ActivityNotFoundException e) {
//            // TODO No Activity Found to handle Intent
//            e.printStackTrace();
//        }
//    }
}
