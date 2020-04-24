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
import android.widget.TextView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.SpecialTemplateAdapter;
import com.china.cibn.bangtvmobile.bangtv.entity.SpecialTemplateInfo;
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
 * 专题模板页面
 */
public class SpecialTemplateActivity extends RxBaseActivity {


  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.swipe_refresh_layout)
  SwipeRefreshLayout mSwipeRefreshLayout;

  @BindView(R.id.recycle)
  RecyclerView mRecyclerView;

  @BindView(R.id.tv_title)
  TextView tvTitle;

  private SpecialTemplateAdapter mAdapter;

  private List<SpecialTemplateInfo.SectionsBean.SpecialRecommendsBean.RecommendsBean> specialListBeanList = new ArrayList<>();

  private List<SpecialTemplateInfo> specialTemplateInfos = new ArrayList<>();

  private String mId;

  private String mName;


  @Override
  public int getLayoutId() {

    return R.layout.activity_special_template_bangtv;
  }

  @Override
  public String setPageName() {
    return getClass().getSimpleName();
  }


  @Override
  public void initViews(Bundle savedInstanceState) {

    Intent intent = getIntent();
    if (intent != null) {
      mId = intent.getStringExtra("special_template_id");
      mName = intent.getStringExtra("special_template_name");

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
    mAdapter = new SpecialTemplateAdapter(this,mRecyclerView, specialListBeanList);
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
    RetrofitHelper.getBangTVAppAPI().getSpecialTemplate(BangtvApp.versionCodeUrl,mId)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              specialTemplateInfos.add(resultBeans);
              specialListBeanList.addAll(resultBeans.getSections().getSpecialRecommends().getRecommends());
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
    tvTitle.setText(mName);
    mToolbar.setTitle("");
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if (actionBar != null) {
      actionBar.setDisplayHomeAsUpEnabled(true);
    }
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    getMenuInflater().inflate(R.menu.menu_rank, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }


  public static void launch(Activity activity,String mId,String mName) {

    Intent intent = new Intent(activity, SpecialTemplateActivity.class);
    intent.putExtra("special_template_id", mId);
    intent.putExtra("special_template_name", mName);


    activity.startActivity(intent);
  }


}
