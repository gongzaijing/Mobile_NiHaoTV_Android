package com.china.cibn.bangtvmobile.bangtv.entity.radio;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class RadioUrlInfo {

    /**
     * data : {"area":"us","play":[{"url":"http://sk.cri.cn/887/index.m3u8","code":"ca0f9ff178317ebc5acb7a4ddd47dd87","size":"SD","model":"http"}]}
     * pid : r-887
     * duration :
     * modify : 1522641339
     */

    private DataBean data;
    private String pid;
    private String duration;
    private int modify;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * area : us
         * play : [{"url":"http://sk.cri.cn/887/index.m3u8","code":"ca0f9ff178317ebc5acb7a4ddd47dd87","size":"SD","model":"http"}]
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
             * url : http://sk.cri.cn/887/index.m3u8
             * code : ca0f9ff178317ebc5acb7a4ddd47dd87
             * size : SD
             * model : http
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
