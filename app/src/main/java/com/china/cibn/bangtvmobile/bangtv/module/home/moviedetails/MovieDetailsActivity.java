package com.china.cibn.bangtvmobile.bangtv.module.home.moviedetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.MoreTotalAdapter;
import com.china.cibn.bangtvmobile.bangtv.apapter.MovieListDetailsSelectionAdapter;
import com.china.cibn.bangtvmobile.bangtv.dal.MPlayRecordInfo;
import com.china.cibn.bangtvmobile.bangtv.dal.PlayRecordHelpler;
import com.china.cibn.bangtvmobile.bangtv.entity.MovieDetailsInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.user.maillogin.LoginActivity;
import com.china.cibn.bangtvmobile.bangtv.module.user.vip.VipActivity;
import com.china.cibn.bangtvmobile.bangtv.module.video.play.VideoPlayerActivity;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.utils.SystemBarHelper;
import com.china.cibn.bangtvmobile.bangtv.widget.CircleProgressView;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by gzj on 18/3/30 17:51
 *
 * <p/>
 * 电影详情界面
 */
public class MovieDetailsActivity extends RxBaseActivity {

  @BindView(R.id.nested_scroll_view)
  NestedScrollView mNestedScrollView;

  @BindView(R.id.bangumi_bg)
  ImageView mBangumiBackgroundImage;

  @BindView(R.id.toolbar)
  Toolbar mToolbar;

  @BindView(R.id.bangumi_pic)
  ImageView mBangumiPic;

  @BindView(R.id.bangumi_details_layout)
  LinearLayout mDetailsLayout;

  @BindView(R.id.circle_progress)
  CircleProgressView mCircleProgressView;

  @BindView(R.id.bangumi_title)
  TextView mBangumiTitle;

  @BindView(R.id.bangumi_update)
  TextView mBangumiUpdate;

  @BindView(R.id.bangumi_play)
  TextView mBangumiPlay;

  @BindView(R.id.bangumi_selection_recycler)
  RecyclerView mBangumiSelectionRecycler;

  @BindView(R.id.tags_layout)
  TagFlowLayout mTagsLayout;

  @BindView(R.id.bangumi_details_introduction)
  TextView mBangumiIntroduction;

  @BindView(R.id.tv_update_index)
  TextView mUpdateIndex;

  @BindView(R.id.bangumi_seasons_recycler)
  RecyclerView MoreRecycler;

  @BindView(R.id.movie_detail_director)
  TextView mTvDirector;

  @BindView(R.id.movie_detail_actor)
  TextView mTvActor;

  @BindView(R.id.text_xuanji)
  RelativeLayout tvXuanji;

  @BindView(R.id.movie_relative_dieector)
  RelativeLayout mRelativeDircetor;

  @BindView(R.id.movie_relative_actor)
  RelativeLayout mRelativeActor;

  @BindView(R.id.play_video)
  ImageView mPlayVideo;
//  @BindView(R.id.bangumi_comment_recycler)
//  RecyclerView mBangumiCommentRecycler;
//
//  @BindView(R.id.bangumi_recommend_recycler)
//  RecyclerView mBangumiRecommendRecycler;
//
//  @BindView(R.id.tv_bangumi_comment_count)
//  TextView mBangumiCommentCount;

  private String seasonId;

  private  MovieDetailsInfo movieDetailsBean = new MovieDetailsInfo();

  private List<MovieDetailsInfo.SourcesBean> movieDetailsSouresList = new ArrayList<>();

  private int totalEpisode=-1;

  private static final int DIVISIONVALUE = 50; // 上方划分区间,默认是50

  private ArrayList<String[]> data = new ArrayList<String[]>();

  String[] episodeArray;

  private MoreTotalAdapter moreTotalAdapter;

  private MovieListDetailsSelectionAdapter movieListDetailsSelectionAdapter;

  private String programSeriesId;

  private  PlayRecordHelpler prh;

  private  MPlayRecordInfo info;

  private int mNumber;

    private AlertDialog.Builder mDialog;

    @Override
  public int getLayoutId() {

    return R.layout.activity_movie_details_bangtv;
  }

