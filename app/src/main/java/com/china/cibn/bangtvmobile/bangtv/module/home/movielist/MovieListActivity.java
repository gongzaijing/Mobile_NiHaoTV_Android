package com.china.cibn.bangtvmobile.bangtv.module.home.movielist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.MovieListPagerAdapter;
import com.china.cibn.bangtvmobile.bangtv.entity.FilterListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.TabEntity;
import com.china.cibn.bangtvmobile.bangtv.entity.recommend.ProgramMenusInfo;
import com.china.cibn.bangtvmobile.bangtv.module.home.search.SearchListActivity;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;
import com.china.cibn.bangtvmobile.bangtv.widget.NoScrollViewPager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/4 21:18
 *
 * <p/>
 * 分区类型详情界面
 */
public class MovieListActivity extends RxBaseActivity {

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.view_pager)
  NoScrollViewPager mViewPager;

  @BindView(R.id.sliding_tabs)
  CommonTabLayout mCommonTab;

  @BindView(R.id.sliding_tabs2)
  CommonTabLayout mCommonTab2;

  @BindView(R.id.sliding_tabs3)
  CommonTabLayout mCommonTab3;

  @BindView(R.id.sliding_tabs4)
  CommonTabLayout mCommonTab4;

  @BindView(R.id.tvName)
  TextView mTvName;

  @BindView(R.id.search_view)
  MaterialSearchView mSearchView;

  @BindView(R.id.iv_isVisib)
  LinearLayout mIvVis;

  @BindView(R.id.layout_tab)
  LinearLayout mLayoutTab;


  private List<String> titles = new ArrayList<>();

  private String mId;

  public static  String mName;

  private List<ProgramMenusInfo.MenuListBean> programMenusInfosList= new ArrayList<>();

  private ProgramMenusInfo.ParentMenuBean programParentMenuBeanInfosList= new ProgramMenusInfo.ParentMenuBean();


  private ProgramMenusInfo programMenusInfos= new ProgramMenusInfo();

  private List<FilterListInfo.TimeBean> filtertimelist = new ArrayList<>();

  private List<FilterListInfo.TypeBean> fitertypelist = new ArrayList<>();

  private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

  private ArrayList<CustomTabEntity> mTabEntities1 = new ArrayList<>();

  private ArrayList<CustomTabEntity> mTabEntities2 = new ArrayList<>();

  private ArrayList<CustomTabEntity> mTabEntities3 = new ArrayList<>();

  private String[] mTitles = {"最新", "最热", "评分"};

  private MovieListPagerAdapter mAdapter;

  private String mParentCatgId;

  private int mPageNumber;

  private String mPageSize;

  private String mCateName;

  private String mYear;

  private  String mArea;

  private String mType;

  private  String mClassType;

  @Override
  public int getLayoutId() {

    return R.layout.activity_region_movielist_bangtv;

  }

  @Override
  public String setPageName() {
    return getClass().getSimpleName()+mName;
  }

  @Override
  public void initViews(Bundle savedInstanceState) {
    if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){
      mTitles = new String[]{"Newest", "Hottest", "Score"};
    }
    mViewPager.setNoScroll(true); //禁止手动滑动
    Bundle mBundle = getIntent().getExtras();
    if (mBundle != null) {
      mId = mBundle.getString(ConstantUtil.EXTRA_BANGTV_MOVIELIST_RID);
      mName = mBundle.getString(ConstantUtil.EXTRA_BANGTV_MOVIELIST_NAME);
    }

    mIvVis.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if(mLayoutTab.getVisibility()!=View.GONE) {
          mLayoutTab.setVisibility(View.GONE);
        }else{
          mLayoutTab.setVisibility(View.VISIBLE);
        }
        setTabreturn();
      }
      });
    initSearchView();
    getMovieList();
  }


  /**
   * 获取电影列表
   * */
  private void getMovieList(){
    RetrofitHelper.getBangTVAppAPI().getProgramMenus(BangtvApp.versionCodeUrl,mId)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              programMenusInfosList.addAll(resultBeans.getMenuList());
              programParentMenuBeanInfosList=resultBeans.getParentMenu();
              programMenusInfos=resultBeans;
              getFifterList();
            }, throwable -> {
              Log.i("","");
            });
  }

