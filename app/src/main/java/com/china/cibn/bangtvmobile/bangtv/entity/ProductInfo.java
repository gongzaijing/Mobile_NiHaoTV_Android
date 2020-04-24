package com.china.cibn.bangtvmobile.bangtv.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/4/4.
 */

public class ProductInfo {

    /**
     * data : [{"pid":"1027","pname":"OTT月包0904","price":"8.99","priceC":"0.00","priceY":"美元/月","isRecommend":"1","rate":"6.90","duration":"30","propic":"","discount":""},{"pid":"1025","pname":"OTT季包","price":"24.27","priceC":"26.97","priceY":"美元/季度","isRecommend":"1","rate":"6.90","duration":"91","propic":"http://resource.bangtv.tv/product/propic/20170721/5971ce61516cb.png","discount":""},{"pid":"1029","pname":"OTT年包109","price":"75.52","priceC":"107.88","priceY":"美元/年","isRecommend":"1","rate":"6.90","duration":"365","propic":"http://resource.bangtv.tv/product/propic/20171008/59dadd1762b1d.png","discount":""}]
     * modify : 1522810739
     * msg : 产品列表获取成功
     * code : 1
     */

    private int modify;
    private String msg;
    private String code;
    private List<DataBean> data;

    public int getModify() {
        return modify;
    }

    public void setModify(int modify) {
        this.modify = modify;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * pid : 1027
         * pname : OTT月包0904
         * price : 8.99
         * priceC : 0.00
         * priceY : 美元/月
         * isRecommend : 1
         * rate : 6.90
         * duration : 30
         * propic :
         * discount :
         */

        private String pid;
        private String pname;
        private String price;
        private String priceC;
        private String priceY;
        private String isRecommend;
        private String rate;
        private String duration;
        private String propic;
        private String discount;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPriceC() {
            return priceC;
        }

        public void setPriceC(String priceC) {
            this.priceC = priceC;
        }

        public String getPriceY() {
            return priceY;
        }

        public void setPriceY(String priceY) {
            this.priceY = priceY;
        }

        public String getIsRecommend() {
            return isRecommend;
        }

        public void setIsRecommend(String isRecommend) {
            this.isRecommend = isRecommend;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getPropic() {
            return propic;
        }

        public void setPropic(String propic) {
            this.propic = propic;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }
    }
}
