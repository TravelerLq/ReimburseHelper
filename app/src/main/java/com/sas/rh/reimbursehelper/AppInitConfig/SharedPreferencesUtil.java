package com.sas.rh.reimbursehelper.AppInitConfig;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dh86 on 2018/1/8.
 */

public class SharedPreferencesUtil {

    private Context context;
    private SharedPreferences userSettings;
    private SharedPreferences.Editor editor;

    public SharedPreferencesUtil(Context context) {
        this.context = context;
        userSettings = context.getSharedPreferences("firstsetting", 0);
        editor = userSettings.edit();
        writeCompanyId("1");
        writeDId("1");
        writeUserId("1");

    }

//================================UserCompany ID Operation==================================================

    public void writeCompanyId(String id){
        editor.putString("cid",id);
        editor.commit();
    }

    public boolean isCidEmpty(){
        String cid = userSettings.getString("cid",null);
        if(cid == null || cid.equals("")){
            return true;
        }
        return false;
    }

    public String getCid(){
        if(isCidEmpty() == true){
            return null;
        }
        String cid = userSettings.getString("cid","");
        return cid;
    }

    public int getCidNum(){
        if(isCidEmpty() == true){
            return -1;
        }
        String cid = userSettings.getString("cid","");
        return Integer.parseInt(cid);
    }

    public void clearCid(){
        editor.remove("cid");
    }

//================================UserDepartment ID Operation==================================================
    public void writeDId(String id){
        editor.putString("did",id);
        editor.commit();
    }

    public boolean isDidEmpty(){
        String did = userSettings.getString("did",null);
        if(did == null || did.equals("")){
            return true;
        }
        return false;
    }

    public String getDid(){
        if(isDidEmpty() == true){
            return "";
        }
        String did = userSettings.getString("did","");
        return did;
    }

    public int getDidNum(){
        if(isDidEmpty() == true){
            return -1;
        }
        String did = userSettings.getString("did","");
        return Integer.parseInt(did);
    }

    public void clearDid(){
        editor.remove("did");
        editor.commit();
    }

    //================================UserIndetity ID Operation==================================================
    public void writeUserId(String id){
        editor.putString("uid",id);
        editor.commit();
    }

    public boolean isUidEmpty(){
        String uid = userSettings.getString("uid",null);
        if(uid == null || uid.equals("")){
            return true;
        }
        return false;
    }

    public String getUid(){
        if(isUidEmpty() == true){
            return "";
        }
        String uid = userSettings.getString("uid","");
        return uid;
    }

    public int getUidNum(){
        if(isUidEmpty() == true){
            return -1;
        }
        String uid = userSettings.getString("uid","");
        return Integer.parseInt(uid);
    }

    public void clearUid(){
        editor.remove("uid");
        editor.commit();
    }
//================================C&U==================================================
//    public boolean isCidAndUidEmpty(){
//        String uid = userSettings.getString("uid",null);
//        String cid = userSettings.getString("cid",null);
//        if((uid == null || uid.equals("")) && ()){
//            return true;
//        }
//        return false;
//    }

}
