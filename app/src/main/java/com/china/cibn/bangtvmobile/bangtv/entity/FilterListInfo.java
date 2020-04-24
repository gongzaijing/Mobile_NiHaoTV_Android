package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/22.
 */

public class FilterListInfo {

    private List<TimeBean> time;
    private List<TypeBean> type;
    private List<?> zone;

    public List<TimeBean> getTime() {
        return time;
    }

    public void setTime(List<TimeBean> time) {
        this.time = time;
    }

    public List<TypeBean> getType() {
        return type;
    }

    public void setType(List<TypeBean> type) {
        this.type = type;
    }

    public List<?> getZone() {
        return zone;
    }

    public void setZone(List<?> zone) {
        this.zone = zone;
    }

    public static class TimeBean {
        /**
         * title : 2015
         * value : 2015
         */

        private String title;
        private String value;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class TypeBean {
        /**
         * title : 精选
         * value : 纪录,传记,历史,名著,推荐
         */

        private String title;
        private String value;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
