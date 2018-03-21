package com.sas.rh.reimbursehelper.NetworkUtil;

import org.apache.commons.httpclient.methods.GetMethod;

/**
 * @author tuzhengsong
 */
public class UTF8GetMethod extends GetMethod{

    public UTF8GetMethod(String url){
        super(url);
    }

    @Override
    public String getRequestCharSet() {
        //return super.getRequestCharSet();
        return "UTF-8";
    }
}
