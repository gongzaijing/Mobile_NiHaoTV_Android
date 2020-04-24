package com.china.cibn.bangtvmobile.bangtv.network;

import android.util.Log;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.bangtv.network.api.BiliAppService;
import com.china.cibn.bangtvmobile.bangtv.network.auxiliary.ApiConstants;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.utils.CommonUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.Utils;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gzj on 18/4/4 21:18
 * <p/>
 * Retrofit帮助类
 */
public class RetrofitHelper {

  private static OkHttpClient mOkHttpClient;


  static {
    initOkHttpClient();
  }



  public static BiliAppService getBiliAppAPI() {

    return createApi(BiliAppService.class, ApiConstants.APP_BASE_URL);
  }
  public static BiliAppService getLayourFileAPI() {

    return createApi(BiliAppService.class, ApiConstants.APP_LAYOUT_URL);
  }
  public static BiliAppService getBangTVAppAPI() {
    String url = null;

    if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV)) {

         url=  ApiConstants.APP_NIHAOTV_URL;

    }else if (AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){

         url=  ApiConstants.APP_NETRANGE_URL;

    }
    return createApi(BiliAppService.class,url);
  }

  public static BiliAppService getNoCacheAppAPI() {

    String url = null;

    if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV)) {

      url=  ApiConstants.APP_NIHAOTV_URL;

    }else if (AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){

      url=  ApiConstants.APP_NETRANGE_URL;

    }
    return createApi(BiliAppService.class,url);

  }

  public static BiliAppService getUserAPIBangtv() {

    return createApi(BiliAppService.class, ApiConstants.APP_USER_URL);
  }
  public static BiliAppService getCeshiAppAPI1() {

    return createApiNoCache(BiliAppService.class, ApiConstants.APP_NETRANGE_URL);
  }





  /**
   * 根据传入的baseUrl，和api创建retrofit  有缓存
   */
  private static <T> T createApi(Class<T> clazz, String baseUrl) {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(mOkHttpClient)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    return retrofit.create(clazz);
  }
  /**
   * 根据传入的baseUrl，和api创建retrofit  无缓存
   */
  private static <T> T createApiNoCache(Class<T> clazz, String baseUrl) {

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(genericClient())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    return retrofit.create(clazz);
  }
  public static OkHttpClient genericClient() {
      //日志显示级别
      HttpLoggingInterceptor.Level level= HttpLoggingInterceptor.Level.BODY;
      //新建log拦截器
      HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
          Log.d("OkHttp",message);
        }
      });
      loggingInterceptor.setLevel(level);

    OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
              @Override
              public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader( "token", AppGlobalVars.USER_TOKEN)
                        .addHeader( "agentVendorId", AppGlobalConsts.CHANNELS_ID)
                        .addHeader("versionId",BangtvApp.HeadersVersionCode+"")
                        .addHeader("mac", Utils.getEthernetMac())
                        .addHeader("uid",AppGlobalVars.USER_ID)
                        .addHeader("did",BangtvApp.did)
                        .addHeader("device","phone")
                        .build();
                return chain.proceed(request);
              }
            }).addInterceptor(loggingInterceptor)
            .build();

    return httpClient;
  }
  /**
   * 初始化OKHttpClient,设置缓存,设置超时时间,设置打印日志,设置UA拦截器
   */
  private static void initOkHttpClient() {

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    if (mOkHttpClient == null) {
      synchronized (RetrofitHelper.class) {
        if (mOkHttpClient == null) {
          //设置Http缓存
          Cache cache = new Cache(new File(BangtvApp.getInstance()
                  .getCacheDir(), "HttpCache"), 1024 * 1024 * 10);

          mOkHttpClient = new OkHttpClient.Builder()
                  .cache(cache)
                  .addInterceptor(interceptor)
                  .addNetworkInterceptor(new CacheInterceptor())
                  .addNetworkInterceptor(new StethoInterceptor())
                  .retryOnConnectionFailure(true)
                  .connectTimeout(30, TimeUnit.SECONDS)
                  .writeTimeout(20, TimeUnit.SECONDS)
                  .readTimeout(20, TimeUnit.SECONDS)
                  .addInterceptor(new UserAgentInterceptor())
                  .build();
        }
      }
    }
  }


  /**
   * 添加UA拦截器，B站请求API需要加上UA才能正常使用
   */
  private static class UserAgentInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

      Request originalRequest = chain.request();
      Request requestWithUserAgent = originalRequest.newBuilder()
              .removeHeader("token")
              .removeHeader("agentVendorId")
              .removeHeader("versionId")
              .removeHeader("mac")
              .removeHeader("uid")
              .removeHeader("did")
              .removeHeader("device")
              .addHeader( "token", AppGlobalVars.USER_TOKEN)
              .addHeader( "agentVendorId", AppGlobalConsts.CHANNELS_ID)
              .addHeader("versionId",BangtvApp.HeadersVersionCode+"")
              .addHeader("mac", Utils.getEthernetMac())
              .addHeader("uid",AppGlobalVars.USER_ID)
              .addHeader("did",BangtvApp.did)
              .addHeader("device","phone")
              .build();
      return chain.proceed(requestWithUserAgent);
    }
  }

  /**
   * 为okhttp添加缓存，这里是考虑到服务器不支持缓存时，从而让okhttp支持缓存
   */
  private static class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

      // 有网络时 设置缓存超时时间1个小时
      int maxAge = 60*30;
      // 无网络时，设置超时为1天
      int maxStale = 60*30;
      Request request = chain.request();
      if (CommonUtil.isNetworkAvailable(BangtvApp.getInstance())) {
        //有网络时只从网络获取
        request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_NETWORK)
                .build();
      } else {
        //无网络时只从缓存中读取
        request = request.newBuilder()
                .cacheControl(CacheControl.FORCE_CACHE)
                .build();
      }
      Response response = chain.proceed(request);
      if (CommonUtil.isNetworkAvailable(BangtvApp.getInstance())) {
        response = response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + maxAge)
                .build();
      } else {
        response = response.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                .build();
      }
      return response;
    }
  }
}