    @Override
    public String setPageName() {
        return getClass().getSimpleName();
    }


    @Override
  public void initViews(Bundle savedInstanceState) {

    Intent intent = getIntent();
    if (intent != null) {
      seasonId = intent.getStringExtra("movie_details_id");
    }

  }

    //设置语言
    private void set(String lauType) {
        // 本地语言设置
        Locale myLocale = new Locale(lauType);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

  @Override
  public void loadData() {
    RetrofitHelper.getNoCacheAppAPI().getMovieDetails(BangtvApp.versionCodeUrl,seasonId)
            .compose(bindToLifecycle())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(movieListBeans -> {
              movieDetailsSouresList.addAll(movieListBeans.getSources());
              movieDetailsBean = movieListBeans;
              finishTask();
                mPlayVideo.setEnabled(true);
            }, throwable -> {
              Log.i("","");
              hideProgressBar();
                mPlayVideo.setEnabled(true);
            });
  }


  @SuppressLint("SetTextI18n")
  @Override
  public void finishTask() {
    //设置封面  先缓存到本地，然后获取
      new getImageCacheAsyncTask(this).execute("url");

    //设置标题
    mBangumiTitle.setText(movieDetailsBean.getName()+"("+movieDetailsBean.getReleaseDate()+")");
    //是否是多集
    totalEpisode=movieDetailsBean.getSources().size();
    episodeArray = new String[totalEpisode];

    //设置更新状态
    if (movieDetailsBean.getLastest()!=null) {
      mBangumiUpdate.setVisibility(View.VISIBLE);
      mBangumiUpdate.setText(movieDetailsBean.getLastest());
    } else {
      mBangumiUpdate.setVisibility(View.GONE);
}
    //设置导演
    if(TextUtils.isEmpty(movieDetailsBean.getDirector())){
      mRelativeDircetor.setVisibility(View.GONE);
      mTvDirector.setVisibility(View.GONE);
    }else {
      mRelativeDircetor.setVisibility(View.VISIBLE);
      mTvDirector.setVisibility(View.VISIBLE);
      mTvDirector.setText(movieDetailsBean.getDirector());
    }
    //设置演员
    if(TextUtils.isEmpty(movieDetailsBean.getActor())){
      mRelativeActor.setVisibility(View.GONE);
      mTvActor.setVisibility(View.GONE);
    }else {
      mRelativeActor.setVisibility(View.VISIBLE);
      mTvActor.setVisibility(View.VISIBLE);
      mTvActor.setText(movieDetailsBean.getActor());
    }
//    //设置简介
    mBangumiIntroduction.setText(movieDetailsBean.getInformation());
//    //设置评论数量
//    mBangumiCommentCount.setText("评论 第1话(" + mPageInfo.getAcount() + ")");
//    //设置标签布局
//    List<BangumiDetailsInfo.ResultBean.TagsBean> tags = result.getTags();
//    mTagsLayout.setAdapter(new TagAdapter<BangumiDetailsInfo.ResultBean.TagsBean>(tags) {
//
//      @Override
//      public View getView(FlowLayout parent, int position, BangumiDetailsInfo.ResultBean.TagsBean tagsBean) {
//
//        TextView mTags = (TextView) LayoutInflater.from(MovieDetailsActivity.this)
//            .inflate(R.layout.layout_tags_item, parent, false);
//        mTags.setText(tagsBean.getTag_name());
//
//        return mTags;
//      }
//    });

    //设置分季版本
//    initSeasonsRecycler();
    //设置选集
    initSelectionRecycler();
    //设置番剧推荐
//    initRecommendRecycler();
    //设置评论
//    initCommentRecycler();
    //加载完毕隐藏进度条
    hideProgressBar();
    mPlayVideo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
                if(movieDetailsBean.getIsplay().equals("0")){//地区访问限制
               ToastUtil.LongToast(getString(R.string.splashactivity_text4));
                  return;
                }
          if(movieDetailsBean.getSources().get(0).getIstry()!=0){
              VideoPlayerActivity.launch(MovieDetailsActivity.this,
                      movieDetailsSouresList.get(mNumber - 1).getVideoid(),
                      movieDetailsBean.getName(), movieDetailsBean.getId(),
                      mNumber + "", movieDetailsBean.getType(),movieDetailsBean);
          }else {
              if (movieDetailsBean.getIslogin() == 0) {//未登录用户去登录页面
                  startActivity(new Intent(MovieDetailsActivity.this, LoginActivity.class));
              } else {
                  if (movieDetailsBean.getServerbuy() == 0) {//未支付用户去支付页面
                      initDialog();

                  } else {
                      //fix by gz 2018/06/05 判断一下是不是第一个元素 防止数组越界
                      if(movieDetailsSouresList.size() >= 1) {
                          VideoPlayerActivity.launch(MovieDetailsActivity.this,
                                  movieDetailsSouresList.get(mNumber - 1).getVideoid(),
                                  movieDetailsBean.getName(), movieDetailsBean.getId(),
                                  mNumber + "", movieDetailsBean.getType(),movieDetailsBean);
                      } else {
                          VideoPlayerActivity.launch(MovieDetailsActivity.this,
                                  movieDetailsSouresList.get(0).getVideoid(),
                                  movieDetailsBean.getName(), movieDetailsBean.getId(),
                                  mNumber + "", movieDetailsBean.getType(), movieDetailsBean);
                      }
                  }
              }
          }
      }
    });

  }
    private class getImageCacheAsyncTask extends AsyncTask<String, Void, File> {
        private final Context context;

        public getImageCacheAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected File doInBackground(String... params) {
            String imgUrl =  params[0];
            try {
                return Glide.with(context)
                        .load(movieDetailsBean.getPicurl())
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            String path = result.getPath();
            Log.e("path", path);
            Glide.with(context)
                    .load(path)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bangtv_default_bj)
                    .dontAnimate()
                    .into(mBangumiPic);
            //高斯模糊图片
            Glide.with(context)
                    .load(path)
                    .bitmapTransform(new BlurTransformation(context))
                    .into(mBangumiBackgroundImage);
        }
    }

    /**
     * 初始化评论recyclerView
     */
