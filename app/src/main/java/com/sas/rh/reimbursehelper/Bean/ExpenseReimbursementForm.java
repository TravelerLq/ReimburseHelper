package com.sas.rh.reimbursehelper.Bean;

import java.util.Date;

public class ExpenseReimbursementForm {
    private Integer formId;

    private String formName;

    private Integer reimbursementDepartmentId;

    private Date reimbursementDate;

    private Double totalAmount;

    private Byte leadershipSignature;

    private String reviewComments;

    private Byte reimbursementSignature;

    private String amountCapital;

    private Double originalLoan;

    private Double refundBalance;

    private Double makeUpDifference;

    private Byte annexCount;

    private Integer reimbursementPersonId;

    private Integer companyId;

    private Byte status;

    private Byte formTemplateId;

    private Byte expenseCategoryId;

    public Integer getFormId() {
        return formId;
    }

    public void setFormId(Integer formId) {
        this.formId = formId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Integer getReimbursementDepartmentId() {
        return reimbursementDepartmentId;
    }

    public void setReimbursementDepartmentId(Integer reimbursementDepartmentId) {
        this.reimbursementDepartmentId = reimbursementDepartmentId;
    }

    public Date getReimbursementDate() {
        return reimbursementDate;
    }

    public void setReimbursementDate(Date reimbursementDate) {
        this.reimbursementDate = reimbursementDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Byte getLeadershipSignature() {
        return leadershipSignature;
    }

    public void setLeadershipSignature(Byte leadershipSignature) {
        this.leadershipSignature = leadershipSignature;
    }

    public String getReviewComments() {
        return reviewComments;
    }

    public void setReviewComments(String reviewComments) {
        this.reviewComments = reviewComments;
    }

    public Byte getReimbursementSignature() {
        return reimbursementSignature;
    }

    public void setReimbursementSignature(Byte reimbursementSignature) {
        this.reimbursementSignature = reimbursementSignature;
    }

    public String getAmountCapital() {
        return amountCapital;
    }

    public void setAmountCapital(String amountCapital) {
        this.amountCapital = amountCapital;
    }

    public Double getOriginalLoan() {
        return originalLoan;
    }

    public void setOriginalLoan(Double originalLoan) {
        this.originalLoan = originalLoan;
    }

    public Double getRefundBalance() {
        return refundBalance;
    }

    public void setRefundBalance(Double refundBalance) {
        this.refundBalance = refundBalance;
    }

    public Double getMakeUpDifference() {
        return makeUpDifference;
    }

    public void setMakeUpDifference(Double makeUpDifference) {
        this.makeUpDifference = makeUpDifference;
    }

    public Byte getAnnexCount() {
        return annexCount;
    }

    public void setAnnexCount(Byte annexCount) {
        this.annexCount = annexCount;
    }

    public Integer getReimbursementPersonId() {
        return reimbursementPersonId;
    }

    public void setReimbursementPersonId(Integer reimbursementPersonId) {
        this.reimbursementPersonId = reimbursementPersonId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getFormTemplateId() {
        return formTemplateId;
    }

    public void setFormTemplateId(Byte formTemplateId) {
        this.formTemplateId = formTemplateId;
    }

    public Byte getExpenseCategoryId() {
        return expenseCategoryId;
    }

    public void setExpenseCategoryId(Byte expenseCategoryId) {
        this.expenseCategoryId = expenseCategoryId;
    }
}