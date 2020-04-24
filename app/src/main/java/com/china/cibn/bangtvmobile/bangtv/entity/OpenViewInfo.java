package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by cibn on 2018/5/9.
 */

public class OpenViewInfo {

    /**
     * data : [{"name":"水平市场启动页运营","resUrl":"http://ag05.resource.bangtv.tv/boot/20180508/5af1527475c31.jpg","md5":"cb30e916cc2ac3a2dcd2c41dd2637043","nodeType":"epgstartpic","param":{"timespan":"5"}},{"name":"关于我们","resUrl":"","md5":"\u201cBangTV棒电视\u201d是一款专门针对海外华人、操作简单、功能强大、基础服务免费的互联网电视服务。\r\n全球直播频道，精品点播内容，告别一切不靠谱的APP。\r\n客服邮箱： bangtv_service@126.com\r\n官网： www.bangtv.tv","nodeType":"aboutus","param":{"timespan":""}}]
     * ip : 114.247.153.132
     */

    private String ip;
    private List<DataBean> data;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * name : 水平市场启动页运营
         * resUrl : http://ag05.resource.bangtv.tv/boot/20180508/5af1527475c31.jpg
         * md5 : cb30e916cc2ac3a2dcd2c41dd2637043
         * nodeType : epgstartpic
         * param : {"timespan":"5"}
         */

        private String name;
        private String resUrl;
        private String md5;
        private String nodeType;
        private ParamBean param;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResUrl() {
            return resUrl;
        }

        public void setResUrl(String resUrl) {
            this.resUrl = resUrl;
        }

        public String getMd5() {
            return md5;
        }

        public void setMd5(String md5) {
            this.md5 = md5;
        }

        public String getNodeType() {
            return nodeType;
        }

        public void setNodeType(String nodeType) {
            this.nodeType = nodeType;
        }

        public ParamBean getParam() {
            return param;
        }

        public void setParam(ParamBean param) {
            this.param = param;
        }

        public static class ParamBean {
            /**
             * timespan : 5
             */

            private String timespan;

            public String getTimespan() {
                return timespan;
            }

            public void setTimespan(String timespan) {
                this.timespan = timespan;
            }
        }
    }
}
