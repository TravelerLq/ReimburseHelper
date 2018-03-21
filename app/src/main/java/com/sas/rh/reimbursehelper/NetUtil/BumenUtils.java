package com.sas.rh.reimbursehelper.NetUtil;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

public class BumenUtils {
	public static void main(String[] args) throws UnsupportedEncodingException {
		//新增部门
//		insert("测试部门",1,2,50.0,2);
//		修改部门（不需要数据用null替代）bmId必须有
//		update(13,"测试部门1",1,2,609.0,2);
		//根据公司查询部门
//		queryByGongsiId(1);
		//根据部门id查询部门 HttpClientUtils里面通过jsonObject.getJSONObject("resultOne").get("key(部门字段名)")获取字段（99行）
//		queryById(13);
		//删除部门
//		delete(12);
		//设置开启
//		setOpen(12);
		//设置关闭
		setClose(13);
	}

	public static JSONObject setClose(Integer bmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/bumen/setClose";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmId", bmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject setOpen(Integer bmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/bumen/setOpen";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmId", bmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject delete(Integer bmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/bumen/delete";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmId", bmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject queryById(Integer bmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/bumen/queryById";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmId", bmId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject queryByGongsiId(Integer gongsiId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/bumen/queryByGongsiId";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		return HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject update(Integer bmId,String buName,Integer gongsiId,
							   Integer bmMaster,Double bmLimit,Integer updatePerson) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/bumen/update";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmId", bmId);
		map.put("bmName", buName);
		map.put("gongsiId", gongsiId);
		map.put("bmMaster", bmMaster);
		map.put("bmLimit", bmLimit);
		map.put("updatePerson", updatePerson);
		return HttpClientUtils.shangchuan2(url, map);

	}

	public static JSONObject insert(String buName, Integer gongsiId,
									 Integer bmMaster, Double bmLimit, Integer createPerson) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/bumen/insert";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmName", buName);
		map.put("gongsiId", gongsiId);
		map.put("bmMaster", bmMaster);
		map.put("bmLimit", bmLimit);
		map.put("createPerson", createPerson);
		return HttpClientUtils.shangchuan2(url, map);
	}
}
