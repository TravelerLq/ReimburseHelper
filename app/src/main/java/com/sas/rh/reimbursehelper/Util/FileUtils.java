package com.sas.rh.reimbursehelper.Util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by lyx on 2017/6/20 15:48.
 * Contact:     lvyongxu@gmail.com
 * Description:
 */

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * 保存的目录名
     */
    private final static String FOLDER_FILE = "/files";

    /**
     * 视频文件夹名称
     */
    private final static String FOLDER_VIDEO = "video";

    /**
     * 视频文件夹名称
     */
    private final static String FOLDER_APK = "Download";

    /**
     * 获取Android/data/com.useeinfo.project/assistant/cache 文件夹路径
     * String folderPath = context.getApplicationContext().getExternalFilesDir(null).getAbsolutePath() + "/Screenshots";
     *
     * @param context
     * @return
     */
    public static String getAppCacheDirPath(Context context) {

        return context.getExternalCacheDir().getPath();
    }

    public static File getAppCacheDir(Context context) {
        return context.getExternalCacheDir();
    }

    public static boolean isExternalAvailable() {
        return Environment.isExternalStorageEmulated();

    }

    /**
     * 获取Android/data/com.useeinfo.project/assistant/file 文件夹路径
     *
     * @param context
     * @return
     */
    public static String getExternalFilesDirPath(Context context) {
        Log.e("getExternalFilesDirPath","="+context.getExternalFilesDir(null).getAbsolutePath());
        return context.getExternalFilesDir(null).getAbsolutePath();
    }

    /**
     * 获取存储项目信息页面文件的文件夹对象
     * Android/data/com.useeinfo.project/assistant/files
     *
     * @param context
     * @return
     */
    public static File getAppFilesDir(Context context) {
        if (isExternalAvailable()) {
            return context.getExternalFilesDir(null);
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * 获取存储项目信息页面文件的文件夹绝对路径
     * Android/data/com.useeinfo.project/assistant/files
     *
     * @param context
     * @return
     */
    public static String getAppFilesDirPath(Context context) {
        return getAppFilesDir(context).getAbsolutePath();
    }

    /**
     * 获取存储项目实况页面的视频文件的文件夹对象
     * Android/data/com.useeinfo.project/assistant/files/video
     *
     * @param context
     * @return
     */
    public static File getAppFilesVideoDir(Context context) {
        if (isExternalAvailable()) {

            File videoFolder = new File(context.getExternalFilesDir(null), FOLDER_VIDEO);
//            Log.e(TAG, "getAppFilesVideoDir: videoFolder===" + videoFolder.getAbsolutePath());
//            Log.e(TAG, "getAppFilesVideoDir: videoFolder.exists()===" + videoFolder.exists());
            if (!videoFolder.exists()) {
                videoFolder.mkdirs();
            }

            return videoFolder;
        } else {

            File videoFolder = new File(context.getFilesDir(), FOLDER_VIDEO);
//            Log.e(TAG, "getAppFilesVideoDir: videoFolder~~~" + videoFolder.getAbsolutePath());
            if (!videoFolder.exists()) {
                videoFolder.mkdirs();
            }

            return videoFolder;
        }
    }


    /**
     * 获取存储项目实况页面的视频文件的文件夹对象
     * Android/data/com.useeinfo.project/assistant/files/video
     *
     * @param context
     * @return
     */
    public static File getAppFilesApkDir(Context context) {
        if (isExternalAvailable()) {

            File videoFolder = new File(Environment.getExternalStorageDirectory(), FOLDER_APK);
//            Log.e(TAG, "getAppFilesVideoDir: videoFolder===" + videoFolder.getAbsolutePath());
//            Log.e(TAG, "getAppFilesVideoDir: videoFolder.exists()===" + videoFolder.exists());
            if (!videoFolder.exists()) {
                videoFolder.mkdirs();
            }

            return videoFolder;
        } else {

            File videoFolder = new File(context.getFilesDir(), FOLDER_APK);
//            Log.e(TAG, "getAppFilesVideoDir: videoFolder~~~" + videoFolder.getAbsolutePath());
            if (!videoFolder.exists()) {
                videoFolder.mkdirs();
            }

            return videoFolder;
        }
    }


    public static String getAppFilesApkDirPath(Context context) {
        return getAppFilesApkDir(context).getAbsolutePath();
    }


    public static String getAppFilesVideoDirPath(Context context) {
        return getAppFilesVideoDir(context).getAbsolutePath();
    }

//    public static String getFileName(String filePath) {
//        int lastDivideLine = filePath.lastIndexOf("/");
//        String fileName = filePath.substring(lastDivideLine);
//        return fileName;
//    }
//
//    public static File getFile(Context context, String fileName) {
//        return new File(getAppFilesDir(context), fileName);
//    }

}
