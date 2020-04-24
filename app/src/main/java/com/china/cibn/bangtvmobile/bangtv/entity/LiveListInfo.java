package com.china.cibn.bangtvmobile.bangtv.entity;

/**
 * Created by cibn on 2018/4/26.
 */

public class LiveListInfo {

    /**
     * channelId : 6697
     * channelName : CCTV2财经频道
     * logo : living/20161216/6697.png
     * no : 1
     * urlType : 2
     * urlid : p2p://198.255.14.43:9905/http://live01-wscdn.cibntv.tv/cctv2/cctv2.m3u8
     * groupid : 1
     * groupname : 央视频道
     * onplay :
     * programId :
     * isOpen : 1
     * isVip : 1
     * isplay : 1
     */

    private String channelId;
    private String channelName;
    private String logo;
    private String no;
    private int urlType;
    private String urlid;
    private String groupid;
    private String groupname;
    private String onplay;
    private String programId;
    private int isOpen;
    private int isVip;
    private String isplay;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public int getUrlType() {
        return urlType;
    }

    public void setUrlType(int urlType) {
        this.urlType = urlType;
    }

    public String getUrlid() {
        return urlid;
    }

    public void setUrlid(String urlid) {
        this.urlid = urlid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getOnplay() {
        return onplay;
    }

    public void setOnplay(String onplay) {
        this.onplay = onplay;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getIsplay() {
        return isplay;
    }

    public void setIsplay(String isplay) {
        this.isplay = isplay;
    }
}
