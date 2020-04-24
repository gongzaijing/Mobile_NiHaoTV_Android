package com.china.cibn.bangtvmobile.bangtv.entity.user;

import java.io.Serializable;

/**
 * @name Login
 * @class name：com..cloud.entity
 * @class describe
 * @anthor
 * @time 2017/7/19 12:37
 * @change
 * @chang time
 * @class describe
 */

public class WXUserInfo implements Serializable {

    /**
     * uid : 100291
     * uname : yi
     * pic : http://wx.qlogo.cn/mmopen/ELSbz5bUQ2FpCnkT4wKar4zjJwBjfnO4lib4m6rM6CMh8ZVCy4QmC5QFxDWBk2DknRfhfxQ3KdcJSicbqrbsvylGPJ2adBQHav/0
     * token : bd3d2f21340059823d1e55eaed590830
     */

    private String uid;
    private String uname;
    private String pic;
    private String token;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
