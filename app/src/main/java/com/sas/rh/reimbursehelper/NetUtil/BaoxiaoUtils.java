package com.sas.rh.reimbursehelper.NetUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 用于报销表的相关请求操作
 *
 * @throws UnsupportedEncodingException
 */
public class BaoxiaoUtils {
	public static void main(String[] args) throws UnsupportedEncodingException {
		// 上传报销信息和相应图片
		// 图片本地地址，图片格式png或者jpg
//		 String filePath = "C:\\Users\\Administrator\\Desktop\\sas web图片集\\topmid1.png";
		String filePath1 = "/Users/tuzhengsong/Desktop/公司文件/test/电子发票.pdf";
//		insert(1,2,123.00,1,"aaa",1,"缘由",1,2,filePath,filePath1);
		insert(1,2,123.00,1,"aaa",1,"缘由",1,2,89,filePath1);
	}

	public static void insert(Integer xmId,Integer ygId,Double bxJine,Integer xfkmId,String bxBz,
							  Integer bmId,String bxReason,Integer qingdanId,Integer createPerson,Integer zpId,String...filePath) throws UnsupportedEncodingException {
		String url = "http://101.200.85.207:8080/sas/baoxiao/save";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		map.put("ygId", ygId);
		map.put("bxJine", bxJine);
		map.put("xfkmId", xfkmId);
		map.put("bxBz", "aaa");
		map.put("bmId", bmId);
		map.put("bxReason", bxReason);
		map.put("qingdanId", qingdanId);
		map.put("createPerson", createPerson);
		map.put("zpId", zpId);
		HttpClientUtils.shangchuan1(url, map, filePath);
	}

}