private void getFifterList(){
  /**
   * 获取筛选列表
   * */
  RetrofitHelper.getBangTVAppAPI().getFilterMenus(BangtvApp.versionCodeUrl,programMenusInfos.getParentMenu().getNodeType())
          .compose(bindToLifecycle())
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(filterListBeans -> {
            filtertimelist.addAll(filterListBeans.getTime());
            fitertypelist.addAll(filterListBeans.getType());
            initViewPager();
            finishTask();
          }, throwable -> {
            Log.i("","");
          });
}

  private void initViewPager() {
    getFilterMenu1();
    titles.add(getString(R.string.movielistactivity_all));
    Observable.from(programMenusInfosList)
            .compose(bindToLifecycle())
            .forEach(dataBean -> titles.add(dataBean.getName()));


    mParentCatgId=programParentMenuBeanInfosList.getParent_id();
    mPageNumber=1;
    mPageSize="80";
    mCateName=getString(R.string.movielistactivity_all);

    mAdapter = new MovieListPagerAdapter(
        getSupportFragmentManager(), mId, titles, programMenusInfosList,true
    ,mParentCatgId,mPageNumber,mPageSize,mCateName,mYear,mArea,mType,mClassType);
    mViewPager.setOffscreenPageLimit(titles.size());
    mViewPager.setAdapter(mAdapter);
    measureTabLayoutTextWidth(0);
    measureTabLayoutTextWidth2(0);
    measureTabLayoutTextWidth3(0);

  }


  @Override
  public void initToolBar() {

    mToolbar.setTitle("");
    mTvName.setText(mName);
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }
  private void initSearchView() {

    //初始化SearchBar
    mSearchView.setVoiceSearch(false);
    mSearchView.setCursorDrawable(R.drawable.custom_cursor);
    mSearchView.setEllipsize(true);
    mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
    mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(String query) {

        SearchListActivity.launch(MovieListActivity.this, query);
        return false;
      }



      @Override
      public boolean onQueryTextChange(String newText) {

        return false;
      }
    });
  }
  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == Activity.RESULT_OK) {
      ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      if (matches != null && matches.size() > 0) {
        String searchWrd = matches.get(0);
        if (!TextUtils.isEmpty(searchWrd)) {
          mSearchView.setQuery(searchWrd, false);
        }
      }

      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_region, menu);
    MenuItem item = menu.findItem(R.id.id_action_search);
    mSearchView.setMenuItem(item);
    return true;
  }

  //自定义下划线长度
  public void measureTabLayoutTextWidth(int position) {

    String titleName = mTabEntities1.get(position).getTabTitle();
    TextView titleView = mCommonTab.getTitleView(position);
    TextPaint paint = titleView.getPaint();
    float v = paint.measureText(titleName);
    mCommonTab.setIndicatorWidth(v / 3);

  }
  //自定义下划线长度
  public void measureTabLayoutTextWidth2(int position) {


    String titleName2 = mTabEntities.get(position).getTabTitle();
    TextView titleView2= mCommonTab2.getTitleView(position);
    TextPaint paint2= titleView2.getPaint();
    float v2 = paint2.measureText(titleName2);
    mCommonTab2.setIndicatorWidth(v2 / 3);

  }

  //自定义下划线长度
  public void measureTabLayoutTextWidth3(int position) {


    String titleName3 = mTabEntities2.get(position).getTabTitle();
    TextView titleView3= mCommonTab3.getTitleView(position);
    TextPaint paint3= titleView3.getPaint();
    float v3 = paint3.measureText(titleName3);
    mCommonTab3.setIndicatorWidth(v3 / 3);
  }


  public static void launch(Activity activity, String mId,String mName) {

    Intent mIntent = new Intent(activity, MovieListActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString(ConstantUtil.EXTRA_BANGTV_MOVIELIST_RID, mId);
    bundle.putString(ConstantUtil.EXTRA_BANGTV_MOVIELIST_NAME,mName);
    mIntent.putExtras(bundle);
    activity.startActivity(mIntent);
  }
/**
* 筛选列表
* */
private void getFilterMenu1(){
  mTabEntities1.add(0, new TabEntity(getString(R.string.movielistactivity_all)));
  for (int i = 0; i < programMenusInfosList.size(); i++) {
    mTabEntities1.add(new TabEntity(programMenusInfosList.get(i).getName()));
  }

  mTabEntities.add(0, new TabEntity(getString(R.string.movielistactivity_all)));
  for (int i = 0; i < filtertimelist.size(); i++) {
    mTabEntities.add(new TabEntity(filtertimelist.get(i).getTitle()));
  }
  mTabEntities2.add(0, new TabEntity(getString(R.string.movielistactivity_all)));

  for (int i = 0; i < fitertypelist.size(); i++) {
    mTabEntities2.add(new TabEntity(fitertypelist.get(i).getTitle()));
  }
  for (int i = 0; i < mTitles.length; i++) {
    mTabEntities3.add(new TabEntity(mTitles[i]));
  }
  mCommonTab.setTabData(mTabEntities1);

  mCommonTab2.setTabData(mTabEntities);

  mCommonTab3.setTabData(mTabEntities2);

  mCommonTab4.setTabData(mTabEntities3);
//大分类
  mCommonTab.setOnTabSelectListener(new OnTabSelectListener() {
    @Override
    public void onTabSelect(int position) {
      measureTabLayoutTextWidth(position);
      if(position==0){
          mParentCatgId=programParentMenuBeanInfosList.getParent_id();
          mCateName=getString(R.string.movielistactivity_all);

      }else{
          mCateName=programMenusInfosList.get(position-1).getName();
          mParentCatgId=programMenusInfosList.get(position-1).getId();
      }

      mPageNumber=1;
      mPageSize="80";
      mAdapter = new MovieListPagerAdapter(
              getSupportFragmentManager(), mId, titles, programMenusInfosList,true
      ,mParentCatgId,mPageNumber,mPageSize,mCateName,mYear,mArea,mType,mClassType);
      mViewPager.setOffscreenPageLimit(titles.size());
      mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onTabReselect(int position) {

    }
  });
  //年份筛选
  mCommonTab2.setOnTabSelectListener(new OnTabSelectListener() {
    @Override
    public void onTabSelect(int position) {
      measureTabLayoutTextWidth2(position);

      mParentCatgId=programParentMenuBeanInfosList.getParent_id();

      if(position!=0){
        mYear=filtertimelist.get(position-1).getValue();

      }else{
        mYear="-1";
      }

       if(TextUtils.isEmpty(mYear)){
         mYear="-1";
       }
       if(TextUtils.isEmpty(mArea)){
         mArea="-1";
       }
      if(TextUtils.isEmpty(mType)){
         mType="-1";
      }
      if(TextUtils.isEmpty(mClassType)){
        mClassType="1";
      }
      mAdapter = new MovieListPagerAdapter(
              getSupportFragmentManager(), mId, titles, programMenusInfosList,false
      , mParentCatgId,mPageNumber,mPageSize,mCateName,mYear,mArea,mType,mClassType);
      mViewPager.setOffscreenPageLimit(titles.size());
      mViewPager.setAdapter(mAdapter);


    }

    @Override
    public void onTabReselect(int position) {

    }
  });
  //类型筛选
  mCommonTab3.setOnTabSelectListener(new OnTabSelectListener() {
    @Override
    public void onTabSelect(int position) {
      measureTabLayoutTextWidth3(position);

      mParentCatgId=programParentMenuBeanInfosList.getParent_id();
      if(position!=0){
        mType = fitertypelist.get(position-1).getValue();

      }else{
        mType="-1";
      }
      if(TextUtils.isEmpty(mYear)){
        mYear="-1";
      }
      if(TextUtils.isEmpty(mArea)){
        mArea="-1";
      }
      if(TextUtils.isEmpty(mType)){
        mType="-1";
      }
      if(TextUtils.isEmpty(mClassType)){
        mClassType="1";
      }
      mAdapter = new MovieListPagerAdapter(
              getSupportFragmentManager(), mId, titles, programMenusInfosList,false
              ,mParentCatgId,mPageNumber,mPageSize,mCateName,mYear,mArea,mType,mClassType);
      mViewPager.setOffscreenPageLimit(titles.size());
      mViewPager.setAdapter(mAdapter);


    }

    @Override
    public void onTabReselect(int position) {

    }
  });
  //最新、最热
  mCommonTab4.setOnTabSelectListener(new OnTabSelectListener() {
    @Override
    public void onTabSelect(int position) {
      mParentCatgId=programParentMenuBeanInfosList.getParent_id();

      if(position==0){
        mClassType="1";
      }else if(position==1){
        mClassType="0";
      }else {
        mClassType="2";
      }
      if(TextUtils.isEmpty(mYear)){
        mYear="-1";
      }
      if(TextUtils.isEmpty(mArea)){
        mArea="-1";
      }
      if(TextUtils.isEmpty(mType)){
        mType="-1";
      }
      mAdapter = new MovieListPagerAdapter(
              getSupportFragmentManager(), mId, titles, programMenusInfosList,false
              ,mParentCatgId,mPageNumber,mPageSize,mCateName,mYear,mArea,mType,mClassType);
      mViewPager.setOffscreenPageLimit(titles.size());
      mViewPager.setAdapter(mAdapter);


    }

    @Override
    public void onTabReselect(int position) {

    }
  });

}
public void setTabreturn() {
  mCommonTab.setCurrentTab(0);
}

}
