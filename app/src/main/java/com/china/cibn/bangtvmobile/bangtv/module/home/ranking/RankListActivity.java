package com.china.cibn.bangtvmobile.bangtv.module.home.ranking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.entity.RankingListInfo;
import com.china.cibn.bangtvmobile.bangtv.module.home.search.SearchListActivity;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.widget.NoScrollViewPager;
import com.flyco.tablayout.SlidingTabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/3/27 14:12
 *
 * 排行榜界面
 */
public class RankListActivity extends RxBaseActivity {

  @BindView(R.id.sliding_tabs)
  SlidingTabLayout mSlidingTabLayout;

  @BindView(R.id.view_pager)
  NoScrollViewPager mViewPager;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.search_view)
  MaterialSearchView mSearchView;

  private List<RankingListInfo.DataBean> dataBeanList = new ArrayList<>();

  public LinkedHashMap<String, List<RankingListInfo.DataBean.ProgramListBean>> data = new LinkedHashMap<String, List<RankingListInfo.DataBean.ProgramListBean>>();

  private int position;


  @Override
  public int getLayoutId() {

    return R.layout.activity_ranking_bangtv;
  }

  @Override
  public String setPageName() {
    return getClass().getSimpleName();
  }


  @Override
  public void initViews(Bundle savedInstanceState) {

//    Intent intent = getIntent();
//    if (intent != null) {
//      position = intent.getIntExtra(ConstantUtil.EXTRA_POSITION, 0);
//    }
    initSearchView();
    loadData();
  }

  @Override
  public void loadData() {
    RetrofitHelper.getBangTVAppAPI().getRankingList(BangtvApp.versionCodeUrl,"")
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              dataBeanList.addAll(resultBeans.getData());
              finishTask();
            }, throwable -> {
            });
  }

  private void switchPager() {

    switch (position) {
      case 0:
        mViewPager.setCurrentItem(0);
        break;
      case 1:
        mViewPager.setCurrentItem(1);
        break;
      case 2:
        mViewPager.setCurrentItem(2);
        break;
      case 3:
        mViewPager.setCurrentItem(3);
        break;
      case 4:
        mViewPager.setCurrentItem(4);
        break;
      case 5:
        mViewPager.setCurrentItem(5);
        break;
    }
  }

  @Override
  public void finishTask() {
    AllareasRankPagerAdapter mAdapter =
            new AllareasRankPagerAdapter(getSupportFragmentManager(), dataBeanList);
    mViewPager.setAdapter(mAdapter);
    mSlidingTabLayout.setViewPager(mViewPager);
    switchPager();  }

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

    getMenuInflater().inflate(R.menu.menu_rank, menu);
    MenuItem item = menu.findItem(R.id.id_action_search);
    mSearchView.setMenuItem(item);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }

  private void initSearchView() {

    //初始化SearchBar
    mSearchView.setVoiceSearch(false);
    mSearchView.setCursorDrawable(R.drawable.custom_cursor);
    mSearchView.setEllipsize(true);
//    mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
    mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(String query) {

        SearchListActivity.launch(RankListActivity.this, query);
        return false;
      }



      @Override
      public boolean onQueryTextChange(String newText) {

        return false;
      }
    });
  }
  public static void launch(Activity activity) {

    Intent intent = new Intent(activity, RankListActivity.class);
    activity.startActivity(intent);
  }


  private static class AllareasRankPagerAdapter extends FragmentStatePagerAdapter {

    private List<RankingListInfo.DataBean> titles;


    AllareasRankPagerAdapter(FragmentManager fm, List<RankingListInfo.DataBean> titles) {

      super(fm);
      this.titles = titles;
    }


    @Override
    public Fragment getItem(int position) {
      return AllRankFragment
          .newInstance(position);
    }


    @Override
    public int getCount() {

      return titles.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {

      return titles.get(position).getChannel();
    }
  }
}
