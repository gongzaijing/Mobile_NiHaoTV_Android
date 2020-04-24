package com.china.cibn.bangtvmobile.bangtv.module.home.ranking;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.AllRankAdapter;
import com.china.cibn.bangtvmobile.bangtv.base.RxLazyFragment;
import com.china.cibn.bangtvmobile.bangtv.entity.RankingListInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/3/24 20:23
 *
 * <p/>
 * 全区排行榜界面
 */
public class AllRankFragment extends RxLazyFragment {

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout mSwipeRefreshLayout;

  private int type;

  private List<RankingListInfo.DataBean> allRanks = new ArrayList<>();
  private List<RankingListInfo.DataBean.ProgramListBean> programListBeanList = new ArrayList<>();

  private AllRankAdapter mAdapter;


  public static AllRankFragment newInstance(int type) {

    AllRankFragment mFragment = new AllRankFragment();
    Bundle mBundle = new Bundle();
    mBundle.putInt(ConstantUtil.EXTRA_KEY, type);
    mFragment.setArguments(mBundle);
    return mFragment;
  }


  @Override
  public int getLayoutResId() {

    return R.layout.fragment_all_areas_rank_bangtv;
  }


  @Override
  public void finishCreateView(Bundle state) {

    type = getArguments().getInt(ConstantUtil.EXTRA_KEY);
    initRefreshLayout();
    initRecyclerView();
  }


  @Override
  protected void initRefreshLayout() {

    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    mSwipeRefreshLayout.post(() -> {
      mSwipeRefreshLayout.setRefreshing(true);
      loadData();
    });
    mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.setRefreshing(false));
  }


  @Override
  protected void loadData() {
    RetrofitHelper.getBangTVAppAPI().getRankingList(BangtvApp.versionCodeUrl,"")
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              allRanks.addAll(resultBeans.getData());
              programListBeanList.addAll(allRanks.get(type).getProgramList());
                finishTask();
            }, throwable -> {
              mSwipeRefreshLayout.setRefreshing(false);
              ToastUtil.ShortToast(getString(R.string.error_text2));

            });
  }


  @Override
  protected void finishTask() {

    mSwipeRefreshLayout.setRefreshing(false);
    mAdapter.notifyDataSetChanged();
  }


  @Override
  protected void initRecyclerView() {

    mSwipeRefreshLayout.setRefreshing(false);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setNestedScrollingEnabled(true);
    GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new AllRankAdapter(getActivity(),mRecyclerView, programListBeanList);
    mRecyclerView.setAdapter(mAdapter);
//    mAdapter.setOnItemClickListener((position, holder) -> VideoDetailsActivity.launch(getActivity(),
//        allRanks.get(position).getAid(),
//        allRanks.get(position).getPic()));
  }
}
