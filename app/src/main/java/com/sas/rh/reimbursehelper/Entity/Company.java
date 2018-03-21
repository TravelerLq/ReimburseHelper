package com.sas.rh.reimbursehelper.Entity;

import java.util.Date;

public class Company {
    private Integer companyId;

    private String companyName;

    private String companyNature;

    private String vatCollectionMethods;

    private String incomeTaxCollectionMethods;

    private String taxId;

    private String openingBank;

    private String bankAccount;

    private String address;

    private String telephone;

    private String invoiceMethod;

    private String legalName;

    private String legalIdNumber;

    private Double companyQuota;

    private Integer createPersonId;

    private Date createTime;

    private Integer updatePersonId;

    private Date updateTime;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNature() {
        return companyNature;
    }

    public void setCompanyNature(String companyNature) {
        this.companyNature = companyNature;
    }

    public String getVatCollectionMethods() {
        return vatCollectionMethods;
    }

    public void setVatCollectionMethods(String vatCollectionMethods) {
        this.vatCollectionMethods = vatCollectionMethods;
    }

    public String getIncomeTaxCollectionMethods() {
        return incomeTaxCollectionMethods;
    }

    public void setIncomeTaxCollectionMethods(String incomeTaxCollectionMethods) {
        this.incomeTaxCollectionMethods = incomeTaxCollectionMethods;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getOpeningBank() {
        return openingBank;
    }

    public void setOpeningBank(String openingBank) {
        this.openingBank = openingBank;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getInvoiceMethod() {
        return invoiceMethod;
    }

    public void setInvoiceMethod(String invoiceMethod) {
        this.invoiceMethod = invoiceMethod;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalIdNumber() {
        return legalIdNumber;
    }

    public void setLegalIdNumber(String legalIdNumber) {
        this.legalIdNumber = legalIdNumber;
    }

    public Double getCompanyQuota() {
        return companyQuota;
    }

    public void setCompanyQuota(Double companyQuota) {
        this.companyQuota = companyQuota;
    }

    public Integer getCreatePersonId() {
        return createPersonId;
    }

    public void setCreatePersonId(Integer createPersonId) {
        this.createPersonId = createPersonId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getUpdatePersonId() {
        return updatePersonId;
    }

    public void setUpdatePersonId(Integer updatePersonId) {
        this.updatePersonId = updatePersonId;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}