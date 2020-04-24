package com.china.cibn.bangtvmobile.bangtv.module.home.special;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.SpecialListAdapter;
import com.china.cibn.bangtvmobile.bangtv.entity.SpecialListInfo;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/3/27 14:12
 *
 * 专题汇总页面
 */
public class SpecialListActivity extends RxBaseActivity {


  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout mSwipeRefreshLayout;

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;


  private SpecialListAdapter mAdapter;
  private List<SpecialListInfo.SpecialListBean> specialListBeanList = new ArrayList<>();


  private String mId;


  @Override
  public int getLayoutId() {

    return R.layout.activity_special_bangtv;
  }

  @Override
  public String setPageName() {
    return getClass().getSimpleName();
  }


  @Override
  public void initViews(Bundle savedInstanceState) {

    Intent intent = getIntent();
    if (intent != null) {
      mId = intent.getStringExtra("specialID");
    }
    initRefreshLayout();
    initRecyclerView();
  }

  @Override
  public void initRecyclerView() {
    mSwipeRefreshLayout.setRefreshing(false);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setNestedScrollingEnabled(true);
    GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);

    mRecyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new SpecialListAdapter(this,mRecyclerView, specialListBeanList);
    mRecyclerView.setAdapter(mAdapter);
  }

  @Override
  public void initRefreshLayout() {
    mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    mSwipeRefreshLayout.post(() -> {
      mSwipeRefreshLayout.setRefreshing(true);
      loadData();
    });
    mSwipeRefreshLayout.setOnRefreshListener(() -> mSwipeRefreshLayout.setRefreshing(false));  }

  @Override
  public void loadData() {
    RetrofitHelper.getBangTVAppAPI().getSpecialList(BangtvApp.versionCodeUrl,mId)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              specialListBeanList.addAll(resultBeans.getSpecialList());
              finishTask();
            }, throwable -> {
            });
  }

  @Override
  public void finishTask() {
    mSwipeRefreshLayout.setRefreshing(false);
    mAdapter.notifyDataSetChanged();
     }

  @Override
  public void initToolBar() {

    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }


  public static void launch(Activity activity,String mId) {

    Intent intent = new Intent(activity, SpecialListActivity.class);
    intent.putExtra("specialID", mId);

    activity.startActivity(intent);
  }


}
