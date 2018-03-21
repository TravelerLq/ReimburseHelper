package com.sas.rh.reimbursehelper.NetUtil;

import net.sf.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.sas.rh.reimbursehelper.NetworkUtil.AddressConfig.RootAddress;

public class ShenheUtils {
    public static void main(String[] args) throws UnsupportedEncodingException {

        insert();
//        getpdfinvoice();
//        revokeone();
//        getapprovermessage();
//        getcommittermessage();
//        dealwithapprove();
//        postApproverCAPdf();
    }

    //撤回单条发票报销
    public static void revokeone() throws UnsupportedEncodingException {
        String url = "http://101.200.85.207:8080/sas/approver/revokeone.do";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("CompanyID", 1);
        map.put("CommitterID", 1);
        map.put("InvoiceId", 30);
        HttpClientUtils.shangchuan2(url, map);
        System.out.println("运行结束");
    }

    //通过或驳回单条报销单审批，即处理审批
    public static void dealwithapprove() throws UnsupportedEncodingException {
        String url = "http://101.200.85.207:8080/sas/approver/dealwithapprove.do";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("CompanyID", 1);
        map.put("CommitterID", 1);
        map.put("InvoiceId", 30);
        map.put("ApproverState", 0);
        map.put("RejectReason", "测试");
        HttpClientUtils.shangchuan2(url, map);
        System.out.println("运行结束");
    }

   //报销者查看自己的报销单审核的进程
    public static JSONObject getcommittermessage(int CompanyId, int CommitterId) throws UnsupportedEncodingException {
        String url = RootAddress+"sas/approver/getcommittermessage.do";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("CompanyId", CompanyId);
        map.put("CommitterId", CommitterId);
        return HttpClientUtils.shangchuan2(url, map);

    }
    //审核者查看待自己审核的发票
    public static JSONObject getapprovermessage(int CompanyId, int CommitterId) throws UnsupportedEncodingException {
        String url = RootAddress+"sas/approver/getapprovermessage.do";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("CompanyId", CompanyId);
        map.put("CommitterId", CommitterId);
        return HttpClientUtils.shangchuan2(url, map);

    }

    //增加一个审核流程
    public static void insert() throws UnsupportedEncodingException {
        String url = "http://101.200.85.207:8080/sas/approver/add.do";
        Integer approverNum = 4;
        Integer approverId = 45;

        String approverName = "tedsft3";

        Integer approverRole = 5;

        Integer companyId = 7;

        Integer departmentId = 9;

        Integer committerId = 765;
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("CompanyId", companyId);
        map.put("DepartmentID", departmentId);
        map.put("CommitterID", committerId);
        map.put("ApproverNum", approverNum);
        map.put("ApproverID", approverId);
        map.put("ApproverName", approverName);
        map.put("ApproverRole", approverRole);
        HttpClientUtils.shangchuan2(url, map);

    }


    //发起报销流程
    public static void getpdfinvoice() throws UnsupportedEncodingException {
        String url = "http://101.200.85.207:8080/sas/approver/getpdfinvoice.do";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("InvoiceId", 30);
        map.put("InvoiceName", "yuanshen");
        map.put("CompanyId", 1);
        map.put("CommitterId", 1);
        HttpClientUtils.shangchuan2(url, map);
    }


    //发票审核者上传自己认证后的pdf
    public static void postApproverCAPdf() throws UnsupportedEncodingException {
        String filePath = "/Users/tuzhengsong/Desktop/公司文件/test/电子发票.pdf";
        String url = "http://101.200.85.207:8080/sas/approver/postApproverCAPdf.do";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("InvoiceId", 30);
        map.put("InvoiceName", "yuanshen");
        map.put("CompanyId", 1);
        map.put("CommitterId", 1);
        HttpClientUtils.shangchuan1(url, map, filePath);
    }
}
