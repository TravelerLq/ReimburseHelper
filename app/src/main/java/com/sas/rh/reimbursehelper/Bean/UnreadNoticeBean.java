package com.sas.rh.reimbursehelper.Bean;

/**
 * Created by liqing on 17/12/5.
 */

public class UnreadNoticeBean extends BaseBean {


    /**
     * content : 测试
     * date : 2018-05-07 14:43:15
     * noticeId : 12
     * readStatus : false
     * title : 测试数据
     */

    private String content;
    private String date;
    private int noticeId;
    private boolean readStatus;
    private String title;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
