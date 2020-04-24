package com.china.cibn.bangtvmobile.bangtv.module.home.movielist;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.base.RxLazyFragment;
import com.china.cibn.bangtvmobile.bangtv.entity.region.RegionDetailsInfo;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;
import com.china.cibn.bangtvmobile.bangtv.widget.CircleProgressView;
import com.china.cibn.bangtvmobile.bangtv.widget.sectioned.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hcc on 16/8/4 21:18
 *
 * <p/>
 * 分区对应类型列表详情界面
 */
public class MovieListDetailsFragment extends RxLazyFragment {

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;

  @BindView(R.id.circle_progress)
  CircleProgressView mCircleProgressView;

  private String rid;

  private SectionedRecyclerViewAdapter mSectionedRecyclerViewAdapter;

  private List<RegionDetailsInfo.DataBean.NewBean> newsVideos = new ArrayList<>();

  private List<RegionDetailsInfo.DataBean.RecommendBean> recommendVideos = new ArrayList<>();


  public static MovieListDetailsFragment newInstance(String rid) {

    MovieListDetailsFragment fragment = new MovieListDetailsFragment();
    Bundle bundle = new Bundle();
    bundle.putString(ConstantUtil.EXTRA_RID, rid);
    fragment.setArguments(bundle);
    return fragment;
  }


  @Override
  public int getLayoutResId() {

    return R.layout.fragment_region_details_bangtv;
  }


  @Override
  public void finishCreateView(Bundle state) {

    rid = getArguments().getString(ConstantUtil.EXTRA_RID);

    isPrepared = true;
    lazyLoad();
  }


  @Override
  protected void lazyLoad() {

    if (!isPrepared || !isVisible) {
      return;
    }

    showProgressBar();
    initRecyclerView();
    loadData();
    isPrepared = false;
  }


  @Override
  protected void initRecyclerView() {

    mSectionedRecyclerViewAdapter = new SectionedRecyclerViewAdapter();
    GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
    mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

      @Override
      public int getSpanSize(int position) {

        switch (mSectionedRecyclerViewAdapter.getSectionItemViewType(position)) {
          case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
            return 1;

          default:
            return 1;
        }
      }
    });
    mRecyclerView.setLayoutManager(mLayoutManager);
    mRecyclerView.setAdapter(mSectionedRecyclerViewAdapter);
  }


  @Override
  protected void loadData() {
  }


  @Override
  protected void showProgressBar() {

    mCircleProgressView.setVisibility(View.VISIBLE);
    mCircleProgressView.spin();
  }


  @Override
  protected void hideProgressBar() {

    mCircleProgressView.setVisibility(View.GONE);
    mCircleProgressView.stopSpinning();
  }
}
