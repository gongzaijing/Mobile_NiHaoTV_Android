package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * fix by gz  2018/4/18.
 *
 *观看历页面的获取数据项
 */

public class HistoryListInfo {


    private List<HistoryListBean> histroyList;

    public List<HistoryListBean> gethistoryList() {
        return histroyList;
    }

    public void setHistoryList(List<HistoryListBean> histroyList) {
        this.histroyList = histroyList;
    }

    public static class HistoryListBean {
        /**
         * id : 24
         * name : 2018元宵喜乐会
         *
         */

        private String id;
        private String name;
        private String image;
        private String timeposition;
        private String type;
        private String playerpos;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTimePosition() {
            return timeposition;
        }

        public void setTimePosition(String timeposition) {
            this.timeposition = timeposition;
        }

        public String gettype() { return type; }

        public void settype(String type) {
            this.type = type;
        }

        public String getplayerpos() {
            return playerpos;
        }

        public void setplayerpos(String playerpos) {
            this.playerpos = playerpos;
        }
    }

}
