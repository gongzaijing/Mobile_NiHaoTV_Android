package com.china.cibn.bangtvmobile.bangtv.utils;

/**
 * Created by Administrator on 2018/3/22.
 */

public class AppGlobalConsts {

    /**
     * 渠道列表
     **/
    public static final String CHANNEL_NIHAOTV = "mobilebangtv"; //棒tv
    public static final String CHANNEL_NIHAOTV_NETRANGE_EN = "mobilenetrange"; //棒tv

    /**
     * 渠道ID
     **/
    public static String CHANNELS_ID =CHANNEL_NIHAOTV;
    /**
     * 本地持久化配置项名称，包括：背景图片ID、升级就绪标志、当前用户、默认用户等 <br />
     * 应用在LocalData类，举例：<br />
     * setKV(AppGlobalConsts.PERSIST_NAMES.CURRENT_USER.name(), "123"); <br />
     * getKV(AppGlobalConsts.PERSIST_NAMES.CURRENT_USER.name());
     **/


    /**
     * 本地消息内容属性名 <br />
     * 通常用于推消息的转发<br />
     * intent.putExtra(AppGlobalConsts.INTENT_MSG_PARAM, "{...}");
     **/
    public static final String INTENT_MSG_PARAM = "msgParam";



    public static enum PERSIST_NAMES {

        /**
         * 当前登录用户(user_id)weixin
         **/
        CURRENT_USER,
        /**
         * 初始化默认用户(user_id)，来自设备登录接口
         **/
        DEFAULT_USER,

        /**
         * 升级就绪，1：就绪；0：未就绪
         **/
        READY_FOR_UPDATE,
        /**
         * 即将更新版本号
         **/
        UPDATE_VERSION_CODE,
        /**
         * 强制升级，1：强制；0：不强制
         **/
        FORCE_UPDATE,

    }
    /**
     * 本地消息过滤字符串
     **/
    public static enum LOCAL_MSG_FILTER {



        /**
         * 通知显示
         **/
        NOTICE_DISPLAY,

        /**
         * EPG版本更新
         **/
        EPG_UPDATE,

        /**
         * EPG启动画面
         **/
        EPG_BOOT_IMAGE_UPDATE,

        /**
         * EPG界面刷新
         **/
        EPG_REFRESH,

        /**
         * 写日志
         **/
        LOG_WRITE,

        /**
         * 网络状态改变
         **/
        NET_CHANGED,

        /**
         * 支付结果
         **/
        PAY_RESULT,

        /**
         * 用户绑定
         **/
        USER_BIND,

        /**
         * 回看支付成功
         **/
        REPLAYPAY_SUCCESS,

        /**
         * 时间更新
         **/
        DATETIME_UPDATE,

        /**
         * 列表筛选
         */
        LIST_FILTER,

        /**
         * 下载服务
         */
        DOWNLOAD_SERVICE,

        /**
         * 背景更换
         */
        BACKGROUND_CHANGE,

        /**
         * 用户头像更换
         */
        USER_IMAGE_CHANGE,

        /**
         * 有新消息送达
         **/
        NEW_MSG_ARRIVED,
        /**
         * 强制下载服务
         */
        FORCE_DOWNLOAD_SERVICE
    }









}
