package com.china.cibn.bangtvmobile.bangtv.entity.radio;

import java.util.List;

/**
 * Created by Administrator on 2018/4/2.
 */

public class RadioDetailsInfo {

    private List<?> parentMenu;
    private List<ProgramListBean> programList;

    public List<?> getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(List<?> parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<ProgramListBean> getProgramList() {
        return programList;
    }

    public void setProgramList(List<ProgramListBean> programList) {
        this.programList = programList;
    }

    public static class ProgramListBean {
        /**
         * id : r-887
         * name : hit FM
         * image : http://resource.bangtv.tv/ott/88.7.png
         * isNews : false
         * cornerPrice : 0
         * createDate :
         * cornerType : 0
         * currentNum : 0
         */

        private String id;
        private String name;
        private String image;
        private String isNews;
        private String cornerPrice;
        private String createDate;
        private String cornerType;
        private String currentNum;

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

        public String getIsNews() {
            return isNews;
        }

        public void setIsNews(String isNews) {
            this.isNews = isNews;
        }

        public String getCornerPrice() {
            return cornerPrice;
        }

        public void setCornerPrice(String cornerPrice) {
            this.cornerPrice = cornerPrice;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getCornerType() {
            return cornerType;
        }

        public void setCornerType(String cornerType) {
            this.cornerType = cornerType;
        }

        public String getCurrentNum() {
            return currentNum;
        }

        public void setCurrentNum(String currentNum) {
            this.currentNum = currentNum;
        }
    }
}
