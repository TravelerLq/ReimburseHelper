package com.sas.rh.reimbursehelper.Util;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.github.dfqin.grantor.PermissionListener;
import com.github.dfqin.grantor.PermissionsUtil;
import com.sas.rh.reimbursehelper.view.activity.AddExpenseActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.utils.ImageCaptureManager;

/**
 * Created by liqing on 18/4/24.
 */

public class TakePicsUtils {

    String savePath;
    Context context;
    public void takePicture(String savePath,Context context){
        this.savePath=savePath;
        this.context=context;
//        //设置参数,并拍照
//        setCameraParams(mCamera, mScreenWidth, mScreenHeight);
//        // 当调用camera.takePiture方法后，camera关闭了预览，这时需要调用startPreview()来重新开启预览
//        mCamera.takePicture(null, null, jpeg);
    }

    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            BufferedOutputStream bos = null;
            Bitmap bm = null;
            try {
                // 获得图片
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
                int pic_width = bm.getWidth();//1280
                int  pic_height= bm.getHeight();//720
                int height,width,x_center,y_center;
                height = (int) (pic_height * 0.8);//屏幕宽的0.8,拍照取景框的宽为屏幕的0.8
                width = (int) (height * 1.6);
                x_center=pic_width/2;
                y_center=pic_height/2;


                Matrix matrix = new Matrix();
                matrix.postRotate(360,pic_width/2,pic_height/2);
                bm = Bitmap.createBitmap(bm, x_center - (width / 2) ,
                        y_center - (height / 2) ,
                        (int) (pic_height*0.8*1.6),
                        (int) (pic_height*0.8),
                        matrix,false);

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    Log.i("---", "Environment.getExternalStorageDirectory()="+Environment.getExternalStorageDirectory());
                    //   String filePath = "/sdcard/dyk"+System.currentTimeMillis()+".jpg";//照片保存路径
                    File file = new File(savePath);
                    if (!file.exists()){
                        file.createNewFile();
                    }
                    bos = new BufferedOutputStream(new FileOutputStream(file));
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中

                }else{
                    Toast.makeText(context,"没有检测到内存卡", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bos.flush();//输出
                    bos.close();//关闭
                    bm.recycle();// 回收bitmap空间
                   // mCamera.stopPreview();// 关闭预览
                    Uri uri = Uri.fromFile(new File(savePath));
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(uri);
//                    ctx.sendBroadcast(intent);
//                    ctx.startActivity(intent.setClass(ctx,PreView.class));
                } catch (IOException e) {
                    e.printStackTrace();




                }
            }

        }
    };

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
