package com.china.cibn.bangtvmobile.bangtv.module.user.vip;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2018/4/3.
 */

public class WXInfo {

    /**
     * data : {"appid":"wxfddc2d12b01ff339","partnerid":"1449004002","prepayid":null,"package":"Sign=WXPay","noncestr":"JmUiOGzJanFIINkdjMGDYoAgk6jKulpf","timestamp":1526004428,"sign":"D508F9EE684B3DC7573600D73DE022F4"}
     * msg : 预支付成功
     * code : 1
     */

    private DataBean data;
    private String msg;
    private int code;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * appid : wxfddc2d12b01ff339
         * partnerid : 1449004002
         * prepayid : null
         * package : Sign=WXPay
         * noncestr : JmUiOGzJanFIINkdjMGDYoAgk6jKulpf
         * timestamp : 1526004428
         * sign : D508F9EE684B3DC7573600D73DE022F4
         */

        private String appid;
        private String partnerid;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
