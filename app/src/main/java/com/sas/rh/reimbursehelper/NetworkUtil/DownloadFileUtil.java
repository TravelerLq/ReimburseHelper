package com.sas.rh.reimbursehelper.NetworkUtil;

import com.alibaba.fastjson.JSONObject;
import com.sas.rh.reimbursehelper.rt.BASE64Decoder;


import java.io.*;

public class DownloadFileUtil {
    private static String urlStr = AddressConfig.RootAddress;

    public static void main(String[] args) throws IOException {
        //图片对应的费用报销单id
        Integer expenseId = 2;

//        Integer formId = 42;

        Integer annexId = 16;

        //存放图片的文佳佳目录，注意结尾要有/
        String fileFolder = "/Users/tuzhengsong/Desktop/公司文件/client/";

//        String filePath = downPdfInvoice(annexId, fileFolder);
        //  String filePath = downPdfForm(annexId, fileFolder);
        //得到图片保存的路径
        // System.out.println(filePath);

    }

    public static JSONObject downPdfForm(Integer annexId, String fileFolder) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("annexId", annexId);
        String url = urlStr + "yuanshensystem/filedown/pdfform";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String fileJson = reJson.getString("file");
        String originalFilename = reJson.getString("originalFilename");
        String filePath = fileFolder + originalFilename;
        System.out.println(fileJson);
        base64StringToPdf(fileJson, filePath);
        return reJson;
    }

    public static String downPdfInvoice(Integer expenseId, String fileFolder) {
        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("expenseId", expenseId);
        jsonObject.put("expenseId", expenseId);
        String url = "http://localhost:8080/yuanshensystem/filedown/pdfinvoice";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
        String fileJson = reJson.getString("file");
        String originalFilename = reJson.getString("originalFilename");
        String filePath = fileFolder + originalFilename;
        System.out.println(fileJson);
//        toImage(fileJson, filePath);

        return filePath;
    }

    public static JSONObject downPdfByFormId(int formId, String fileFolder) throws IOException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("formId", formId);
        String url = urlStr+"yuanshensystem/filedown/getpdfbyformid";
        JSONObject reJson = JsonUtil.uploadJson(url, jsonObject);
//        String fileJson = reJson.getString("file");
//        String originalFilename = reJson.getString("originalFilename");
//        String filePath = fileFolder + originalFilename;
//        System.out.println(fileJson);
//        base64StringToPdf(fileJson, filePath);
        return reJson;
    }




    // base64位编码转成PDF
    public static File base64StringToPdf(String base64Content, String filePath) {
        BASE64Decoder decoder = new BASE64Decoder();
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        File file =null;

        try {
            // base64编码内容转换为字节数组
            byte[] bytes = decoder.decodeBuffer(base64Content);
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(
                    bytes);
            bis = new BufferedInputStream(byteInputStream);
            file = new File(filePath);
            File path = file.getParentFile();
            if (!path.exists()) {
                path.mkdirs();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[1024];
            int length = bis.read(buffer);
            while (length != -1) {
                bos.write(buffer, 0, length);
                length = bis.read(buffer);
            }
            bos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // closeStream(bis, fos, bos);
            try {
                if (bis != null) {
                    bis.close();
                }

                if (fos != null) {
                    fos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}

