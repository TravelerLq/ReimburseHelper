package com.sas.rh.reimbursehelper.Bean;

/**
 * Created by liqing on 18/3/21.
 * 三级报销单Bean
 */

public class ThirdExpenseCategoryBean extends BaseBean {


    /**
     * category : 2
     * categoryName : 业务招待费
     * item : 1 三级类别ID
     * itemName : 住宿费
     * remark : 招待客户**；住宿时间、地点
     */

    private String category;
    private String categoryName;
    private String item;
    private String itemName;
    private String remark;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
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
