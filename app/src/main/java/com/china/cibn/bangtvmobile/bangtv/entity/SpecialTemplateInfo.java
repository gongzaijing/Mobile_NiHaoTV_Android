package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/27.
 */

public class SpecialTemplateInfo {

    /**
     * specialId : 24
     * title : 2018元宵喜乐会
     * slogan :
     * imgUrl1 : http://resource.bangtv.tv/special/20180302/5a9a15137ee04.jpg
     * imgUrl2 :
     * introduction :
     * specialTemplateName : 1
     * sections : {"specialRecommends":{"recommends":[{"id":"v-1291","title":"2018湖南卫视元宵喜乐会","imgUrl":"http://resource.bangtv.tv/poster/20180303/5a9a012866b52.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1292","title":"2018江苏卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20180303/5a9a05e3bd541.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1294","title":"2018年北京电视台元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20180305/5a9cb99e7fc9b.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1293","title":"2018安徽卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20180303/5a99ff49d5e3b.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1060","title":"2017东方卫视元宵喜乐会","imgUrl":"http://resource.bangtv.tv/poster/20170213/58a1492614594.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1061","title":"2017年江苏卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20170213/58a14c74618b3.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1056","title":"2017北京卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20170210/589d80f50ee29.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1062","title":"2017年辽宁卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20170210/589d5e5354d4b.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1057","title":"2017安徽卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20170210/589d844218c57.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"}]}}
     */

    private String specialId;
    private String title;
    private String slogan;
    private String imgUrl1;
    private String imgUrl2;
    private String introduction;
    private String specialTemplateName;
    private SectionsBean sections;

    public String getSpecialId() {
        return specialId;
    }

    public void setSpecialId(String specialId) {
        this.specialId = specialId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getSpecialTemplateName() {
        return specialTemplateName;
    }

    public void setSpecialTemplateName(String specialTemplateName) {
        this.specialTemplateName = specialTemplateName;
    }

    public SectionsBean getSections() {
        return sections;
    }

    public void setSections(SectionsBean sections) {
        this.sections = sections;
    }

    public static class SectionsBean {
        /**
         * specialRecommends : {"recommends":[{"id":"v-1291","title":"2018湖南卫视元宵喜乐会","imgUrl":"http://resource.bangtv.tv/poster/20180303/5a9a012866b52.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1292","title":"2018江苏卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20180303/5a9a05e3bd541.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1294","title":"2018年北京电视台元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20180305/5a9cb99e7fc9b.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1293","title":"2018安徽卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20180303/5a99ff49d5e3b.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1060","title":"2017东方卫视元宵喜乐会","imgUrl":"http://resource.bangtv.tv/poster/20170213/58a1492614594.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1061","title":"2017年江苏卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20170213/58a14c74618b3.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1056","title":"2017北京卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20170210/589d80f50ee29.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1062","title":"2017年辽宁卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20170210/589d5e5354d4b.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"},{"id":"v-1057","title":"2017安徽卫视元宵晚会","imgUrl":"http://resource.bangtv.tv/poster/20170210/589d844218c57.tv.jpg","action":"OPEN_DETAIL","cornerPrice":"0"}]}
         */

        private SpecialRecommendsBean specialRecommends;

        public SpecialRecommendsBean getSpecialRecommends() {
            return specialRecommends;
        }

        public void setSpecialRecommends(SpecialRecommendsBean specialRecommends) {
            this.specialRecommends = specialRecommends;
        }

        public static class SpecialRecommendsBean {
            private List<RecommendsBean> recommends;

            public List<RecommendsBean> getRecommends() {
                return recommends;
            }

            public void setRecommends(List<RecommendsBean> recommends) {
                this.recommends = recommends;
            }

            public static class RecommendsBean {
                /**
                 * id : v-1291
                 * title : 2018湖南卫视元宵喜乐会
                 * imgUrl : http://resource.bangtv.tv/poster/20180303/5a9a012866b52.tv.jpg
                 * action : OPEN_DETAIL
                 * cornerPrice : 0
                 */

                private String id;
                private String title;
                private String imgUrl;
                private String action;
                private String cornerPrice;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String getImgUrl() {
                    return imgUrl;
                }

                public void setImgUrl(String imgUrl) {
                    this.imgUrl = imgUrl;
                }

                public String getAction() {
                    return action;
                }

                public void setAction(String action) {
                    this.action = action;
                }

                public String getCornerPrice() {
                    return cornerPrice;
                }

                public void setCornerPrice(String cornerPrice) {
                    this.cornerPrice = cornerPrice;
                }
            }
        }
    }
}
