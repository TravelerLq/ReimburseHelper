package com.sas.rh.reimbursehelper.Bean;

import java.util.Date;

/**
 * Created by liqing on 17/12/5.
 */

public class UnreadNoticeBean extends BaseBean {

    /**
     * MsgId : f82e3b7d-fa7b-417a-ac82-445084b9c42a
     * Sender : -1
     * Receiver : 3
     * MsgTitle : 消息测试
     * MsgContent : 消息测试
     * CreateDate : 2017-12-04 00:00:00
     * Status 0是未读，1是已读
     *   {
     "MsgId": "c65e0955-57f5-4506-b8e8-bf57af304866",
     "Sender": "-1",
     "Receiver": "2",
     "MsgTitle": "消息测试",
     "MsgContent": "消息测试",
     "CreateDate": "2017-12-04 00:00:00",
     "Status": 1
     }
     */

    private Integer noticeId;

    private Byte noticeTypeId;

    private String title;

    private String content;

    private Date date;

    private Integer companyId;

    private Integer initiateUserid;

    private Integer affiliatedUserid;

    public Integer getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Integer noticeId) {
        this.noticeId = noticeId;
    }

    public Byte getNoticeTypeId() {
        return noticeTypeId;
    }

    public void setNoticeTypeId(Byte noticeTypeId) {
        this.noticeTypeId = noticeTypeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getInitiateUserid() {
        return initiateUserid;
    }

    public void setInitiateUserid(Integer initiateUserid) {
        this.initiateUserid = initiateUserid;
    }

    public Integer getAffiliatedUserid() {
        return affiliatedUserid;
    }

    public void setAffiliatedUserid(Integer affiliatedUserid) {
        this.affiliatedUserid = affiliatedUserid;
    }
}
