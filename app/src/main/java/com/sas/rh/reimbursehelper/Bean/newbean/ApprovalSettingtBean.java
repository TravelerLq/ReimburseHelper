package com.sas.rh.reimbursehelper.Bean.newbean;

import com.sas.rh.reimbursehelper.Bean.BaseBean;

/**
 * Created by liqing on 18/5/3.
 */

public class ApprovalSettingtBean extends BaseBean {
    private String dname;
    private String num;
    private String name;
    /**
     * approveNumId : 24
     * approveNumName : 部门审批
     * approverId : 54
     * approverName : 王朕
     * approverOrder : 1
     */

    private int approveNumId;
    private String approveNumName;
    private int approverId;
    private String approverName;
    private int approverOrder;

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getApproveNumId() {
        return approveNumId;
    }

    public void setApproveNumId(int approveNumId) {
        this.approveNumId = approveNumId;
    }

    public String getApproveNumName() {
        return approveNumName;
    }

    public void setApproveNumName(String approveNumName) {
        this.approveNumName = approveNumName;
    }

    public int getApproverId() {
        return approverId;
    }

    public void setApproverId(int approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public int getApproverOrder() {
        return approverOrder;
    }

    public void setApproverOrder(int approverOrder) {
        this.approverOrder = approverOrder;
    }
}
