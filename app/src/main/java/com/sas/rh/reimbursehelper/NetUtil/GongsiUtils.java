package com.sas.rh.reimbursehelper.NetUtil;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

public class GongsiUtils {
//	public static void main(String[] args) throws UnsupportedEncodingException {
	/*1.新增公司（公司名称，公司性质，增值税征收方式，所得税征收方式，
		公司税号，开户银行，银行账号，公司地址，公司电话，开票方式，创建者id）*/
//	insert("测试公司","上市","增值税征收方式","所得税征收方式","公司税号",
//				"开户银行","银行账号","公司地址","公司电话","开票方式",2);
//		2.查询公司 HttpClientUtils里面通过jsonObject.getJSONObject("resultOne").get("key(部门字段名)")获取字段（99行）
//		query(1);
//		3.查询所有公司信息 HttpClientUtils 需要自己具体解析jObject（106行）
//		queryAll();
//		4.修改公司信息
//		update(3,"测试公司","上市1","增值税征收方式","所得税征收方式","公司税号",
//				"开户银行","银行账号","公司地址","公司电话","开票方式",2);
//	}

	public static JSONObject update(Integer gongsiId,String gongsiName,String gongsiProperty,String gongsiVat,
			String gongsiIncometax,String taxNumber,String depositBank,String bankAccount,
			String address,String telephone,String invoicePatten,String identityCard,Double gongsiLimit,Integer updatePerson) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/gongsi/update";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		map.put("gongsiName", gongsiName);
		map.put("gongsiProperty", gongsiProperty);
		map.put("gongsiVat", gongsiVat);
		map.put("gongsiIncometax", gongsiIncometax);
		map.put("taxNumber", taxNumber);
		map.put("depositBank", depositBank);
		map.put("bankAccount", bankAccount);
		map.put("address", address);
		map.put("telephone", telephone);
		map.put("invoicePatten", invoicePatten);
        map.put("identityCard", identityCard);
        map.put("gongsiLimit", gongsiLimit);
		map.put("updatePerson", updatePerson);
		return HttpClientUtils.shangchuan2(url, map);
	}

	private static void queryAll() throws UnsupportedEncodingException {
		String url = RootAddress+"sas/gongsi/queryAll";
		Map<String, Object> map = new HashMap<String, Object>();
		HttpClientUtils.shangchuan2(url, map);
	}

	private static void query(Integer gongsiId) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/gongsi/query";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiId", gongsiId);
		HttpClientUtils.shangchuan2(url, map);
	}

	public static JSONObject insert(String gongsiName, String gongsiProperty, String gongsiVat,
									 String gongsiIncometax, String taxNumber, String depositBank, String bankAccount,
									 String address, String telephone, String invoicePatten, String identityCard,Double gongsiLimit,Integer createPerson) throws UnsupportedEncodingException {
		String url = RootAddress+"sas/gongsi/insert";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("gongsiName", gongsiName);
		map.put("gongsiProperty", gongsiProperty);
		map.put("gongsiVat", gongsiVat);
		map.put("gongsiIncometax", gongsiIncometax);
		map.put("taxNumber", taxNumber);
		map.put("depositBank", depositBank);
		map.put("bankAccount", bankAccount);
		map.put("address", address);
		map.put("telephone", telephone);
		map.put("invoicePatten", invoicePatten);
		map.put("identityCard", identityCard);
		map.put("gongsiLimit", gongsiLimit);
		map.put("createPerson", createPerson);
		return HttpClientUtils.shangchuan2(url, map);
	}
	
}
