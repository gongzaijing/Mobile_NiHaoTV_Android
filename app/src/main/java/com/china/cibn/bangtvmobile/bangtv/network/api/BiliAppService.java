package com.china.cibn.bangtvmobile.bangtv.network.api;

import com.china.cibn.bangtvmobile.bangtv.entity.AuthenticationInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.CheckUpdateInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.FilterListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.GoOutInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.LoginReturnInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.MenuListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.MovieDetailsInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.MovieListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.OpenViewInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.ProductInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.RankingListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.RegisterReturnInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.search.SearchListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.SpecialListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.SpecialTemplateInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.VideoUrlInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.VipPayInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.radio.RadioDetailsInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.radio.RadioListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.radio.RadioUrlInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.radio.WxLoginInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.recommend.LayoutFileInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.recommend.ProgramMenusInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.recommend.RecommendBannerInfoBangTv;
import com.china.cibn.bangtvmobile.bangtv.module.user.vip.WXInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.LiveListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.LivePlayURlInfo;
import java.util.List;
import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by hcc on 16/8/4 12:03
 *
 */
public interface BiliAppService {

  /**
   * 开机认证BangTV
   */
  @GET("{versionCode}/authentication/index")
  Observable<AuthenticationInfo> getAuthen(@Path("versionCode") String versionCode);


  /**
   * 升级检测BangTV
   */
  @GET("{versionCode}/upgrade/index")
  Observable<CheckUpdateInfo> getCheckUpdate(@Path("versionCode") String versionCode);

  /**
   * 获得开机信息(开机画面，版本信息)
   **/
  @GET("{versionCode}/openview/index")
  Observable<OpenViewInfo> getBootimg(@Path("versionCode") String versionCode);

  /**
   * 首页推荐banner  BangTV
   */
  @GET("{versionCode}/index/recommendlist")
  Observable<RecommendBannerInfoBangTv> getBannerInfo(@Path("versionCode") String versionCode, @Query("menuId") String adid);

  /**
   * 首页上方列表BangTV
   */
  @GET("{versionCode}/index/menulist")
  Observable<MenuListInfo> getMenuList(@Path("versionCode") String versionCode);

  /**
   * 首页布局BangTV
   */
  @GET
  Observable<LayoutFileInfo> getLayoutFileInfo(@Url String url);

  /**
   * 电影列表BangTV
   */
  @GET("{versionCode}/program/menulist")
  Observable<ProgramMenusInfo> getProgramMenus(@Path("versionCode") String versionCode, @Query("parentCatgId") String rid);

  /**
   * 电影筛选列表BangTV
   */
  @GET("{versionCode}/program/filterclass")
  Observable<FilterListInfo> getFilterMenus(@Path("versionCode") String versionCode, @Query("type") String type);

  /**
   * 电影列表筛选数据BangTV
   */
  @GET("{versionCode}/program/filterlist")
  Observable<MovieListInfo> getFiltersDetails(@Path("versionCode") String versionCode,
                                              @Query("parentCatgId") String mParentCatgId,
                                              @Query("year") String mYear,
                                              @Query("area") String mArea,
                                              @Query("type") String mType,
                                              @Query("classType") String mClassType,
                                              @Query("pageNumber") int mPageNumber,
                                              @Query("pageSize") String mPageSize
  );

  /**
   * 电影列表海报数据BangTV
   */
  @GET("{versionCode}/program/movielist")
  Observable<MovieListInfo> getProgramMenusDetails(@Path("versionCode") String versionCode,
                                                   @Query("parentCatgId") String mParentCatgId,
                                                   @Query("pageNumber") int mPageNumber,
                                                   @Query("pageSize") String mPageSize,
                                                   @Query("cateName") String mCateName

  );

  /**
   * 电影详情页数据BangTV
   */
  @GET("{versionCode}/detail/moviedetail")
  Observable<MovieDetailsInfo> getMovieDetails(@Path("versionCode") String versionCode,
                                               @Query("programSeriesId") String mId);

