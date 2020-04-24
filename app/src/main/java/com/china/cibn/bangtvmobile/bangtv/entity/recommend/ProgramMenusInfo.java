package com.china.cibn.bangtvmobile.bangtv.entity.recommend;

import java.util.List;

/**
 * Created by Administrator on 2018/2/24.
 */

public class ProgramMenusInfo {

    /**
     * parentMenu : {"parent_id":"movie","name":"电影","nodeType":"MOVIECATG","action":"OPEN_DETAIL","actionURL":""}
     * menuList : [{"id":"movie-34","name":"院线大片","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"},{"id":"movie-77","name":"网大精选","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"},{"id":"movie-35","name":"爆笑喜剧","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"},{"id":"movie-36","name":"唯美爱情","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"},{"id":"movie-38","name":"动作巨制","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"},{"id":"movie-39","name":"恐怖悬疑","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"},{"id":"movie-40","name":"动漫剧场","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"},{"id":"movie-41","name":"人物传记","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"},{"id":"movie-55","name":"抢鲜·看","nodeType":"MOVIECATG","action":"OPEN_PROGRAM_LIST"}]
     */

    private ParentMenuBean parentMenu;
    private List<MenuListBean> menuList;

    public ParentMenuBean getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(ParentMenuBean parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<MenuListBean> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuListBean> menuList) {
        this.menuList = menuList;
    }

    public static class ParentMenuBean {
        /**
         * parent_id : movie
         * name : 电影
         * nodeType : MOVIECATG
         * action : OPEN_DETAIL
         * actionURL :
         */

        private String parent_id;
        private String name;
        private String nodeType;
        private String action;
        private String actionURL;

        public String getParent_id() {
            return parent_id;
        }

        public void setParent_id(String parent_id) {
            this.parent_id = parent_id;
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

    public static class MenuListBean {
        /**
         * id : movie-34
         * name : 院线大片
         * nodeType : MOVIECATG
         * action : OPEN_PROGRAM_LIST
         */

        private String id;
        private String name;
        private String nodeType;
        private String action;

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
    }
}
