package com.sas.rh.reimbursehelper.Util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sas.rh.reimbursehelper.rt.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liqing on 18/3/22.
 * File to Base64
 */

public class FileToBase64Util {

    public static void getAsa() {
        String base64Code = "";
        try {
            //base64转码
            base64Code = encodeBase64File("/Users/dh86/Desktop/aaa.rar");
            //生成sas文件
            // GenerateSasFile("#######", "aaaa.pdf", base64Code, "/Users/dh86/Desktop/");
            //解析sas文件
            String[] results = AnalyzeSasFile("/Users/dh86/Desktop/aaaa.sas");
            System.out.println("签名内容: " + results[0] + "\n文件名称: " + results[1] + "\n文件内容: " + results[2]);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static String sasPath = "";

    public static String getSaspath() {
        if (TextUtils.isEmpty(sasPath)) {
            Log.e("--", "sasPath=empty");
        }
        return sasPath;

    }

    /**
     * 将签名后的base64字符封装保存为.sas文件
     *
     * @param key        文件P1签名生成的key
     * @param filename   包含后缀的完整文件名称,比如xxx.xxx
     * @param filebase64 文件的base64编码字串
     * @param destpath   生成文件的存放目录，比如xxx/xxxx/xxxx/
     * @return 返回0表示成功，返回1表示文件或字串长度过大(>100MB),返回2表示文件名过长(>255B)
     */
    public static int GenerateSasFile(String cer64, String key, String filename, String filebase64, String destpath) {
        try {
            // 单个base64字串大小应当不超过100M
            if (filebase64.length() > 104857600) {
                return 1;
            } else if (filename.length() > 255) {
                return 2;
            }
            String cer64_length = String.format("%010d", cer64.length());
            String key_length = String.format("%010d", key.length());
            String filename_length = String.format("%03d", filename.length());
            String filebase64_length = String.format("%010d", filebase64.length());
            String dpath = destpath.substring(destpath.length() - 2).trim().equals("/") ? destpath + "/" : destpath;
            String fname = filename.substring(0, filename.lastIndexOf("."));
            String sasfile_path = dpath + fname + ".sas";
            String filecontent = cer64_length + key_length + filename_length + filebase64_length + cer64 + key + filename + filebase64;
            System.out.println("=====filecontent:" + filecontent);
            String file_outstr_hex = str2HexStr(filecontent);
            Log.e("cer64=" + cer64_length + "key_length=" + key_length, "filename_length=" + filename_length + "filebase64_length=" + filebase64_length
                    + "key=" + key + "filename=" + filename + "filebase64=" + filebase64);
            Log.e("file16进制＝", "=" + file_outstr_hex);
            writeFile(file_outstr_hex, sasfile_path);
            File file = new File(sasfile_path);
            file.getPath();
            // sasfile_path=/storage/emulated/0/Download//storage/emulated/0/Download/下载.sas
            Log.e("FileToBase64Util", "sasfile_path=" + sasfile_path);
            // toSasFile(file_outstr_hex, sasfile_path);
        } catch (Exception e) {
            System.out.println("生成sas文件异常:");
            e.printStackTrace();
            return -1;
        }

        return 0;

    }

    /**
     * 将.sas文件按照结构解析
     *
     * @param SasFilepath 需要解析的sas完整路径，例如：xxx/xxxx/xxxx/xxx.sas
     * @return 返回null则表示解析过程发生异常，可将异常输出到后台日志进行查看，解析成功则将包含数据的数组返回
     * results[0]=key内容,
     * results[1]=文件名称,
     * results[2]=文件内容
     */
    public static String[] AnalyzeSasFile(String SasFilepath) {
        String[] results = new String[3];
        results[0] = "";
        results[1] = "";
        results[2] = "";
        String fileContent;
        String fileOriginalContent;
        try {
            File file = new File(SasFilepath);
            FileInputStream inputFile = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            inputFile.read(buffer);
            inputFile.close();
            fileContent = new String(buffer);
            fileOriginalContent = hexStr2Str(fileContent);
            int key_length = Integer.parseInt(fileOriginalContent.substring(0, 10));
            int filename_length = Integer.parseInt(fileOriginalContent.substring(10, 13));
            int filebase64_length = Integer.parseInt(fileOriginalContent.substring(13, 23));
            int nextElementIndex = 23;//长度区过后第一个元素下标
            String key = fileOriginalContent.substring(nextElementIndex, nextElementIndex + key_length);
            nextElementIndex = nextElementIndex + key_length;
            String filename = fileOriginalContent.substring(nextElementIndex, nextElementIndex + filename_length);
            nextElementIndex = nextElementIndex + filename_length;
            String filebase64 = fileOriginalContent.substring(nextElementIndex, nextElementIndex + filebase64_length);
            results[0] = key;
            results[1] = filename;
            results[2] = filebase64;
        } catch (Exception e) {
            System.out.println("解析sas文件异常:");
            e.printStackTrace();
            return null;
        }
        return results;

    }


    /**
     * 将文件转成base64 字符串
     *
     * @param path
     * @return
     * @throws Exception
     */

    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return new BASE64Encoder().encode(buffer);

    }


    public static void writeFile(String s, String path) {
        String sPath = "/storage/emulated/0/Download/bx2.sas";
        String file_outstr_hex = s;
        FileOutputStream fop = null;
        File file;
        String content = file_outstr_hex;

        try {

            file = new File(sPath);
            fop = new FileOutputStream(file);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                Boolean res = file.createNewFile();
                if (!res)
                    Log.e("创建失败！", "---");
            }

            // get the content in bytes
            byte[] contentInBytes = content.getBytes();

            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            sasPath = file.getPath();
//            sasPath = path;
            Log.e("sas", "success--");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将转换后字符保存为文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void toSasFile(String base64Code, String targetPath)
            throws Exception {

        byte[] buffer = base64Code.getBytes();
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }


    /**
     * 将转换后字符保存为文件
     *
     * @param base64Code
     * @param targetPath
     * @throws Exception
     */

    public static void toSaveFile(String base64Code, String targetPath)
            throws Exception {

        byte[] buffer = base64Code.getBytes();
        File file = null;
        try {
            file = new File(targetPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        FileOutputStream out = new FileOutputStream(targetPath);
        out.write(buffer);
        out.close();
    }

    public static File getFilePath(String filePath,
                                   String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }

    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 字符串转换成为16进制
     *
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            // sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 16进制直接转换成为字符串
     *
     * @param hexStr
     * @return
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }
}
