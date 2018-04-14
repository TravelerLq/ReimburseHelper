package com.sas.rh.reimbursehelper.Util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

//import com.useeinfo.project.assistant.BuildConfig;

import java.io.File;

/**
 * Created by lyx on 2017/6/20 17:14.
 * Contact:     lvyongxu@gmail.com
 * Description:
 */

public class IntentUtils {

    /**
     * 跳转至应用详情页面
     *
     * @param context
     */

    private static Context mContext;
    private static final String APPLICATION_ID = "com.sas.rh.reimbursehelper";

    public static void gotoAppDetailSettingIntent(Context context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            Log.e("LoginActivity", ".SDK_INT >= 9 ");
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));

        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        context.startActivity(localIntent);
        //launchApp(context, localIntent);
    }

    /**
     * 安全的启动APP
     */
    public static boolean launchApp(Context ctx, Intent intent) {
        if (ctx == null)
            throw new NullPointerException("ctx is null");
        try {
            ctx.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            Log.e("login", "launchApp: " + e);
            return false;
        }
    }

    //
    Uri apkFileUri;

    // 在24及其以上版本，解决崩溃异常：
    // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
//
// File apkFile = new File(apkPath);
// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//        apkFileUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".provider", apkFile);
//    } else {
//        apkFileUri = Uri.fromFile(apkFile);
//    }
//        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        installIntent.setDataAndType(apkFileUri, "application/vnd.android.package-archive");
//
//
    //android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(File file, Context context) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            uri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileProvider", file);
//        } else {
//            uri = Uri.fromFile(file);
//        }
//        // Uri uri = Uri.fromFile(file)
//        // intent.setDataAndType(uri, "image/*");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.setDataAndType(uri, "application/vnd.android.package-archive");
//        return intent;

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        Uri fileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(context, APPLICATION_ID + ".provider", file);

        } else {
            fileUri = Uri.fromFile(file);
        }
        Log.e("intentUtils", "getImageIntent: fileUri= " + fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri, "image/*");
        return intent;


    }


    //android获取一个用于打开PDF文件的intent
    public static Intent getTxtFileIntent(File file, Context context) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(uri, "application/pdf");

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        Uri fileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(context, APPLICATION_ID + ".provider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri, "text/plain");
        return intent;
    }


    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(File file, Context context) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(uri, "application/pdf");

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        Uri fileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(context, APPLICATION_ID + ".provider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri, "application/pdf");
        return intent;
    }

    //android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(File file, Context context) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(uri, "application/msword");
//        return intent;

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        Uri fileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(context, APPLICATION_ID + ".provider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        Log.e("------", "getWord:fileUri=  " + fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri, "application/msword");
        return intent;
    }


    public static Intent getPdfIntent(File file)
    {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(File file, Context context) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(uri, "application/vnd.ms-excel");
//        return intent;
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri fileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(context, APPLICATION_ID + ".provider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri, "application/vnd.ms-excel");
        return intent;
    }

    //android获取一个用于打开PPT文件的intent
    public static Intent getPPTFileIntent(File file, Context context) {
//        Intent intent = new Intent("android.intent.action.VIEW");
//        intent.addCategory("android.intent.category.DEFAULT");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri uri = Uri.fromFile(file);
//        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
//        return intent;

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        Uri fileUri;
        // 在24及其以上版本，解决崩溃异常：
        // android.os.FileUriExposedException: file:///storage/emulated/0/xxx exposed beyond app through Intent.getData()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(context, APPLICATION_ID + ".provider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(fileUri, "application/vnd.ms-powerpoint");
        return intent;


    }

}
