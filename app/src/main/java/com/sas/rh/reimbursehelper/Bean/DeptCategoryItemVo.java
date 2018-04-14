package com.sas.rh.reimbursehelper.Bean;

public class DeptCategoryItemVo {

    private Byte category;
    private String categoryName;
    private Byte item;
    private String itemName;
    private String remark;

    public Byte getCategory() {
        return category;
    }

    public void setCategory(Byte category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Byte getItem() {
        return item;
    }

    public void setItem(Byte item) {
        this.item = item;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
