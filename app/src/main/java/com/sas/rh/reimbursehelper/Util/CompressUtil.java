package com.sas.rh.reimbursehelper.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liqing on 18/4/20.
 */

public class CompressUtil {
    public static DisplayMetrics dm;

//
//    public static void compress( ) {
//        dm = new DisplayMetrics();
//      getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        float hh = dm.heightPixels;
//        float ww = dm.widthPixels;
//        BitmapFactory.Options opts = new BitmapFactory.Options();
//        opts.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
//        opts.inJustDecodeBounds = false;
//        int w = opts.outWidth;
//        int h = opts.outHeight;
//        int size = 0;
//        if (w <= ww && h <= hh) {
//            size = 1;
//        } else {
//            double scale = w >= h ? w / ww : h / hh;
//            double log = Math.log(scale) / Math.log(2);
//            double logCeil = Math.ceil(log);
//            size = (int) Math.pow(2, logCeil);
//        }
//        opts.inSampleSize = size;
//        bitmap = BitmapFactory.decodeFile(srcPath, opts);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        int quality = 100;
//        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//        System.out.println(baos.toByteArray().length);
//        while (baos.toByteArray().length > 45 * 1024) {
//            baos.reset();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//            quality -= 20;
//            System.out.println(baos.toByteArray().length);
//        }
//        try {
//            baos.writeTo(new FileOutputStream("/mnt/sdcard/Servyou/photo/buffer/22.jpg"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                baos.flush();
//                baos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
