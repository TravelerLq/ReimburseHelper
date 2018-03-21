package com.sas.rh.reimbursehelper.NetUtil;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

public class ShlcUtils {
	public static void main(String[] args) throws UnsupportedEncodingException {
		insert(1,3,4,1);
//		update(5,1,5,3,1);
//		getShlc(1);
	}

	public static JSONObject insert(Integer bmId, Integer shlcMember, Integer shlcYouxianji, Integer createPerson) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/shlc/insert";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmId", bmId);
		map.put("shlcMember", shlcMember);
		map.put("shlcYouxianji", shlcYouxianji);
		map.put("createPerson", createPerson);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject update(Integer  shlcId,Integer bmId,Integer shlcMember,Integer shlcYouxianji,Integer updatePerson) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/shlc/update";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shlcId", shlcId);
		map.put("bmId", bmId);
		map.put("shlcMember", shlcMember);
		map.put("shlcYouxianji", shlcYouxianji);
		map.put("updatePerson", updatePerson);
		return HttpClientUtils.shangchuan2(url, map);
	}
	public static JSONObject getShlc(Integer bmId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/shlc/getShlc";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("bmId", bmId);
		return HttpClientUtils.shangchuan2(url, map);
	}
}
