package com.china.cibn.bangtvmobile;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.china.cibn.bangtvmobile.bangtv.dal.LocalData;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.SharedPreferencesUtils;
import com.china.cibn.bangtvmobile.bangtv.network.Config;
import com.china.cibn.bangtvmobile.bangtv.utils.ThemeHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.UmengUtils;
import com.china.cibn.bangtvmobile.bangtv.utils.Utils;
import com.facebook.stetho.Stetho;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


/**
 * @author gzj
 * @date 2018年3月1日10:38:25
 */
public class BangtvApp extends Application implements ThemeUtils.switchColor {

  public static BangtvApp mInstance;

  private static SharedPreferencesUtils shared;
  private static IWXAPI api;
 public static String did="";
  //fix by gz 2018/05/09 begin 极光推送
  public static String jid = ""; //推送id
  //fix by gz 2018/05/09 end   极光推送
    public static int versionCode=0;

    public static int HeadersVersionCode;
  public static String versionCodeUrl="";

  public static String secretkey = "tvbang"; //用户约定

  public static int ScreenWidth = 1920;
  public static int ScreenHeight = 1080;

  public static SharedPreferencesUtils getShared() {
    return shared;
  }
  @Override
  public void onCreate() {

    super.onCreate();
    shared = new SharedPreferencesUtils(this,"Login");
    mInstance = this;
//    initData();
    UmengUtils.initUmeng();
    LocalData.initLocalData(getApplicationContext());
    UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null);
    init();
//    if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)) {
//      set("en");
//    }

    //fix by gz 2018/05/09 begin 极光推送
    initJpush();
    //fix by gz 2018/05/09 end   极光推送
  }

//设置当前App的语言
  private void set(String lauType) {
    Locale myLocale = new Locale(lauType);
    Resources res = getResources();
    DisplayMetrics dm = res.getDisplayMetrics();
    Configuration conf = res.getConfiguration();
    conf.locale = myLocale;
    res.updateConfiguration(conf, dm);
  }

  private void init() {
    // 初始化主题切换
    ThemeUtils.setSwitchColor(this);
    //初始化Leak内存泄露检测工具
    //LeakCanary.install(this);
    //初始化Stetho调试工具
    Stetho.initialize(
        Stetho.newInitializerBuilder(this)
            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
            .build());
  }

  //fix by gz 2018/05/09 begin 极光推送
  private void initJpush() {
    JPushInterface.setDebugMode(true);  //发布前改为false
    JPushInterface.init(this);
    if (null != JPushInterface.getRegistrationID(this)) {
      jid = JPushInterface.getRegistrationID(this);
      System.out.println("JPshID = " + jid);
    }
    Set<String> sets = new HashSet<>();
    sets.add(AppGlobalConsts.CHANNELS_ID);
    JPushInterface.setTags(this, sets, new TagAliasCallback() {
      @Override
      public void gotResult(int i, String s, Set<String> set) {
        Log.d("alias", "set tag result is" + i);
      }
    });
  }
  //fix by gz 2018/05/09 end   极光推送


public void initData(){
    try {
      HeadersVersionCode = getPackageManager().getPackageInfo(
                getApplicationContext().getPackageName(),
                PackageManager.GET_CONFIGURATIONS). versionCode;
    } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
    }
    did = Utils.currentDeviceMark(this);
    initRes();
}
private void initRes(){

    versionCode = HeadersVersionCode/100;
    versionCodeUrl = "v"+versionCode;
}

  public static BangtvApp getInstance() {

    return mInstance;
  }

  /**
   * 判断微信客户端是否存在
   *
   * @return true安装, false未安装
   */
  public static boolean isWeChatAppInstalled(Context context) {


    api = WXAPIFactory.createWXAPI(context, Config.APP_ID_WX);
    if(api.isWXAppInstalled() && api.isWXAppSupportAPI()) {
      return true;
    } else {
      final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
      List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
      if (pinfo != null) {
        for (int i = 0; i < pinfo.size(); i++) {
          String pn = pinfo.get(i).packageName;
          if (pn.equalsIgnoreCase("com.tencent.mm")) {
            return true;
          }
        }
      }
      return false;
    }
  }

  @Override
  public int replaceColorById(Context context, @ColorRes int colorId) {

    if (ThemeHelper.isDefaultTheme(context)) {
      return context.getResources().getColor(colorId);
    }
    String theme = getTheme(context);
    if (theme != null) {
      colorId = getThemeColorId(context, colorId, theme);
    }
    return context.getResources().getColor(colorId);
  }


  @Override
  public int replaceColor(Context context, @ColorInt int color) {

    if (ThemeHelper.isDefaultTheme(context)) {
      return color;
    }
    String theme = getTheme(context);
    int colorId = -1;

    if (theme != null) {
      colorId = getThemeColor(context, color, theme);
    }
    return colorId != -1 ? getResources().getColor(colorId) : color;
  }


  private String getTheme(Context context) {

    if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_STORM) {
      return "blue";
    } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_HOPE) {
      return "purple";
    } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_WOOD) {
      return "green";
    } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_LIGHT) {
      return "green_light";
    } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_THUNDER) {
      return "yellow";
    } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_SAND) {
      return "orange";
    } else if (ThemeHelper.getTheme(context) == ThemeHelper.CARD_FIREY) {
      return "red";
    }
    return null;
  }


  private
  @ColorRes
  int getThemeColorId(Context context, int colorId, String theme) {

    switch (colorId) {
      case R.color.theme_color_primary:
        return context.getResources().getIdentifier(theme, "color", getPackageName());
      case R.color.theme_color_primary_dark:
        return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
      case R.color.theme_color_primary_trans:
        return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
    }
    return colorId;
  }


  private
  @ColorRes
  int getThemeColor(Context context, int color, String theme) {

    switch (color) {
      case 0xfffb7299:
        return context.getResources().getIdentifier(theme, "color", getPackageName());
      case 0xffb85671:
        return context.getResources().getIdentifier(theme + "_dark", "color", getPackageName());
      case 0x99f0486c:
        return context.getResources().getIdentifier(theme + "_trans", "color", getPackageName());
    }
    return -1;
  }

}
