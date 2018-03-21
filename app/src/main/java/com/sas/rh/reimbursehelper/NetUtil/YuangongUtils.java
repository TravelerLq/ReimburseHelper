package com.sas.rh.reimbursehelper.NetUtil;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

public class YuangongUtils {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		String filePath = "C:\\Users\\Administrator\\Desktop\\sas web图片集\\topmid1.png";//员工头像
		
		//新增员工（包含头像）
//		insert("name","gender","1996-11-01","2009-12-12","1#2","password",1,2,"21321312312","ygYoujian","username",filePath);
		//删除指定id员工(ygId必填)
//		delete(4);
		//更新员工信息，单独更新头像亦可(ygId必填)
//		update(5,"name1","gender1","1996-11-01","2009-12-12","password",1,2,"21321312312","ygYoujian","username",filePath);
		//获取员工头像地址 HttpClientUtils里面通过jsonObject.get("ygTouxiang")可以获得头像地址（99行）
//		getYgTouxiang(5);
		//根据id获取员工信息 HttpClientUtils里面通过jsonObject.getJSONObject("resultOne").get("key(员工字段名)")获取字段（99行）
//		getYg(5);
		//获取公司下的所有员工  HttpClientUtils 需要自己具体解析jObject（106行）
//		getYgByGongsiId(1);
		//获取员工权限
//		queryYgQuanxian(2);
		//增加员工指定权限
//		addYgQuanxian(1,4);
		//删除员工指定权限
		deleteYgQuanxian(1,4);
	}
	public static JSONObject deleteYgQuanxian(int ygId, int qxId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/deleteYgQuanxian";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ygId", ygId);
		map.put("qxId", qxId);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject addYgQuanxian(int ygId, int qxId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/addYgQuanxian";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ygId", ygId);
		map.put("qxId", qxId);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject queryYgQuanxian(Integer ygId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/queryYgQuanxian";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ygId", ygId);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject getYgByGongsiId(Integer gongsiId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/getYgByGongsiId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject getYg(Integer ygId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/getYg";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ygId", ygId);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject getYgTouxiang(Integer ygId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/getYgTouxiang";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ygId", ygId);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject update(Integer ygId,String ygName,String ygGender,String ygBrith,String ygEnterdate,
			String ygPassword,Integer gongsiId,Integer bmId,String ygDianhua,
			String ygYoujian,String ygUsername,String filePath) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/update";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ygId", ygId);
		map.put("ygName", ygName);
		map.put("ygGender", ygGender);
		map.put("ygBrith", ygBrith);
		map.put("ygEnterdate", ygEnterdate);
//		map.put("ygQuanxian", ygQuanxian);
		map.put("ygPassword", ygPassword);
		map.put("gongsiId", gongsiId);
		map.put("bmId", bmId);
		map.put("ygDianhua", ygDianhua);
		map.put("ygYoujian", ygYoujian);
		map.put("ygUsername", ygUsername);
		// 图片本地地址，图片格式png或者jpg
		return HttpClientUtils.shangchuan1(url, map, filePath);
	}
	public static JSONObject delete(Integer ygId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/delete";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ygId", ygId);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject insert(String ygName, String ygGender, String ygBrith, String ygEnterdate,
									 String ygQuanxian, String ygPassword, Integer gongsiId, Integer bmId, String ygDianhua,
									 String ygYoujian, String ygUsername, String filePath) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/yg/insert";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("ygName", ygName);
		map.put("ygGender", ygGender);
		map.put("ygBrith", ygBrith);
		map.put("ygEnterdate", ygEnterdate);
		map.put("ygQuanxian", ygQuanxian);
		map.put("ygPassword", ygPassword);
		map.put("gongsiId", gongsiId);
		map.put("bmId", bmId);
		map.put("ygDianhua", ygDianhua);
		map.put("ygYoujian", ygYoujian);
		map.put("ygUsername", ygUsername);
		// 图片本地地址，图片格式png或者jpg
		return HttpClientUtils.shangchuan1(url, map, filePath);
	}
	
}
