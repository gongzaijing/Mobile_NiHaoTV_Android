package com.china.cibn.bangtvmobile.bangtv.entity;

/**
 * Created by Administrator on 2018/4/2.
 */

public class LoginReturnInfo {

    /**
     * data : {"user_name":"test0616@cri.cn","user_id":"104770","nick":"","pic":"","reg":"2017-06-16","token":"acd251295976777511dc137f95f60493"}
     * msg : 登录成功
     * code : 1
     */

    private DataBean data;
    private String msg;
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class DataBean {
        /**
         * user_name : test0616@cri.cn
         * user_id : 104770
         * nick :
         * pic :
         * reg : 2017-06-16
         * token : acd251295976777511dc137f95f60493
         */

        private String user_name;
        private String user_id;
        private String nick;
        private String pic;
        private String reg;
        private String token;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getReg() {
            return reg;
        }

        public void setReg(String reg) {
            this.reg = reg;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
