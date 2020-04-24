package com.china.cibn.bangtvmobile.bangtv.module.home.classify;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.HomePageBangTVAdapter;
import com.china.cibn.bangtvmobile.bangtv.base.RxLazyFragment;
import com.china.cibn.bangtvmobile.bangtv.entity.recommend.LayoutFileInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.recommend.RecommendBannerInfoBangTv;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.widget.CustomEmptyView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
* BangTv分类页面
* */
public class HomePageFragmentBangTv extends RxLazyFragment {

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout mSwipeRefreshLayout;

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;

  @BindView(R.id.empty_layout)
  CustomEmptyView mCustomEmptyView;
  private HomePageBangTVAdapter mSectionedAdapter;

  private List<RecommendBannerInfoBangTv.ItemListBean> recomendBannersList = new ArrayList<>();

  private RecommendBannerInfoBangTv recommendBanners = new RecommendBannerInfoBangTv();

  private List<LayoutFileInfo.LayoutBean> layoutBeans = new ArrayList<>();

  private boolean mIsRefreshing = false;

  int[] mList;

  private int _blc_pics;

  public static String menuId;

  public static HomePageFragmentBangTv newInstance(String id) {
    menuId= id;

    return new HomePageFragmentBangTv();
  }


  @Override
  public int getLayoutResId() {

    return R.layout.fragment_home_recommended_bangtv;
  }


  @Override
  public void finishCreateView(Bundle state) {
    this.clearData();
    isPrepared = true;
    lazyLoad();
  }


  @Override
  protected void lazyLoad() {

    if (!isPrepared || !isVisible) {
      return;
    }

    initRefreshLayout();
    initRecyclerView();
    isPrepared = false;
  }


  @Override
  protected void initRecyclerView() {
    mSectionedAdapter = new HomePageBangTVAdapter(getActivity());
    mRecyclerView.setAdapter(mSectionedAdapter);
    GridLayoutManager layout = new GridLayoutManager(getActivity(), 6);
    layout.setOrientation(LinearLayoutManager.VERTICAL);
    layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

      @Override
      public int getSpanSize(int position) {

        return mSectionedAdapter.getSpanSize(position);
      }
    });

    mRecyclerView.setLayoutManager(layout);
    setRecycleNoScroll();

  }

  @Override
  protected void initRefreshLayout() {

    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    mSwipeRefreshLayout.setOnRefreshListener(this::loadData);

    mSwipeRefreshLayout.post(() -> {

      mSwipeRefreshLayout.setRefreshing(true);
      mIsRefreshing = true;
      loadData();
    });
    mSwipeRefreshLayout.setOnRefreshListener(() -> {
      clearData();
      loadData();
    });

  }
  @Override
  protected void loadData() {

    RetrofitHelper.getBangTVAppAPI().getBannerInfo(BangtvApp.versionCodeUrl,menuId)
        .compose(bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(resultBeans -> {
          recomendBannersList=resultBeans.getItemList();
          recommendBanners=resultBeans;
          getFifter();
        }, throwable -> {
          initEmptyView();
        });

  }
  /**
   * 获取布局 json
   * */
  private void getFifter(){
    RetrofitHelper.getLayourFileAPI().getLayoutFileInfo(recommendBanners.getLayoutFile())
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans1 -> {
              layoutBeans = resultBeans1.getLayout();
              finishTask();
            }, throwable -> {
               initEmptyView();
            });

  }
  @Override
  protected void finishTask() {

    mSwipeRefreshLayout.setRefreshing(false);
    mIsRefreshing = false;
    hideEmptyView();
    mList = new int[recomendBannersList.size()];
    _blc_pics=0;
    for(int j=0;j<layoutBeans.size();j++){
      if(layoutBeans.get(j).getS()==21){
        mList[_blc_pics++] = 1;
        mList[_blc_pics++] = 1;
      }else if(layoutBeans.get(j).getS()==11){
        mList[_blc_pics++] = 1;
      }else if(layoutBeans.get(j).getS()==31){
        mList[_blc_pics++] = 1;
        mList[_blc_pics++] = 2;
        mList[_blc_pics++] = 2;
      }else if(layoutBeans.get(j).getS()==32){
        mList[_blc_pics++] = 2;
        mList[_blc_pics++] = 2;
        mList[_blc_pics++] = 1;
      }else{
        return;
      }
    }
    mSectionedAdapter.setLiveInfo(recomendBannersList,mList,false,"page");

   mSectionedAdapter.notifyDataSetChanged();

  }
  public void initEmptyView() {

    mSwipeRefreshLayout.setRefreshing(false);
    mCustomEmptyView.setVisibility(View.VISIBLE);
    mRecyclerView.setVisibility(View.GONE);
//    mCustomEmptyView.setEmptyImage(R.drawable.img_tips_error_load_error);
    mCustomEmptyView.setEmptyText(getString(R.string.error_text2));
  }
  public void hideEmptyView() {

    mCustomEmptyView.setVisibility(View.GONE);
    mRecyclerView.setVisibility(View.VISIBLE);
  }
  private void clearData() {
    recomendBannersList.clear();
    layoutBeans.clear();
    mIsRefreshing = true;
  }
  private void setRecycleNoScroll() {

    mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
  }
}
