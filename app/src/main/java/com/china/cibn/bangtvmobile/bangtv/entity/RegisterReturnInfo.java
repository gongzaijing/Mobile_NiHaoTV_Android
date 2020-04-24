package com.china.cibn.bangtvmobile.bangtv.entity;

/**
 * Created by Administrator on 2018/3/29.
 */

public class RegisterReturnInfo {

    /**
     * data : {"user_name":"aaass@ccc.mm","email":"aaass@ccc.mm","user_id":119180,"nick":"","pic":"","reg":"2018-03-29","token":"931e610316aa37f4e4b07f55c0032ff3"}
     * msg : 注册成功
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
         * user_name : aaass@ccc.mm
         * email : aaass@ccc.mm
         * user_id : 119180
         * nick :
         * pic :
         * reg : 2018-03-29
         * token : 931e610316aa37f4e4b07f55c0032ff3
         */

        private String user_name;
        private String email;
        private int user_id;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
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
