package com.sas.rh.reimbursehelper.data;

import com.anupcowkur.reservoir.Reservoir;
import com.sas.rh.reimbursehelper.Bean.SaveUserBean;
import com.sas.rh.reimbursehelper.Bean.UserBean;
import com.sas.rh.reimbursehelper.constant.Constant;


/**
 * Description：FireCommand
 * Created by：Kyle
 * Date：2017/2/9
 */
public class UserData {
    public static SaveUserBean getUserInfo(){
        SaveUserBean userBean=null;
        try {
            if (Reservoir.contains(Constant.KEY_USER)) {
                userBean= Reservoir.get(Constant.KEY_USER, SaveUserBean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userBean;
    }
    public static void saveUser(SaveUserBean userBean){
        try {
            Reservoir.put(Constant.KEY_USER,userBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void removeUser(){
        try {
            Reservoir.delete(Constant.KEY_USER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void setSave(boolean save){
        try {
            Reservoir.put(Constant.KEY_ISSAVE, save);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isSave(){
        boolean isSave=true;
        try {
            if (Reservoir.contains(Constant.KEY_ISSAVE)) {
                isSave= Reservoir.get(Constant.KEY_ISSAVE, boolean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSave;
    }
    public static void setAutoLogin(boolean save){
        try {
            Reservoir.put(Constant.KEY_AUTO_LOGIN, save);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isAuto(){
        boolean isSave=true;
        try {
            if (Reservoir.contains(Constant.KEY_AUTO_LOGIN)) {
                isSave= Reservoir.get(Constant.KEY_AUTO_LOGIN, boolean.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSave;
    }
}
