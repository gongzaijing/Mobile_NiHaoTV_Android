package com.china.cibn.bangtvmobile.bangtv.entity;

/**
 * Created by Administrator on 2018/3/21.
 */

public class AuthenticationInfo {

    /**
     * addressList : {"epg":"https://ag05.ott.bangtv.tv/v28","statistic":"http://st.ott.bangtv.tv"}
     * resultCode : 0
     * templateId : 00080000000000000000000000000050
     * state : 1111
     * token : guest123456
     * deviceId : 0
     * userId : guest
     */

    private AddressListBean addressList;
    private String resultCode;
    private String templateId;
    private String state;
    private String token;
    private String deviceId;
    private String userId;

    public AddressListBean getAddressList() {
        return addressList;
    }

    public void setAddressList(AddressListBean addressList) {
        this.addressList = addressList;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public static class AddressListBean {
        /**
         * epg : https://ag05.ott.bangtv.tv/v28
         * statistic : http://st.ott.bangtv.tv
         */

        private String epg;
        private String statistic;

        public String getEpg() {
            return epg;
        }

        public void setEpg(String epg) {
            this.epg = epg;
        }

        public String getStatistic() {
            return statistic;
        }

        public void setStatistic(String statistic) {
            this.statistic = statistic;
        }
    }
}
