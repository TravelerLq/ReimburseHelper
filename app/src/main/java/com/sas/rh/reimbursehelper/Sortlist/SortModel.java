package com.sas.rh.reimbursehelper.Sortlist;

import com.sas.rh.reimbursehelper.Bean.BaseBean;

public class SortModel extends BaseBean {

    private String name;   //显示的数�?
    private String sortLetters;  //显示数据拼音的首字母
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
