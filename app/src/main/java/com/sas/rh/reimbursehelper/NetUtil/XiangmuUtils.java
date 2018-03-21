package com.sas.rh.reimbursehelper.NetUtil;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

public class XiangmuUtils {
	public static void main(String[] args) throws UnsupportedEncodingException {
		//新增项目(可以增加人员)
//		insert(1,1,1,2,200.1,"测试项目",0,"1#2#3");
		//修改项目
//		update(31,1,1,1,2,200.1,"测试项目改",0);
//		删除项目
//		delete(22); 
		//根据id查询项目 HttpClientUtils里面通过jsonObject.getJSONObject("resultOne").get("key(员工字段名)")获取字段（99行）
//		query(1);
//		查询公司下所有项目  HttpClientUtils 需要自己具体解析jObject（106行）resultList已经解析
//		queryByGongsiId(1);
		//增加项目成员
//		addMember(1,2);
//		删除项目成员
//		deleteMember(1,2); HttpClientUtils 需要自己具体解析jObject（106行）resultList已经解析
		//查询项目成员
//		queryMember(1);
		//查询项目审核状态 HttpClientUtils里面通过jsonObject.getString("resultOne")获取审核状态
//		queryZt(1);
	}

	public static JSONObject queryZt(Integer xmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/queryZt";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject queryMember(Integer xmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/queryMember";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject deleteMember(Integer xmId,Integer ygId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/deleteMember";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		map.put("ygId", ygId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject addMember(Integer xmId,Integer ygId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/addMember";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		map.put("ygId", ygId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject queryByGongsiId(Integer gongsiId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/queryByGongsiId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject query(Integer xmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/query";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject delete(Integer xmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/delete";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject update(Integer xmId,Integer gongsiId,Integer bmId,Integer ygId, Integer updatePerson,
			Double xmJine,String xmName,Integer xmZt) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/update";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		map.put("gongsiId", gongsiId);
		map.put("bmId", bmId);
		map.put("ygId", ygId);
		map.put("updatePerson", updatePerson);
		map.put("xmJine", xmJine);
		map.put("xmName", xmName);
		map.put("xmZt", xmZt);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject insert(Integer gongsiId, Integer bmId, Integer ygId, Integer createPerson,
									 Double xmJine, String xmName, Integer xmZt, String member) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/insert";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		map.put("bmId", bmId);
		map.put("ygId", ygId);
		map.put("createPerson", createPerson);
		map.put("xmJine", xmJine);
		map.put("xmName", xmName);
		map.put("xmZt", xmZt);
		map.put("member", member);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject setClose(Integer xmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/setClose";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject setOpen(Integer xmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/xm/setOpen";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("xmId", xmId);
		return HttpClientUtils.shangchuan2(url, map);

	}
	
}
