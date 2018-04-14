package cn.unitid.spark.cm.sdk.data.entity;

import java.util.List;

/**
 * 注册证书实体类
 * Created by lyb on 2016/8/22.
 */
public class RegisterInfo {
    private String templateId;
    private List<Item> itemList;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
