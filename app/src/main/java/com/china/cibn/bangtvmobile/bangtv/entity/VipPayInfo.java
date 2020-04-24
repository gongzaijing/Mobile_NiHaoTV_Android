package com.china.cibn.bangtvmobile.bangtv.entity;

/**
 * Created by Administrator on 2018/4/4.
 */

public class VipPayInfo {


    /**
     * data : {"time":"2021-05-16到期","day":"","isVip":"1"}
     * msg :
     * code : 1
     */

    private DataBean data;
    private String msg;
    private String code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * time : 2021-05-16到期
         * day :
         * isVip : 1
         */

        private String time;
        private String day;
        private String isVip;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getIsVip() {
            return isVip;
        }

        public void setIsVip(String isVip) {
            this.isVip = isVip;
        }
    }
}
