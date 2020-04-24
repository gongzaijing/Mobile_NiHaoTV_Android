package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20.
 */

public class MenuListInfo {


    /**
     * data : [{"id":"49","name":"分类","nodeType":"COMMONCATG","action":"OpenUrl","actionURL":""},{"id":"50","name":"推荐","nodeType":"COMMONCATG","action":"OpenUrl","actionURL":""},{"id":"51","name":"直播","nodeType":"COMMONCATG","action":"OpenUrl","actionURL":""},{"id":"52","name":"设置","nodeType":"COMMONCATG","action":"OpenUrl","actionURL":""}]
     * city_name : 北京
     * try_play_time : 600000
     */

    private String city_name;
    private int try_play_time;
    private List<DataBean> data;

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public int getTry_play_time() {
        return try_play_time;
    }

    public void setTry_play_time(int try_play_time) {
        this.try_play_time = try_play_time;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 49
         * name : 分类
         * nodeType : COMMONCATG
         * action : OpenUrl
         * actionURL :
         */

        private String id;
        private String name;
        private String nodeType;
        private String action;
        private String actionURL;

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

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public String getActionURL() {
            return actionURL;
        }

        public void setActionURL(String actionURL) {
            this.actionURL = actionURL;
        }
    }
}
