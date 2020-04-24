package com.china.cibn.bangtvmobile.bangtv.module.home.radio;

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
import com.china.cibn.bangtvmobile.bangtv.entity.radio.RadioListInfo;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.widget.NoScrollViewPager;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/4/2 14:12
 *
 * 数字电台界面
 */
public class RadioListActivity extends RxBaseActivity {

  @BindView(R.id.sliding_tabs)
  SlidingTabLayout mSlidingTabLayout;

  @BindView(R.id.view_pager)
  NoScrollViewPager mViewPager;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  private List<RadioListInfo> listInfos =new ArrayList<>();


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

    loadData();
  }

  @Override
  public void loadData() {
    RetrofitHelper.getBangTVAppAPI().getRadioList(BangtvApp.versionCodeUrl)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(resultBeans -> {
              listInfos.addAll(resultBeans);
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
    RadioPagerAdapter mAdapter =
            new RadioPagerAdapter(getSupportFragmentManager(), listInfos);
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
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }
    return super.onOptionsItemSelected(item);
  }


  public static void launch(Activity activity) {

    Intent intent = new Intent(activity, RadioListActivity.class);
    activity.startActivity(intent);
  }


  private static class RadioPagerAdapter extends FragmentStatePagerAdapter {

    private List<RadioListInfo> titles;

    private String mId;

    RadioPagerAdapter(FragmentManager fm,List<RadioListInfo> titles) {

      super(fm);
      this.titles = titles;

    }


    @Override
    public Fragment getItem(int position) {
      mId=titles.get(position).getCate();
      return RadioFragment
          .newInstance(mId);
    }


    @Override
    public int getCount() {

      return 1;
    }


    @Override
    public CharSequence getPageTitle(int position) {

      return titles.get(position).getType();
    }
  }
}