//  private void initCommentRecycler() {
//
//    mBangumiCommentRecycler.setHasFixedSize(false);
//    mBangumiCommentRecycler.setNestedScrollingEnabled(false);
//    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
//    mBangumiCommentRecycler.setLayoutManager(mLinearLayoutManager);
//    BangumiDetailsCommentAdapter mCommentAdapter = new BangumiDetailsCommentAdapter(
//        mBangumiCommentRecycler, replies);
//    HeaderViewRecyclerAdapter mHeaderViewRecyclerAdapter = new HeaderViewRecyclerAdapter(
//        mCommentAdapter);
//
//    View headView = LayoutInflater.from(this)
//        .inflate(R.layout.layout_video_hot_comment_head, mBangumiCommentRecycler, false);
//    RecyclerView mHotCommentRecycler = (RecyclerView) headView.findViewById(
//        R.id.hot_comment_recycler);
//    mHotCommentRecycler.setHasFixedSize(false);
//    mHotCommentRecycler.setNestedScrollingEnabled(false);
//    mHotCommentRecycler.setLayoutManager(new LinearLayoutManager(this));
//    BangumiDetailsHotCommentAdapter mVideoHotCommentAdapter = new BangumiDetailsHotCommentAdapter(
//        mHotCommentRecycler, hotComments);
//    mHotCommentRecycler.setAdapter(mVideoHotCommentAdapter);
//    mHeaderViewRecyclerAdapter.addHeaderView(headView);
//
//    mBangumiCommentRecycler.setAdapter(mHeaderViewRecyclerAdapter);
//  }


  /**
   * 初始化多集recyclerView  大于100集
   */
  private void initSeasonsRecycler() {
    int right =totalEpisode % DIVISIONVALUE == 0 ? totalEpisode
            / DIVISIONVALUE : totalEpisode / DIVISIONVALUE + 1;

    {
      String[] dividers = new String[right];
      for (int i = 0; i < right; i++) {
        if (DIVISIONVALUE * (i + 1) <= totalEpisode) {
          dividers[i] = DIVISIONVALUE * i + 1 + "-"
                  + DIVISIONVALUE * (i + 1) +"集";
        } else {
          dividers[i] = DIVISIONVALUE * i + 1 + "-"
                  + totalEpisode + "集";
          break;
        }
      }
      data.add(dividers);
    }

//

    MoreRecycler.setHasFixedSize(false);
    MoreRecycler.setNestedScrollingEnabled(false);
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this,
        LinearLayoutManager.HORIZONTAL, false);
    MoreRecycler.setLayoutManager(mLinearLayoutManager);


    for(int i=0;i<data.size();i++){
      moreTotalAdapter = new MoreTotalAdapter(
               MoreRecycler, new ArrayList<String>(
              Arrays.asList(data.get(i))));

      MoreRecycler.setAdapter(moreTotalAdapter);
      moreTotalAdapter.notifyItemForeground(i);

    }
    moreTotalAdapter.setOnItemClickListener((position, holder) -> {
      moreTotalAdapter.notifyItemForeground(position);
      MoreRecycler.scrollToPosition(position);
      movieListDetailsSelectionAdapter.notifyItemForeground(position*DIVISIONVALUE);
    mBangumiSelectionRecycler.scrollToPosition(position*DIVISIONVALUE);
    });
