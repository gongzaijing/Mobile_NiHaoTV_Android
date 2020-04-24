package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/30.
 */

public class VideoUrlInfo {

    /**
     * data : [{"area":"us","play":[{"url":"http://vod-wscdn.cibntv.tv/tv/movie/2671/5241.m3u8?wsSecret=f3949d3c91b416e1a15e4d0d3dcd250e&wsTime=5abdbc2f","code":"d2b5509d0d4adde51683e4c91f97e5c8","size":"SD","model":"cdn"}]}]
     * pid : m-261
     * duration :
     * modify : 1522382119
     */

    private String pid;
    private String duration;
    private int modify;
    private List<DataBean> data;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getModify() {
        return modify;
    }

    public void setModify(int modify) {
        this.modify = modify;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * area : us
         * play : [{"url":"http://vod-wscdn.cibntv.tv/tv/movie/2671/5241.m3u8?wsSecret=f3949d3c91b416e1a15e4d0d3dcd250e&wsTime=5abdbc2f","code":"d2b5509d0d4adde51683e4c91f97e5c8","size":"SD","model":"cdn"}]
         */

        private String area;
        private List<PlayBean> play;

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public List<PlayBean> getPlay() {
            return play;
        }

        public void setPlay(List<PlayBean> play) {
            this.play = play;
        }

        public static class PlayBean {
            /**
             * url : http://vod-wscdn.cibntv.tv/tv/movie/2671/5241.m3u8?wsSecret=f3949d3c91b416e1a15e4d0d3dcd250e&wsTime=5abdbc2f
             * code : d2b5509d0d4adde51683e4c91f97e5c8
             * size : SD
             * model : cdn
             */

            private String url;
            private String code;
            private String size;
            private String model;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
            }
        }
    }
}
