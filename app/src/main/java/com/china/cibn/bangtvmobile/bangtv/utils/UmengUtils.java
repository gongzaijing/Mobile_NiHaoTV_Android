package com.china.cibn.bangtvmobile.bangtv.utils;

/**
 * Created by Administrator on 2018/4/9.
 */

import android.content.Context;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by SoBan on 2017/3/23.
 * Describe: 统计配置初始化
 */

public class UmengUtils {

    /**
     * 在Application中做的初始化
     */
    public static void initUmeng() {
        MobclickAgent.setDebugMode(true);//开启调试模式（如果不开启debug运行不会上传umeng统计）
        MobclickAgent.openActivityDurationTrack(false);
    }

    /**
     * 在BaseActivity跟BaseFragmentActivity中的onResume加入
     *
     * @param context
     */
    public static void onResumeToActivity(Context context,String mPageName) {
        MobclickAgent.onPageStart(mPageName);
        MobclickAgent.onResume(context);
    }

    /**
     * 在BaseActivity跟BaseFragmentActivity中的onPause加入
     *
     * @param context
     */
    public static void onPauseToActivity(Context context,String mPageName) {
        MobclickAgent.onPageEnd(mPageName);
        MobclickAgent.onPause(context);
    }

    /**
     * 在BaseFragment中的onResume加入
     *
     * @param mPageName
     */
    public static void onResumeToFragment(String mPageName) {
        MobclickAgent.onPageStart(mPageName);
    }

    /**
     * 在BaseFragment中的onPause加入
     *
     * @param mPageName
     */
    public static void onPauseToFragment(String mPageName) {
        MobclickAgent.onPageEnd(mPageName);
    }

    /**
     * 在登录成功的地方调用
     *
     * @param userId 用户id
     */
    public static void onLogin(String userId) {
        MobclickAgent.onProfileSignIn(userId);
    }

    /**
     * 在退出登录的地方调用
     */
    public static void onLogout() {
        MobclickAgent.onProfileSignOff();
    }
}