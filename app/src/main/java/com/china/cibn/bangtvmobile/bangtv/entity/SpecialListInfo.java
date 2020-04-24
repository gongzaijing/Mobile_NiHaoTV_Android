package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 *
 * 专题汇总
 */

public class SpecialListInfo {

    private List<SpecialListBean> specialList;

    public List<SpecialListBean> getSpecialList() {
        return specialList;
    }

    public void setSpecialList(List<SpecialListBean> specialList) {
        this.specialList = specialList;
    }

    public static class SpecialListBean {
        /**
         * id : 24
         * name : 2018元宵喜乐会
         * image : http://resource.bangtv.tv/special/20180302/5a9a151365d44.jpg
         */

        private String id;
        private String name;
        private String image;

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
    }
}
