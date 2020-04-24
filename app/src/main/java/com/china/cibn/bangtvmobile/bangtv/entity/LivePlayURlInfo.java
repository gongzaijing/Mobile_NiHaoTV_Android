package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by cibn on 2018/4/26.
 */

public class LivePlayURlInfo {

    /**
     * data : [{"area":"us","play":[{"url":"http://live01-wscdn.cibntv.tv/cctv9/cctv9.m3u8?wsSecret=26c3d666af8cb727ba07e50fda3bf073&wsTime=5ae1a4b7","code":"ce92e0b8783a1f7a076a987ccd781547","size":"SD","model":"cdn"}]}]
     * lid : 6698
     * duration :
     * modify : 1524735407
     */

    private String lid;
    private String duration;
    private int modify;
    private List<DataBean> data;

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
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
         * play : [{"url":"http://live01-wscdn.cibntv.tv/cctv9/cctv9.m3u8?wsSecret=26c3d666af8cb727ba07e50fda3bf073&wsTime=5ae1a4b7","code":"ce92e0b8783a1f7a076a987ccd781547","size":"SD","model":"cdn"}]
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
             * url : http://live01-wscdn.cibntv.tv/cctv9/cctv9.m3u8?wsSecret=26c3d666af8cb727ba07e50fda3bf073&wsTime=5ae1a4b7
             * code : ce92e0b8783a1f7a076a987ccd781547
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
