package com.china.cibn.bangtvmobile.bangtv.module.home.search;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.SearchListAdapter;
import com.china.cibn.bangtvmobile.bangtv.entity.search.SearchListInfo;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.KeyBoardUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.StatusBarUtil;
import com.china.cibn.bangtvmobile.bangtv.widget.CircleProgressView;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hcc on 16/8/29 19:58
 *
 * <p/>
 * 全站搜索界面
 */
public class SearchListActivity extends RxBaseActivity {



  @BindView(R.id.search_img)
  ImageView mSearchBtn;

  @BindView(R.id.search_edit)
  EditText mSearchEdit;

  @BindView(R.id.search_text_clear)
  ImageView mSearchTextClear;

  @BindView(R.id.recycle)
  RecyclerView recyclerView;

  @BindView(R.id.video_loading_progress)
  CircleProgressView mCircleProgress;

  @BindView(R.id.tv_serach_fail)
  TextView  mSerashFail;
  private String content;

//  private AnimationDrawable mAnimationDrawable;

  private List<String> titles = new ArrayList<>();

  private List<Fragment> fragments = new ArrayList<>();

  private List<SearchListInfo.ProgramListBean> navs = new ArrayList<>();

  private SearchListAdapter mAdapter;

  @Override
  public int getLayoutId() {

    return R.layout.activity_search_bangtv;
  }

  @Override
  public String setPageName() {
    return getClass().getSimpleName();
  }


  @Override
  public void initToolBar() {
    //设置6.0以上StatusBar字体颜色
    StatusBarUtil.from(this)
        .setLightStatusBar(true)
        .process();
  }


  @Override
  public void initViews(Bundle savedInstanceState) {

    Intent intent = getIntent();
    if (intent != null) {
      content = intent.getStringExtra(ConstantUtil.EXTRA_CONTENT);
    }

//    mLoadingView.setImageResource(R.drawable.anim_search_loading);
//    mAnimationDrawable = (AnimationDrawable) mLoadingView.getDrawable();
    showSearchAnim();
    mSearchEdit.clearFocus();
    mSearchEdit.setText(content);
    getSearchData();
    search();
    setUpEditText();
    initRecyclerView();
  }

  @Override
  public void initRecyclerView() {
    recyclerView.setHasFixedSize(true);
    recyclerView.setNestedScrollingEnabled(true);
    GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);

    recyclerView.setLayoutManager(mLayoutManager);
    mAdapter = new SearchListAdapter(this,recyclerView, navs);
    recyclerView.setAdapter(mAdapter);
  }

  private void setUpEditText() {

    RxTextView.textChanges(mSearchEdit)
        .compose(this.bindToLifecycle())
        .map(CharSequence::toString)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(s -> {

          if (!TextUtils.isEmpty(s)) {
            mSearchTextClear.setVisibility(View.VISIBLE);
          } else {
            mSearchTextClear.setVisibility(View.GONE);
          }
        });

    RxView.clicks(mSearchTextClear)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(aVoid -> {

          mSearchEdit.setText("");
        });

    RxTextView.editorActions(mSearchEdit)
        .filter(integer -> !TextUtils.isEmpty(mSearchEdit.getText().toString().trim()))
        .filter(integer -> integer == EditorInfo.IME_ACTION_SEARCH)
        .flatMap(new Func1<Integer, Observable<String>>() {

          @Override
          public Observable<String> call(Integer integer) {

            return Observable.just(mSearchEdit.getText().toString().trim());
          }
        })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(s -> {

          KeyBoardUtil.closeKeybord(mSearchEdit,
              SearchListActivity.this);
          showSearchAnim();
          clearData();
          content = s;
          getSearchData();
        });
  }


  private void search() {

    RxView.clicks(mSearchBtn)
        .throttleFirst(2, TimeUnit.SECONDS)
        .map(aVoid -> mSearchEdit.getText().toString().trim())
        .filter(s -> !TextUtils.isEmpty(s))
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(s -> {

          KeyBoardUtil.closeKeybord(mSearchEdit,
              SearchListActivity.this);
          showSearchAnim();
          clearData();
          content = s;
          getSearchData();
        });
  }


  private void clearData() {

    navs.clear();
    titles.clear();
    fragments.clear();
  }


  private void getSearchData() {

   String page = "1";
    String pageSize = "100";
    String searchType="0";
    RetrofitHelper.getBangTVAppAPI().getSearchList(BangtvApp.versionCodeUrl, searchType,content,page,pageSize)
        .compose(this.bindToLifecycle())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(dataBean -> {
          navs.addAll(dataBean.getProgramList());
          finishTask();
        }, throwable -> {
          setEmptyLayout();
          hideSearchAnim();
        });
  }


  @Override
  public void finishTask() {
    mAdapter.notifyDataSetChanged();
    hideSearchAnim();
  }


  public String checkNumResults(int numResult) {

    return numResult > 100 ? "99+" : String.valueOf(numResult);
  }


//  private void measureTabLayoutTextWidth(int position) {
//
//    String title = titles.get(position);
//    TextView titleView = mSlidingTabLayout.getTitleView(position);
//    TextPaint paint = titleView.getPaint();
//    float textWidth = paint.measureText(title);
//    mSlidingTabLayout.setIndicatorWidth(textWidth / 3);
//  }


  private void showSearchAnim() {

//    mLoadingView.setVisibility(View.VISIBLE);
//    mAnimationDrawable.start();
    mCircleProgress.setVisibility(View.VISIBLE);
  }


  private void hideSearchAnim() {
    mCircleProgress.setVisibility(View.GONE);
    if(mSerashFail.getVisibility()==View.VISIBLE) {
      mSerashFail.setVisibility(View.GONE);
    }
//    mLoadingView.setVisibility(View.GONE);
//    mAnimationDrawable.stop();
  }


  public void setEmptyLayout() {
    mSerashFail.setVisibility(View.VISIBLE);
//    mCircleProgress.setVisibility(View.VISIBLE);
//    mLoadingView.setImageResource(R.drawable.search_failed);
  }


  @OnClick(R.id.search_back)
  void OnBack() {

    onBackPressed();
  }


  @Override
  public void onBackPressed() {

//    if (mAnimationDrawable != null && mAnimationDrawable.isRunning()) {
//      mAnimationDrawable.stop();
//      mAnimationDrawable = null;
//    }

    super.onBackPressed();
  }


  public static void launch(Activity activity, String str) {

    Intent mIntent = new Intent(activity, SearchListActivity.class);
    mIntent.putExtra(ConstantUtil.EXTRA_CONTENT, str);
    activity.startActivity(mIntent);
  }


  @Override
  protected void onDestroy() {

    super.onDestroy();
//    if (mAnimationDrawable != null) {
//      mAnimationDrawable.stop();
//      mAnimationDrawable = null;
//    }
  }


}
