package com.sas.rh.reimbursehelper.Bean.newbean;

import android.graphics.Bitmap;

import com.sas.rh.reimbursehelper.Bean.BaseBean;

import java.util.List;

/**
 * Created by liqing on 18/3/29.
 */

public class ApprovalAllDetailBean {


    /**
     * approvalProcessVoAppArrayList : [{"approvalName":"部门审批","approver":"王朕","date":"2018-04-26 20:36:25","number":1,"result":"true"}]
     * singleReimVoAppArrayList : [{"date":"2018-04-26","expenseId":306,"money":"23.66","name":"餐饮费","remark":"well"}]
     * status : 200
     */

    private int status;
    private List<ApprovalProcessVoAppArrayListBean> approvalProcessVoAppArrayList;
    private List<SingleReimVoAppArrayListBean> singleReimVoAppArrayList;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ApprovalProcessVoAppArrayListBean> getApprovalProcessVoAppArrayList() {
        return approvalProcessVoAppArrayList;
    }

    public void setApprovalProcessVoAppArrayList(List<ApprovalProcessVoAppArrayListBean> approvalProcessVoAppArrayList) {
        this.approvalProcessVoAppArrayList = approvalProcessVoAppArrayList;
    }

    public List<SingleReimVoAppArrayListBean> getSingleReimVoAppArrayList() {
        return singleReimVoAppArrayList;
    }

    public void setSingleReimVoAppArrayList(List<SingleReimVoAppArrayListBean> singleReimVoAppArrayList) {
        this.singleReimVoAppArrayList = singleReimVoAppArrayList;
    }

    public static class ApprovalProcessVoAppArrayListBean {
        /**
         * approvalName : 部门审批
         * approver : 王朕
         * date : 2018-04-26 20:36:25
         * number : 1
         * result : true
         */

        private String approvalName;
        private String approver;
        private String date;
        private int number;
        private String result;

        public String getApprovalName() {
            return approvalName;
        }

        public void setApprovalName(String approvalName) {
            this.approvalName = approvalName;
        }

        public String getApprover() {
            return approver;
        }

        public void setApprover(String approver) {
            this.approver = approver;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }

    public static class SingleReimVoAppArrayListBean extends BaseBean {
        /**
         * date : 2018-04-26
         * expenseId : 306
         * money : 23.66
         * name : 餐饮费
         * remark : well
         */

        private String date;
        private int expenseId;
        private String money;
        private String name;
        private String remark;

        private String fileData;
        private String fileDate;
        private String fileName;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileDate() {
            return fileDate;
        }

        public void setFileDate(String fileDate) {
            this.fileDate = fileDate;
        }

        public String getFileData() {
            return fileData;
        }

        public void setFileData(String fileData) {
            this.fileData = fileData;
        }


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getExpenseId() {
            return expenseId;
        }

        public void setExpenseId(int expenseId) {
            this.expenseId = expenseId;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
