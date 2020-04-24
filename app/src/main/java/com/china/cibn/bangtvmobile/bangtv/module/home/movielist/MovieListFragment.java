package com.china.cibn.bangtvmobile.bangtv.module.home.movielist;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.stction.MovieListSection;
import com.china.cibn.bangtvmobile.bangtv.base.RxLazyFragment;
import com.china.cibn.bangtvmobile.bangtv.entity.MovieListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.region.RegionRecommendInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;
import com.china.cibn.bangtvmobile.bangtv.widget.banner.BannerEntity;
import com.china.cibn.bangtvmobile.bangtv.widget.sectioned.SectionedRecyclerViewAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 2016/10/21 20:39
 *
 * <p>
 * 分区推荐页面
 */

public class MovieListFragment extends RxLazyFragment {

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout mRefreshLayout;

  private boolean mIsRefreshing = false;

  private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;

  private List<BannerEntity> bannerEntities = new ArrayList<>();

  private List<RegionRecommendInfo.DataBean.BannerBean.TopBean> banners = new ArrayList<>();

  private List<RegionRecommendInfo.DataBean.RecommendBean> recommends = new ArrayList<>();

  private List<RegionRecommendInfo.DataBean.NewBean> news = new ArrayList<>();

  private List<RegionRecommendInfo.DataBean.DynamicBean> dynamics = new ArrayList<>();

  private List<MovieListInfo.ProgramListBean> programListBeanList=  new ArrayList<>();


  private boolean isTrue=true;//判断是筛选还是列表 列表为true

 private String mParentCatgId;

 private int mPageNumber;

 private String mPageSize;

  private String mCateName;

  private String mYear;

  private  String mArea;

  private String mType;

   private  String mClassType;

    private boolean isLoadingMore=true;

    private ProgressDialog pd;

    private boolean isFan=false;//翻页

    public static MovieListFragment newInstance(String rid,boolean _istrue,
                                            String _mParentCatgId, int _mPageNumber,
                                              String _mPageSize, String _mCateName,
                                            String _mYear, String _mArea, String _mType, String _mClassType) {

    MovieListFragment fragment = new MovieListFragment();
    Bundle bundle = new Bundle();
    bundle.putString(ConstantUtil.EXTRA_RID, rid);
      bundle.putString("mparent_catagid", _mParentCatgId);
      bundle.putInt("page_number", _mPageNumber);
      bundle.putString("page_size", _mPageSize);
      bundle.putString("cate_name", _mCateName);
      bundle.putString("_year", _mYear);
      bundle.putString("_area", _mArea);
      bundle.putString("_type", _mType);
      bundle.putString("class_type", _mClassType);
    bundle.putBoolean("istrue", _istrue);
    fragment.setArguments(bundle);
    return fragment;
  }


  @Override
  public int getLayoutResId() {

    return R.layout.fragment_region_recommend_bangtv;
  }


  @Override
  public void finishCreateView(Bundle state) {
   isTrue = getArguments().getBoolean("istrue");
     mParentCatgId= getArguments().getString("mparent_catagid");
     mPageNumber= getArguments().getInt("page_number");
     mPageSize =getArguments().getString("page_size");
     mCateName= getArguments().getString("cate_name");
      mYear=getArguments().getString("_year");
      mArea=getArguments().getString("_area");
      mType=getArguments().getString("_type");
      mClassType=getArguments().getString("class_type");
    initRefreshLayout();
    initRecyclerView();
  }


  @Override
  protected void initRefreshLayout() {

    mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    mRecyclerView.post(() -> {
      mRefreshLayout.setRefreshing(true);
      mIsRefreshing = true;
      mPageNumber=1;
      loadData();
    });
    mRefreshLayout.setOnRefreshListener(() -> {
        if(isTrue){
            clearData();
            mPageNumber=1;
            loadData();
        }else{
            mRefreshLayout.setRefreshing(false);

        }

    });
  }


  private void clearData() {

    bannerEntities.clear();
    banners.clear();
    recommends.clear();
    news.clear();
    dynamics.clear();
    mIsRefreshing = true;
    mSectionedRecyclerViewAdapter.removeAllSections();
  }


  @Override
  protected void initRecyclerView() {

    mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
    GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
    mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

      @Override
      public int getSpanSize(int position) {

        switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
          case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
            return 2;

          default:
            return 1;
        }
      }
    });
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);
    mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            int totalItemCount = mLayoutManager.getItemCount();
            //预加载

            if (lastVisibleItem >= totalItemCount - 4 && dy > 0&&isTrue) {
                Log.i("","");
                if(isLoadingMore){
                    isLoadingMore=false;
                    mPageNumber+=1;
                    isTrue=true;
                    isFan=true;
                    loadData();
                }else{
                    isLoadingMore=false;
                }
            }
        }

    });
    setRecycleNoScroll();
  }


  @Override
  protected void loadData() {
  if(isTrue){ 
      if(isFan){
          pd = ProgressDialog.show(getActivity(), "", getString(R.string.load_more1));
          new Handler(new Handler.Callback() {
              @Override
              public boolean handleMessage(Message arg0) {
                  if(pd.isShowing()){
                      pd.dismiss();
                  }
                  return false;
              }
          }).sendEmptyMessageDelayed(0, 20000);
      }
    RetrofitHelper.getBangTVAppAPI()
            .getProgramMenusDetails(BangtvApp.versionCodeUrl,mParentCatgId,mPageNumber,mPageSize,mCateName)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(dataBean -> {
                if(isFan){
                    isFan=false;
                    pd.dismiss();
                }
                if(dataBean.getProgramList()==null){
                    return;
                }else{
                    programListBeanList = dataBean.getProgramList();

                }
              finishTask();
            }, throwable -> {
                if(isFan){
                    isFan=false;
                    pd.dismiss();
                }
              mRefreshLayout.setRefreshing(false);
              ToastUtil.ShortToast(getString(R.string.error_text2));
            });
  }else {
      String encYear = "-1";
      String encArea = "-1";
      String encType = "-1";
      try {
          encYear = URLEncoder.encode(mYear, "UTF-8");
          encArea = URLEncoder.encode(mArea, "UTF-8");
          encType = URLEncoder.encode(mType, "UTF-8");
      } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }
      RetrofitHelper.getBangTVAppAPI()
            .getFiltersDetails(BangtvApp.versionCodeUrl,
                    mParentCatgId,encYear,encArea,encType,mClassType,mPageNumber,mPageSize)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(dataBean -> {
              programListBeanList = dataBean.getProgramList();
              finishTask();
            }, throwable -> {
              mRefreshLayout.setRefreshing(false);
              ToastUtil.ShortToast(getString(R.string.error_text2));
            });

  }
  }


  @Override
  protected void finishTask() {

    converBanner();
    if(programListBeanList!=null){
        mSectionedRecyclerViewAdapter.addSection(
                new MovieListSection(getActivity(), programListBeanList));
    }
    mIsRefreshing = false;
    mRefreshLayout.setRefreshing(false);
    mSectionedRecyclerViewAdapter.notifyDataSetChanged();
      isLoadingMore=true;

  }


  private void converBanner() {

    Observable.from(banners)
        .compose(bindToLifecycle())
        .forEach(topBean -> bannerEntities.add(new BannerEntity(topBean.getUri(),
            topBean.getTitle(), topBean.getImage())));
  }


  private void setRecycleNoScroll() {

    mRecyclerView.setOnTouchListener((v, event) -> mIsRefreshing);
  }
}
