package com.china.cibn.bangtvmobile.bangtv.module.home.recommend;

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
* BangTv推荐主页
* */
public class HomeRecommendedFragmentBangTv extends RxLazyFragment {

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout mSwipeRefreshLayout;

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;

  @BindView(R.id.empty_layout)
  CustomEmptyView mCustomEmptyView;
  private HomePageBangTVAdapter mSectionedRecyclerViewAdapter;


  private List<RecommendBannerInfoBangTv.ItemListBean> recomendBannersList = new ArrayList<>();

  private RecommendBannerInfoBangTv recommendBanners = new RecommendBannerInfoBangTv();

  private List<LayoutFileInfo.LayoutBean> layoutBeans = new ArrayList<>();

  private boolean mIsRefreshing = false;
//  private DemoAdapter adapter;
  private List<List<RecommendBannerInfoBangTv.ItemListBean>> list1 = new ArrayList<List<RecommendBannerInfoBangTv.ItemListBean>>();

  int[] mList;

  private int _blc_pics;

  private boolean m_img=false;

  public static String menuId;

  public static HomeRecommendedFragmentBangTv newInstance(String id) {
    menuId = id;

    return new HomeRecommendedFragmentBangTv();
  }


  @Override
  public int getLayoutResId() {

    return R.layout.fragment_home_recommended_bangtv;
  }


  @Override
  public void finishCreateView(Bundle state) {

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

    mSectionedRecyclerViewAdapter = new HomePageBangTVAdapter(getActivity());
    mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);
    GridLayoutManager layout = new GridLayoutManager(getActivity(), 6);
    layout.setOrientation(LinearLayoutManager.VERTICAL);
    layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

      @Override
      public int getSpanSize(int position) {

        return mSectionedRecyclerViewAdapter.getSpanSize(position);
      }
    });
    mSwipeRefreshLayout.setOnRefreshListener(() -> {

      clearData();
      loadData();
    });

    mRecyclerView.setLayoutManager(layout);

}


  @Override
  protected void initRefreshLayout() {

    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    mSwipeRefreshLayout.setOnRefreshListener(this::loadData);
    mSwipeRefreshLayout.post(() -> {

      mSwipeRefreshLayout.setRefreshing(true);
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
          recomendBannersList.addAll(resultBeans.getItemList());
          recommendBanners=resultBeans;
          RetrofitHelper.getLayourFileAPI().getLayoutFileInfo(recommendBanners.getLayoutFile())
                  .compose(bindToLifecycle())
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(resultBeans1 -> {
                    layoutBeans.addAll(resultBeans1.getLayout());
                    finishTask();
                  }, throwable -> {
                    initEmptyView();
                  });
        }, throwable -> {
          initEmptyView();
        });


  }
  @Override
  protected void finishTask() {

    mSwipeRefreshLayout.setRefreshing(false);
    mIsRefreshing = false;
    hideEmptyView();
    recomendBannersList.remove(0);
    recomendBannersList.remove(0);
    recomendBannersList.remove(0);
    layoutBeans.remove(0);
    list1.add(recomendBannersList);
    mList = new int[recomendBannersList.size()];
    _blc_pics=0;
    for(int j=0;j<layoutBeans.size();j++){
      if(layoutBeans.get(j).getS()==21){
        mList[_blc_pics++] = 1;
        mList[_blc_pics++] = 1;
      }else if(layoutBeans.get(j).getS()==11){
        mList[_blc_pics++] = 12;
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
    mSectionedRecyclerViewAdapter.setLiveInfo(recomendBannersList,mList,true,"recommend");
    mSectionedRecyclerViewAdapter.notifyDataSetChanged();

//    }
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