//
  }


  /**
   * 初始化选集recyclerView
   */
  private void initSelectionRecycler() {
    if(totalEpisode>1){
      mBangumiSelectionRecycler.setVisibility(View.VISIBLE);
      tvXuanji.setVisibility(View.VISIBLE);
    }else {
      mNumber=1;
      mBangumiSelectionRecycler.setVisibility(View.GONE);
      tvXuanji.setVisibility(View.GONE);
      return;
    }
    /**
     * 观看历史记录
     * */
      programSeriesId= movieDetailsBean.getId();
      prh= new PlayRecordHelpler(this);
      info = prh.getPlayRcInfo(programSeriesId);
      if(info!=null&&info.getPlayerpos()!=0&&movieDetailsBean.getServerbuy()==1 ){
          mNumber=info.getPlayerpos();
      }else{
          mNumber=1;
      }
    mBangumiSelectionRecycler.setHasFixedSize(false);
    mBangumiSelectionRecycler.setNestedScrollingEnabled(false);
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this,
        LinearLayoutManager.HORIZONTAL, false);
//    mLinearLayoutManager.setReverseLayout(true);
    mBangumiSelectionRecycler.setLayoutManager(mLinearLayoutManager);
 movieListDetailsSelectionAdapter
        = new MovieListDetailsSelectionAdapter(mBangumiSelectionRecycler, movieDetailsSouresList,movieDetailsBean);
    mBangumiSelectionRecycler.setAdapter(movieListDetailsSelectionAdapter);
    //剧集选择所在位置   观看记忆
    movieListDetailsSelectionAdapter.notifyItemForeground(mNumber-1);
    mBangumiSelectionRecycler.scrollToPosition(mNumber-1);
    movieListDetailsSelectionAdapter.setOnItemClickListener((position, holder) -> {
                if(movieDetailsBean.getIsplay().equals("0")){//地区访问限制
                    ToastUtil.LongToast("中国大陆地区的用户无法使用");
                    return;
                }


        if(movieDetailsSouresList.get(position).getIstry()!=0){
            VideoPlayerActivity.launch(MovieDetailsActivity.this,
                    movieDetailsSouresList.get(position).getVideoid(), movieDetailsBean.getName(), movieDetailsBean.getId(), position + 1 + "", movieDetailsBean.getType(),movieDetailsBean);
        }else{
            if(movieDetailsBean.getIslogin()==0){//未登录用户去登录页面
                startActivity(new Intent(MovieDetailsActivity.this, LoginActivity.class));
            }else {
                if (movieDetailsBean.getServerbuy() == 0) {//未支付用户去支付页面
                    initDialog();
                } else {
                    VideoPlayerActivity.launch(MovieDetailsActivity.this,
                            movieDetailsSouresList.get(position).getVideoid(), movieDetailsBean.getName(), movieDetailsBean.getId(), position + 1 + "", movieDetailsBean.getType(),movieDetailsBean);
                }
            }
        }
    });
  if(totalEpisode>=DIVISIONVALUE) {
    initSeasonsRecycler();
  }
  }


  /**
   * 初始化番剧推荐recyclerView
   */
