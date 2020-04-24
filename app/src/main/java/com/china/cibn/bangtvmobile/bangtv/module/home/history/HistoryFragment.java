package com.china.cibn.bangtvmobile.bangtv.module.home.history;

import butterknife.BindView;

import com.china.cibn.bangtvmobile.bangtv.apapter.HistoryListAdapter;
import com.china.cibn.bangtvmobile.bangtv.base.RxLazyFragment;
import com.china.cibn.bangtvmobile.bangtv.dal.PlayRecordHelpler;
import com.china.cibn.bangtvmobile.bangtv.entity.HistoryListInfo;

import com.china.cibn.bangtvmobile.bangtv.module.common.BangTVMainActivity;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.widget.CustomEmptyView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * bix by gz 2018/04/24 14:12
 *
 * <p/>
 * 历史记录
 */
public class HistoryFragment extends RxLazyFragment {

  @BindView(R.id.empty_view)
  CustomEmptyView mCustomEmptyView;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  //fix by gz 2018/04/20 横向滑动控件
  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout mSwipeRefreshLayout;
  //fix by gz 2018/04/20 横向滑动控件
  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;


  private HistoryListAdapter mAdapter;
  private List<HistoryListInfo.HistoryListBean> historyListBeanList ;

  private String mId;

  private HistoryDataHandle historyDataHandle;    //fix by gz 2018/04/19 database
  private PlayRecordHelpler mPlayRecordOpt;       //fix by gz 2018/04/19 database



  public static HistoryFragment newInstance() {
    return new HistoryFragment();
  }

  @Override
  public int getLayoutResId() {
    return R.layout.activity_history_bangtv;
  }

  @Override
  public void finishCreateView(Bundle state) {
    initCustomEmptyView();
    initRefreshLayout();
    initRecyclerView();
    initToolBar();
  }

  public void initCustomEmptyView() {
    historyListBeanList= new ArrayList<>();
    mCustomEmptyView.setEmptyImage(R.drawable.ic_movie_pay_order_error03);
    mCustomEmptyView.setEmptyText("您还没有观看记录，赶紧观看视频吧");
  }

  @Override
  protected void initRefreshLayout() {
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    mSwipeRefreshLayout.post(() -> {
      mSwipeRefreshLayout.setRefreshing(true);
      loadData();
    });
    mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        loadData();
        mSwipeRefreshLayout.setRefreshing(false);
      }
    });
  }

  @Override
  protected void initRecyclerView(){
    mSwipeRefreshLayout.setRefreshing(false);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setNestedScrollingEnabled(true);
    GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new HistoryListAdapter(getActivity(), mRecyclerView, historyListBeanList);
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override
  public void finishTask() {
    mSwipeRefreshLayout.setRefreshing(false);
    mAdapter.notifyDataSetChanged();
  }

  public void initToolBar() {

    mToolbar.setTitle("");
    mToolbar.setNavigationIcon(R.drawable.ic_navigation_drawer);
    mToolbar.setNavigationOnClickListener(v -> {

      Activity activity1 = getActivity();
      if (activity1 instanceof BangTVMainActivity) {
        ((BangTVMainActivity) activity1).toggleDrawer();
      }
    });
  }

  @Override
  protected void loadData() {

    mPlayRecordOpt = new PlayRecordHelpler(getActivity());

    historyDataHandle = new HistoryDataHandle();
    if(historyListBeanList!=null){
      historyListBeanList.clear();
    }
    historyListBeanList.addAll( historyDataHandle.getHistorydata(mPlayRecordOpt));

    if (0 == historyListBeanList.size()) {
      historyempty();
    }
    else {
          historyfull();
    }

    finishTask();
  }

  public void historyempty()
  {
    mToolbar.setVisibility(View.VISIBLE);
    mSwipeRefreshLayout.setVisibility(View.GONE);//View.VISIBLE
    mRecyclerView.setVisibility(View.GONE);
    mCustomEmptyView.setVisibility(View.VISIBLE);
  }

  public void historyfull() {
    mToolbar.setVisibility(View.VISIBLE);
    mSwipeRefreshLayout.setVisibility(View.VISIBLE);//View.VISIBLE
    mRecyclerView.setVisibility(View.VISIBLE);
    mCustomEmptyView.setVisibility(View.GONE);
  }


  @Override
  public void onResume() {
    super.onResume();
    loadData();
  }
}