  /**
   * 排行榜列表海报BangTV
   */
  @GET("{versionCode}/rank/list")
  Observable<RankingListInfo> getRankingList(@Path("versionCode") String versionCode,
                                             @Query("classtype") String classtype);

  /**
   * 专题汇总BangTV
   */
  @GET("{versionCode}/special/list")
  Observable<SpecialListInfo> getSpecialList(@Path("versionCode") String versionCode,
                                             @Query("peciallistid") String peciallistid);


  /**
   * 专题模板BangTV
   */
  @GET("{versionCode}/special/detail")
  Observable<SpecialTemplateInfo> getSpecialTemplate(
          @Path("versionCode") String versionCode,
          @Query("specialid") String specialid);

  /**
   * 搜索BangTV
   */
  @GET("{versionCode}/search/programlist")
  Observable<SearchListInfo> getSearchList(
          @Path("versionCode") String versionCode,
          @Query("searchType") String searchType,
          @Query("searchValue") String searchValue,
          @Query("pageNumber") String pageNumber,
          @Query("pageSize") String pageSize);

  /**
   * 邮箱注册BangTV
   */
  @FormUrlEncoded
  @POST("{versionCode}/user/register")
  Observable<RegisterReturnInfo> startVipEMRegister(@Path("versionCode") String versionCode,
                                                    @FieldMap Map<String, String> map);

  /**
   * 邮箱登录BangTV
   */
  @FormUrlEncoded
  @POST("{versionCode}/user/loginnew")
  Observable<LoginReturnInfo> startVipEmLogin(@Path("versionCode") String versionCode,
                                              @FieldMap Map<String, String> map);

  /**
   * wx支付
   */
  @POST("pay/app/appPay.php")
  Observable<WXInfo> startWX(@Query("item_number") String pid);


  /**
   * 注销登录BangTV
   */
  @GET("ott/logoutnew/")
  Observable<GoOutInfo> getGoOut();


  /**
   * 微信登录获取信息BangTV
   */
  @FormUrlEncoded
  @POST("mobile/mobile-login")
  Observable<WxLoginInfo> getLoginInfo(@FieldMap Map<String, String> map);

  /**
   * 获取电影播放Url
   */
  @GET("{versionCode}/vod/play")
  Observable<VideoUrlInfo> getVideoUrl(
          @Path("versionCode") String versionCode,
          @Query("pid") String mId,
          @Query("playType") String mPlayType,
          @Query("cdnType") String mCdnType
  );

  /**
   * 获取直播频道
   */
  @GET("{versionCode}/live/channellist")
  Observable<List<LiveListInfo>> getLiveList(
          @Path("versionCode") String versionCode);

  /**
   * 获取直播播放URL
   */
  @GET("{versionCode}/live/play")
  Observable<LivePlayURlInfo> getLiveUrl(
          @Path("versionCode") String versionCode
          , @Query("lid") String mPlayId,
          @Query("playType") String mPlayType,
          @Query("cdnType") String mCdnType);

  /**
   * 获取VIP产品信息
   */
  @GET("{versionCode}/user/getproduct")
  Observable<ProductInfo> getProductList(@Path("versionCode") String versionCode);

  /**
   * 获取V会员信息
   */
  @GET("{versionCode}/user/payinfo")
  Observable<VipPayInfo> getPayInfo(@Path("versionCode") String versionCode);

  /**
   * 电台列表BangTV
   */
  @GET("{versionCode}/radio/category")
  Observable<List<RadioListInfo>> getRadioList(@Path("versionCode") String versionCode);

  /**
   * 电台海报页BangTV
   */
  @GET("{versionCode}/radio/list")
  Observable<RadioDetailsInfo> getRadioDetails(@Path("versionCode") String versionCode, @Query("cate") String mCate);

  /**
   * 电台播放Url BangTV
   */
  @GET("{versionCode}/radio/play")
  Observable<RadioUrlInfo> getRadioUrl(@Path("versionCode") String versionCode,
                                       @Query("pid") String mPid);
  /*---------------------------------------------------*/


}