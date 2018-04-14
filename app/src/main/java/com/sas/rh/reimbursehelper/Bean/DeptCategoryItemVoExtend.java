package com.sas.rh.reimbursehelper.Bean;

public class DeptCategoryItemVoExtend {

    private Byte category;
    private String categoryName;
    private Byte item;
    private String itemName;
    private String remark;
    private String remark_written = "";
    private double amount = 0.0;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getRemark_written() {
        return remark_written;
    }

    public void setRemark_written(String remark_written) {
        this.remark_written = remark_written;
    }

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
