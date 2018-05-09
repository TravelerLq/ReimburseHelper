package com.sas.rh.reimbursehelper.newactivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sas.rh.reimbursehelper.NetworkUtil.StrTobaseUtil;
import com.sas.rh.reimbursehelper.R;
import com.sas.rh.reimbursehelper.Util.BitmapUtil;
import com.sas.rh.reimbursehelper.Util.FileToBase64Util;
import com.sas.rh.reimbursehelper.Util.Loger;
import com.sas.rh.reimbursehelper.view.activity.AddBaoxiaojizhuActivity;
import com.sas.rh.reimbursehelper.view.activity.AddExpenseActivity;
import com.sas.rh.reimbursehelper.view.activity.ApproveProcedureManageConfigActivity;
import com.sas.rh.reimbursehelper.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPicker;
import me.iwf.photopicker.PhotoPreview;

/**
 * Created by liqing on 18/4/27.
 */

public class TestActivity extends BaseActivity {
    private ImageView ivPicTest, ivTestViewPic;
    private ArrayList<String> selectedPhotos = new ArrayList<>();
    private String path;
    private List<String> originalBoxPicList = new ArrayList<>();
    private String picPath;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;

    }

    @Override
    protected void initData() {
        ivPicTest = (ImageView) findViewById(R.id.iv_take_pic);
        ivTestViewPic = (ImageView) findViewById(R.id.iv_pic_test_view);

    }

    @Override
    protected void initListeners() {
        ivPicTest.setOnClickListener(this);
        ivTestViewPic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_take_pic:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setPreviewEnabled(false)
                        .setSelected(selectedPhotos)
                        .start(TestActivity.this);
                break;
            case R.id.iv_pic_test_view:
                PhotoPreview.builder()
                        .setPhotos((ArrayList) originalBoxPicList)
                        .setShowDeleteButton(false)
                        .start(TestActivity.this, PhotoPreview.REQUEST_CODE);

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK &&
                (requestCode == PhotoPicker.REQUEST_CODE || requestCode == PhotoPreview.REQUEST_CODE)) {

            if (data != null) {
                selectedPhotos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                //System.out.println("********"+photos.get(0)+"********");
            }


            path = selectedPhotos.get(0);
           // originalBoxPicList.add(path);
            Loger.e("--path--" + path);
            try {
                String base64CodePic = FileToBase64Util.encodeBase64File(path);
                if (!TextUtils.isEmpty(base64CodePic)) {
                    Bitmap bitmap = StrTobaseUtil.base64ToBitmap(base64CodePic);
                    //bitmap to png

                    picPath = BitmapUtil.saveBitmapToSDCard(bitmap);
                    Loger.e("bitmap--save to Pic" + picPath);
                    originalBoxPicList.clear();
                    originalBoxPicList.add(0, picPath);



//                    方式一.  Drawable drawable=new BitmapDrawable(Bitmap);
//                    Glide.with(mContext).load(drawable).into(ivImage);
                    //2.
//                    byte[] decodedString = Base64.decode(person_object.getPhoto(),Base64.NO_WRAP);
//                    InputStream inputStream  = new ByteArrayInputStream(decodedString);
//                    Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
//                    user_image.setImageBitmap(bitmap);
                    // ivPicTest.setImageBitmap(bitmap);
                    ivTestViewPic.setImageBitmap(bitmap);
//                    Drawable drawable = new BitmapDrawable(bitmap);
//                    Glide.with(TestActivity.this).load(drawable).into(ivPicTest);


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
