package com.china.cibn.bangtvmobile.bangtv.module.home.radio;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.AllRankAdapter;
import com.china.cibn.bangtvmobile.bangtv.apapter.RadioAdapter;
import com.china.cibn.bangtvmobile.bangtv.base.RxLazyFragment;
import com.china.cibn.bangtvmobile.bangtv.entity.RankingListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.radio.RadioDetailsInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/4/2 20:23
 *
 * <p/>
 * 数字电台界面
 */
public class RadioFragment extends RxLazyFragment {

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout mSwipeRefreshLayout;

  private String mId;

  private List<RadioDetailsInfo.ProgramListBean> beanArrayList = new ArrayList<>();

  private RadioAdapter mAdapter;


  public static RadioFragment newInstance(String mId) {

    RadioFragment mFragment = new RadioFragment();
    Bundle mBundle = new Bundle();
    mBundle.putString("radio_id",mId);
    mFragment.setArguments(mBundle);
    return mFragment;
  }


  @Override
  public int getLayoutResId() {

    return R.layout.fragment_all_areas_rank_bangtv;
  }


  @Override
  public void finishCreateView(Bundle state) {

    mId = getArguments().getString("radio_id");
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
    RetrofitHelper.getBangTVAppAPI().getRadioDetails(BangtvApp.versionCodeUrl,mId)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              beanArrayList.addAll(resultBeans.getProgramList());
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
    mAdapter = new RadioAdapter(getActivity(),mRecyclerView, beanArrayList);
    mRecyclerView.setAdapter(mAdapter);
//    mAdapter.setOnItemClickListener((position, holder) -> VideoDetailsActivity.launch(getActivity(),
//        allRanks.get(position).getAid(),
//        allRanks.get(position).getPic()));
  }
}
