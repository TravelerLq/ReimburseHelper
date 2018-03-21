package com.sas.rh.reimbursehelper.NetUtil;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;


/**
 * 用于消费科目表的请求操作
 * 
 * @author llx
 *
 */
public class XfkmUtils {
//	public static void main(String[] args) throws UnsupportedEncodingException {
		// 增加公司自定义科目项
//		insert(1,2,"测试");
		//删除公司自定义科目项
//		delete(1,1,8);
		// 设置科目状态
//		（1）设置关闭
//		removeXfkmId(1,1,3);
		//（2）设置打开
//		addXfkmId(1,1,3);
		// a.根据公司获取公司选定的科目信息
//		getXfkmId(1,2);
		// b.根据公司获取公司数据库中所有科目信息
//		getAllXfkmId(1);
		// 修改自定义科目信息
//		xiugaiXfkmId(1,3,1,"xx");
		
//	}

	public static JSONObject xiugaiXfkmId(Integer gongsiId,Integer xfkmId,
			Integer ygId,String xfkmName) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xfkm/xiugaiXfkmId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		map.put("xfkmId", xfkmId);//消费科目id(只能改自定义消费科目)
		map.put("ygId", ygId);//提交者id	
		map.put("xfkmName", xfkmName);//消费科目id

		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject getAllXfkmId(Integer gongsiId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xfkm/getAllXfkmId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject getXfkmId(Integer gongsiId,Integer ygId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xfkm/getXfkmId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		map.put("ygId", ygId);//查询者id	
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject addXfkmId(Integer gongsiId,Integer ygId,Integer xfkmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xfkm/addXfkmId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		map.put("ygId", ygId);//提交者id	
		map.put("xfkmId", xfkmId);//消费科目id
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject removeXfkmId(Integer gongsiId,Integer ygId,Integer xfkmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xfkm/removeXfkmId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		map.put("ygId", ygId);//提交者id	
		map.put("xfkmId", xfkmId);//消费科目id
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject delete(Integer gongsiId,Integer ygId,Integer xfkmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xfkm/delete";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		map.put("ygId", ygId);//提交者id	
		map.put("xfkmId", xfkmId);//消费科目id
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject insert(Integer gongsiId, Integer ygId, String xfkmName) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xfkm/insert";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		map.put("ygId", ygId);//提交者id	
		map.put("xfkmName", xfkmName);//消费科目名称
		
		return HttpClientUtils.shangchuan2(url, map);
	}
	
}
