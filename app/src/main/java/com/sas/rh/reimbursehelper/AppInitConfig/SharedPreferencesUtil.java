package com.sas.rh.reimbursehelper.AppInitConfig;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

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
//        writeCompanyId("1");
//        writeDId("1");
//        writeUserId("1");

    }

//================================UserCompany ID Operation==================================================

    public void setTestBase64(String base64) {
        editor.putString("base64", base64);
        editor.commit();
    }

    public void setSasBase64(String base64) {
        editor.putString("sasBase", base64);
        editor.commit();
    }

    public String getSasBase64() {
        String key = userSettings.getString("sasBase", null);
        Log.e("key=", "" + key);
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return key;
    }


    public String getTestBase64() {
        String key = userSettings.getString("base64", null);
        Log.e("key=", "" + key);
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return key;
    }




    //pdf签名的key
    public void setCertKey(String key) {
        editor.putString("key", key);
        editor.commit();
    }

    public String getKey() {
        String key = userSettings.getString("key", null);
        Log.e("key=", "" + key);
        if (TextUtils.isEmpty(key)) {
            return null;
        }
        return key;
    }

    public void setCert(String cert) {
        editor.putString("cert", cert);
        editor.commit();
    }

    public String getCert() {
        String cert = userSettings.getString("cert", null);
        Log.e("cert=", "" + cert);
        if (TextUtils.isEmpty(cert)) {
            return null;
        }
        return cert;
    }
//用户名
    public void setAccount(String account) {
        editor.putString("account", account);
        editor.commit();
    }

    public String getAccount() {
        String account = userSettings.getString("account", null);
        Log.e("account=", "" + account);
        if (TextUtils.isEmpty(account)) {
            return null;
        }
        return account;
    }




    public boolean isCidEmpty() {
        String cid = userSettings.getString("cid", null);
        if (cid == null || cid.equals("")) {
            return true;
        }
        return false;
    }

    public String getCid() {
        if (isCidEmpty() == true) {
            return null;
        }
        String cid = userSettings.getString("cid", "");
        return cid;
    }

    public int getCidNum() {
        if (isCidEmpty() == true) {
            return -1;
        }
        String cid = userSettings.getString("cid", "");
        return Integer.parseInt(cid);
    }

    public void writeCompanyId(String id) {
        editor.putString("cid", id);
        editor.commit();
    }

    public void clearCid() {
        editor.remove("cid");
    }

    //================================UserDepartment ID Operation==================================================
    public void writeDId(String id) {
        editor.putString("did", id);
        editor.commit();
    }

    public boolean isDidEmpty() {
        String did = userSettings.getString("did", null);
        if (did == null || did.equals("")) {
            return true;
        }
        return false;
    }

    public String getDid() {
        if (isDidEmpty() == true) {
            return "";
        }
        String did = userSettings.getString("did", "");
        return did;
    }

    public int getDidNum() {
        if (isDidEmpty() == true) {
            return -1;
        }
        String did = userSettings.getString("did", "");
        return Integer.parseInt(did);
    }

    public void clearDid() {
        editor.remove("did");
        editor.commit();
    }

    //================================UserIndetity ID Operation==================================================
    public void writeUserId(String id) {
        editor.putString("uid", id);
        editor.commit();
    }

    public boolean isUidEmpty() {
        String uid = userSettings.getString("uid", null);
        if (uid == null || uid.equals("")) {
            return true;
        }
        return false;
    }

    public String getUid() {
        if (isUidEmpty() == true) {
            return "";
        }
        String uid = userSettings.getString("uid", "");
        return uid;
    }

    public int getUidNum() {
        if (isUidEmpty() == true) {
            return -1;
        }
        String uid = userSettings.getString("uid", "");
        return Integer.parseInt(uid);
    }

    public void clearUid() {
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

//    public void setUserData(String account, String psw, String id, String tel, String email) {
//        editor.putString("account", account);
//        editor.putString("psw",psw);
//        editor.putString("id", id);
//        editor.putString("tel",tel);
//        editor.putString("email",email);
//        editor.commit();
//    }
//    public List<String> getUserData(String account, String psw, String id, String tel, String email) {
//        List<String> list=new ArrayList<>();
//
//        String accountStr = userSettings.getString("account", "");
//        list.add(accountStr);
//        return Integer.parseInt(uid);
//    }


}