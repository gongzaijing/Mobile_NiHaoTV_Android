package com.china.cibn.bangtvmobile.bangtv.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.text.TextUtils;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.dal.LocalData;
import com.china.cibn.bangtvmobile.bangtv.dal.UserHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/3/22.
 * 全局常量
 */

public class AppGlobalVars {

    /**
     * 当前用户token
     */
    public static  String USER_TOKEN = "";

    /**
     * 默认用户token
     **/
    public static String DEFAULT_TOKEN = "";


    /**
     * 当前用户ID
     **/
    public static String USER_ID = "";

    /**
     * 默认用户ID
     **/
    public static String DEFAULT_ID = "";



    /**
     * 当前用户头像
     */
    public static String USER_PIC = "";

    /**
     * 当前用户名(微信昵称)
     */
    public static String USER_NICK_NAME = "";

    /**
     * 用户ip
     */
    public static String IP_ADRESS = "";

    /**
     * 有线网卡MAC
     **/
    public static String LAN_MAC = "";
    /**
     * 电影试看时长
     **/
    public  static  int ISTRY_TIME;

}
