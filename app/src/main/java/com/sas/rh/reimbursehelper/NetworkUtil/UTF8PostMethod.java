package com.sas.rh.reimbursehelper.NetworkUtil;

import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author tuzhengsong
 * 创建PostMethod的子类设置编码格式
 */
public class UTF8PostMethod extends PostMethod {
    public UTF8PostMethod(String url){
        super(url);
    }

    @Override
    public String getRequestCharSet() {
        //return super.getRequestCharSet();
        return "UTF-8";
    }
}