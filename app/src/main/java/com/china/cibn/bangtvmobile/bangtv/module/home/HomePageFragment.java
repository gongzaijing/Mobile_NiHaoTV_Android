package com.china.cibn.bangtvmobile.bangtv.module.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.HomePagerAdapter;
import com.china.cibn.bangtvmobile.bangtv.base.RxLazyFragment;
import com.china.cibn.bangtvmobile.bangtv.entity.MenuListInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.common.BangTVMainActivity;
import com.china.cibn.bangtvmobile.bangtv.module.home.search.SearchListActivity;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.flyco.tablayout.SlidingTabLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/3/30 21:18
 *
 * <p/>
 * 首页模块主界面
 */
public class HomePageFragment extends RxLazyFragment {

  @BindView(R.id.view_pager)
  ViewPager mViewPager;

  @BindView(R.id.sliding_tabs)
  SlidingTabLayout mSlidingTab;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.search_view)
  MaterialSearchView mSearchView;

 private List<MenuListInfo.DataBean> dataList = new ArrayList<>();

    public static HomePageFragment newInstance() {

    return new HomePageFragment();
  }


  @Override
  public int getLayoutResId() {

    return R.layout.fragment_home_pager_bangtv;
  }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    @Override
  public void finishCreateView(Bundle state) {

    setHasOptionsMenu(true);
    initToolBar();
    initSearchView();
      loadData();
  }

    @Override
    protected void loadData() {
        RetrofitHelper.getNoCacheAppAPI().getMenuList(BangtvApp.versionCodeUrl)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(resultBeans -> {
                    AppGlobalVars.ISTRY_TIME=resultBeans.getTry_play_time();
                    dataList.addAll(resultBeans.getData());
                    initViewPager();
                }, throwable -> {
                    ToastUtil.ShortToast(getString(R.string.error_text2));
                });

    }

    private void initToolBar() {

    mToolbar.setTitle("");
    ((BangTVMainActivity) getActivity()).setSupportActionBar(mToolbar);
//    mCircleImageView.setImageResource(R.drawable.ic_hotbitmapgg_avatar);

  }

  @Override
  public void onResume() {
    super.onResume();
  }

  private void initSearchView() {

    //初始化SearchBar
    mSearchView.setVoiceSearch(false);
    mSearchView.setCursorDrawable(R.drawable.custom_cursor);
    mSearchView.setEllipsize(true);
    mSearchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
    mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

      @Override
      public boolean onQueryTextSubmit(String query) {

        SearchListActivity.launch(getActivity(), query);
        return false;
      }


      @Override
      public boolean onQueryTextChange(String newText) {

        return false;
      }
    });
  }


    private void initViewPager() {
    dataList.remove(3);
    HomePagerAdapter mHomeAdapter = new HomePagerAdapter(getChildFragmentManager(),
        getApplicationContext(),dataList);
    mViewPager.setOffscreenPageLimit(5);
    mViewPager.setAdapter(mHomeAdapter);
    mSlidingTab.setViewPager(mViewPager);
    mViewPager.setCurrentItem(1);
  }


  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

    menu.clear();
    inflater.inflate(R.menu.menu_main, menu);

    // 设置SearchViewItemMenu
    MenuItem item = menu.findItem(R.id.id_action_search);
    mSearchView.setMenuItem(item);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();
    switch (id) {
//      case R.id.id_action_game:
//        //游戏中心
//        startActivity(new Intent(getActivity(), GameCentreActivity.class));
//        break;
//
//      case R.id.id_action_download:
//        //离线缓存
//        startActivity(new Intent(getActivity(), OffLineDownloadActivity.class));
//        break;
    }

    return super.onOptionsItemSelected(item);
  }


  @OnClick(R.id.navigation_layout)
  void toggleDrawer() {

    Activity activity = getActivity();
    if (activity instanceof BangTVMainActivity) {
      ((BangTVMainActivity) activity).toggleDrawer();
    }
  }


  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {

    if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == Activity.RESULT_OK) {
      ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
      if (matches != null && matches.size() > 0) {
        String searchWrd = matches.get(0);
        if (!TextUtils.isEmpty(searchWrd)) {
          mSearchView.setQuery(searchWrd, false);
        }
      }

      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }


  public boolean isOpenSearchView() {

    return mSearchView.isSearchOpen();
  }


  public void closeSearchView() {

    mSearchView.closeSearch();
  }
}
