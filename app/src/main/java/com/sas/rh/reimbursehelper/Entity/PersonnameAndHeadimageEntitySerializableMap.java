package com.sas.rh.reimbursehelper.Entity;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by IMAC86 on 2017/11/3.
 */

public class PersonnameAndHeadimageEntitySerializableMap implements Serializable {

    private Map<String,PersonnameAndHeadimageEntity> map;

    public Map<String, PersonnameAndHeadimageEntity> getMap() {
        return map;
    }

    public void setMap(Map<String, PersonnameAndHeadimageEntity> map) {
        this.map = map;
    }
}