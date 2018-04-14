package com.sas.rh.reimbursehelper.Util;

import com.sas.rh.reimbursehelper.rt.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;


public class test {
	public static void main(String[] args) {
		String base64Code = "";
		try {
			//base64转码
			base64Code = encodeBase64File("/Users/tuzhengsong/WorkSpace/PdfTest/src/TB.pdf");
			//生成sas文件
			GenerateSasFile("aaaaabbbbb","#######","aaaa.pdf","aaaa","/Users/tuzhengsong/WorkSpace/PdfTest/src/");
			//解析sas文件
//			String[] results = AnalyzeSasFile("/Users/tuzhengsong/WorkSpace/PdfTest/src/aaaa.sas");
//			System.out.println("证书base64: "+results[0]+"\n签名内容: "+results[1]+"\n文件名称: "+results[2]+"\n文件内容: "+results[3]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}

	 
	 /**  
	  * 将签名后的base64字符封装保存为.sas文件  
	  * @param key  文件P1签名生成的key
	  * @param filename  包含后缀的完整文件名称,比如xxx.xxx
	  * @param filebase64  文件的base64编码字串
	  * @param destpath  生成文件的存放目录，比如xxx/xxxx/xxxx/
	  * @return 返回0表示成功，返回1表示文件或字串长度过大(>100MB),返回2表示文件名过长(>255B)
	  */  
	 public static int GenerateSasFile(String cer64,String key,String filename,String filebase64,String destpath) {
		 try {
			 // 单个base64字串大小应当不超过100M
			 if(filebase64.length()>104857600 ) {
				 return 1;
			 }else if(filename.length()>255){
				 return 2;
			 }
			 String cer64_length = String.format("%010d", cer64.length());  
			 String key_length = String.format("%010d", key.length());  
			 String filename_length = String.format("%03d", filename.length());
			 String filebase64_length = String.format("%010d", filebase64.length());
			 String dpath = destpath.substring(destpath.length()-2).trim().equals("/")?destpath+"/":destpath;
			 String fname =filename.substring(0,filename.lastIndexOf("."));
			 String sasfile_path =dpath+fname+".sas";
			 String filecontent = cer64_length+key_length+filename_length+filebase64_length+cer64+key+filename+filebase64;
			 System.out.println("=====filecontent:"+filecontent);
			 String file_outstr_hex = str2HexStr(filecontent);
			 File file = new File(sasfile_path);
			 toSasFile(file_outstr_hex,sasfile_path);
		 }catch(Exception e) {
			 System.out.println("生成sas文件异常:");
			 e.printStackTrace();
			 return -1;
		 }
		 
		 return 0;
		 
	 }
	 
	 /**  
	  * 将.sas文件按照结构解析
	  * @param SasFilepath  需要解析的sas完整路径，例如：xxx/xxxx/xxxx/xxx.sas
	  * @return 返回null则表示解析过程发生异常，可将异常输出到后台日志进行查看，解析成功则将包含数据的数组返回
	  * results[0]=key内容,
	  * results[1]=文件名称,
	  * results[2]=文件内容
	  */  
	 public static String[] AnalyzeSasFile(String SasFilepath) {
		 String[] results = new String[4];
		 results[0] = "";
		 results[1] = "";
		 results[2] = "";
		 results[3] = "";
		 String fileContent;
		 String fileOriginalContent;
		 try {
			 File file = new File(SasFilepath);  
			 FileInputStream inputFile = new FileInputStream(file);  
			 byte[] buffer = new byte[(int) file.length()];  
			 inputFile.read(buffer);  
			 inputFile.close();  
			 fileContent= new String(buffer);
			 fileOriginalContent = hexStr2Str(fileContent);
			 int cer64_length = Integer.parseInt(fileOriginalContent.substring(0,10));
			 int key_length = Integer.parseInt(fileOriginalContent.substring(10,20));
			 int filename_length =  Integer.parseInt(fileOriginalContent.substring(20,23));
			 int filebase64_length =  Integer.parseInt(fileOriginalContent.substring(23,33));
			 //长度区过后第一个元素下标
			 int nextElementIndex = 33;
			 String cer64 = fileOriginalContent.substring(nextElementIndex, nextElementIndex+cer64_length);
			 nextElementIndex = nextElementIndex+cer64_length; 
			 String key = fileOriginalContent.substring(nextElementIndex, nextElementIndex+key_length);
			 nextElementIndex = nextElementIndex+key_length;
			 String filename = fileOriginalContent.substring(nextElementIndex, nextElementIndex+filename_length);
			 nextElementIndex = nextElementIndex+filename_length;
			 String filebase64 = fileOriginalContent.substring(nextElementIndex, nextElementIndex+filebase64_length);
			 results[0] = cer64;
			 results[1] = key;
			 results[2] = filename;
			 results[3] = filebase64;
			 
		 }catch(Exception e) {
			 System.out.println("解析sas文件异常:");
			 e.printStackTrace();
			 return null;
		 }
		 return results;
		 
	 }
	 	
		
		/**  
		  * 将文件转成base64 字符串  
		  * @param path文件路径  
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
	  
	 /**  
	  * 将转换后字符保存为文件  
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
	  * 字符串转换成为16进制
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