//  private void initRecommendRecycler() {
//
//    mBangumiRecommendRecycler.setHasFixedSize(false);
//    mBangumiRecommendRecycler.setNestedScrollingEnabled(false);
//    mBangumiRecommendRecycler.setLayoutManager(
//        new GridLayoutManager(MovieDetailsActivity.this, 3));
//    BangumiDetailsRecommendAdapter mBangumiDetailsRecommendAdapter
//        = new BangumiDetailsRecommendAdapter(
//        mBangumiRecommendRecycler, bangumiRecommends);
//    mBangumiRecommendRecycler.setAdapter(mBangumiDetailsRecommendAdapter);
//  }


  @Override
  public void initToolBar() {
    mToolbar.setTitle("");

    setSupportActionBar(mToolbar);
    ActionBar supportActionBar = getSupportActionBar();
    if (supportActionBar != null) {
      supportActionBar.setDisplayHomeAsUpEnabled(true);
    }

    //设置Toolbar的透明度
    mToolbar.setBackgroundColor(Color.argb(0, 251, 114, 153));

    //设置StatusBar透明
    SystemBarHelper.immersiveStatusBar(this);
    SystemBarHelper.setPadding(this, mToolbar);

    //获取顶部image高度和toolbar高度
    float imageHeight = getResources().getDimension(R.dimen.bangumi_details_top_layout_height);
    float toolBarHeight = getResources().getDimension(R.dimen.action_bar_default_height);

    mNestedScrollView.setNestedScrollingEnabled(true);
    mNestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

      @Override
      public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //根据NestedScrollView滑动改变Toolbar的透明度
        float offsetY = toolBarHeight - imageHeight;
        //计算滑动距离的偏移量
        float offset = 1 - Math.max((offsetY - scrollY) / offsetY, 0f);
        float absOffset = Math.abs(offset);
        //如果滑动距离大于1就设置完全不透明度
        if (absOffset >= 1) {
          absOffset = 1;
        }
        mToolbar.setBackgroundColor(Color.argb((int) (absOffset * 255), 251, 114, 153));
      }
    });
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    if (item.getItemId() == android.R.id.home) {
      onBackPressed();
    }

    return super.onOptionsItemSelected(item);
  }


//  @Override
//  public boolean onCreateOptionsMenu(Menu menu) {
//
//    getMenuInflater().inflate(R.menu.menu_bangumi_details, menu);
//    return true;
//  }


    private void  initDialog(){
        mDialog = new AlertDialog.Builder(this);
        mDialog.setMessage(getString(R.string.moviedetails_vipinfo));
        mDialog.setNegativeButton(getString(R.string.moviedetails_later), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        mDialog.setPositiveButton(getString(R.string.moviedetails_openvip), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(MovieDetailsActivity.this, VipActivity.class));
            }
        }).setCancelable(false).create().show();
    }
  @Override
  protected void onResume() {
    super.onResume();
//    initSelectionRecycler();
      if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){
          set("en");
      }else{
          set("ch");

      }
      mPlayVideo.setEnabled(false);
      showProgressBar();
      loadData();
  }

  @Override
  public void showProgressBar() {

    mCircleProgressView.setVisibility(View.VISIBLE);
    mCircleProgressView.spin();
    mDetailsLayout.setVisibility(View.GONE);
  }


  @Override
  public void hideProgressBar() {

    mCircleProgressView.setVisibility(View.GONE);
    mCircleProgressView.stopSpinning();
    mDetailsLayout.setVisibility(View.VISIBLE);
  }


  public static void launch(Activity activity, String mId) {

    Intent mIntent = new Intent(activity, MovieDetailsActivity.class);
    Bundle bundle = new Bundle();
    bundle.putString("movie_details_id", mId);
    mIntent.putExtras(bundle);
    activity.startActivity(mIntent);
  }
}
