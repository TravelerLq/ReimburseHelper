package cn.unitid.spark.cm.sdk.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lyb on 2016/6/29.
 */
public class UrlParser {

    public static Map<String, String> parser(String url){

        Map<String, String> mapRequest = new HashMap<String, String>();
        try {
            url=URLDecoder.decode(url,"UTF-8");
            String[] items=url.split("[?]");
            if(items.length>1){
                String [] params=items[1].split("[&]");
                if(params.length>0){
                    for(String param:params){
                       int index= param.indexOf("=");
                        mapRequest.put(param.substring(0,index),param.substring(index+1));

                    }

                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return mapRequest;
    }
}
