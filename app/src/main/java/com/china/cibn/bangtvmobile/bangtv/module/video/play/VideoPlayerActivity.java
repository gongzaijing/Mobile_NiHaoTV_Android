package com.china.cibn.bangtvmobile.bangtv.module.video.play;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.VideoSelectListAdapter;
import com.china.cibn.bangtvmobile.bangtv.dal.MPlayRecordInfo;
import com.china.cibn.bangtvmobile.bangtv.dal.PlayRecordHelpler;
import com.china.cibn.bangtvmobile.bangtv.entity.MovieDetailsInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.VideoUrlInfo;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.common.SplashActivity;
import com.china.cibn.bangtvmobile.bangtv.module.user.maillogin.LoginActivity;
import com.china.cibn.bangtvmobile.bangtv.module.user.vip.VipActivity;
import com.china.cibn.bangtvmobile.bangtv.module.video.media.VideoPlayerView;
import com.china.cibn.bangtvmobile.bangtv.module.video.media.callback.VideoBackListener;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalVars;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;
import com.china.cibn.bangtvmobile.bangtv.utils.DateTools;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.utils.DownLoadApk;
import com.facebook.stetho.common.LogUtil;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by gzj on 2018/4/26
 *
 * <p/>
 * 视频播放界面
 */
public class VideoPlayerActivity extends RxBaseActivity
        implements VideoBackListener, View.OnTouchListener  {


    @BindView(R.id.playerView)
    VideoPlayerView mPlayerView;

    @BindView(R.id.buffering_indicator)
    View mBufferingIndicator;

    @BindView(R.id.video_start)
    RelativeLayout mVideoPrepareLayout;

//    @BindView(R.id.bili_anim)
//    ImageView mAnimImageView;

    @BindView(R.id.video_start_info)
    TextView mPrepareText;

    @BindView(R.id.too_layout)
    RelativeLayout too_layout;

    @BindView(R.id.gesture_volume_layout)
    RelativeLayout gesture_volume_layout;

    @BindView(R.id.gesture_bright_layout)
    RelativeLayout gesture_bright_layout;

    @BindView(R.id.gesture_progress_layout)
    RelativeLayout gesture_progress_layout;

    @BindView(R.id.geture_tv_progress_time)
    TextView geture_tv_progress_time;

    @BindView(R.id.geture_tv_volume_percentage)
    TextView geture_tv_volume_percentage;

    @BindView(R.id.geture_tv_bright_percentage)
    TextView geture_tv_bright_percentage;

    @BindView(R.id.gesture_tv_progress)
   TextView gesture_tv_progress;

    @BindView(R.id.gesture_iv_player_volume)
    ImageView gesture_iv_player_volume;

    @BindView(R.id.gesture_iv_player_bright)
    ImageView gesture_iv_player_bright;


    public static  RelativeLayout mediaController;

    @BindView(R.id.media_controller_seekbar)
    SeekBar mSeekBar;

    @BindView(R.id.media_controller_controls)
    LinearLayout mMedia_controller;

    @BindView(R.id.media_controller_time_current)
    TextView mCurrentTime;

    @BindView(R.id.media_controller_time_total)
    TextView mTotalTime;

    @BindView(R.id.media_controller_play_pause)
    ImageButton mBtnPause;

    @BindView(R.id.media_controller_back)
    ImageView mBack;

    @BindView(R.id.media_controller_title)
    TextView mTitle;

    @BindView(R.id.media_lock)
    ImageView mLock;

    @BindView(R.id.select_dialog)
    RelativeLayout mSelectDialog;

    @BindView(R.id.select_tv)
    TextView mTvSelect;

    @BindView(R.id.select_text)
    TextView mTextSelect;

    @BindView(R.id.recycle_select_dialog)
    RecyclerView mReclerSelectDialog;

    @BindView(R.id.tv_test_msg)
    TextView mTestMsg;

    @BindView(R.id.video_loading_text)
    TextView mLoading;

    private AnimationDrawable mLoadingAnim;

    public static  String mId;

    private String title;

    private int LastPosition = 0;

    private String startText = "";

    private List<VideoUrlInfo.DataBean.PlayBean> movieDetailsBean;

    /** 视频窗口的宽和高 */
    private int playerWidth,playerHeight;

    /** 视频播放时间,视频播放的总时长 */
    private int playingTime=0, videoTotalTime=0;


    private GestureDetector gestureDetector;

    private AudioManager audiomanager;

    private int maxVolume, currentVolume=-1;

    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快

    private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志

    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量,3.调节亮度

    private static final int GESTURE_MODIFY_PROGRESS = 1;

    private static final int GESTURE_MODIFY_VOLUME = 2;

    private static final int GESTURE_MODIFY_BRIGHT = 3;


    private long exitTime=0;

    private boolean isSeekBarChanging;

    private Timer timer;

    private  boolean isActive=true;

  private  Context mContext;

    private int screenWidthPixels;


    boolean handler=false;

    boolean handler2=false;

    private String seasonId;

    private  MovieDetailsInfo movieDetailsInfo;

    private VideoSelectListAdapter mAdapter;

    private String mNumber;

    public static VideoPlayerActivity instance;

    private boolean toSeek;

    private boolean volumeControl;

    private long newPosition = -1;

    private static final int MESSAGE_SEEK_NEW_POSITION = 3;

    private static final int MESSAGE_HIDE_CENTER_BOX = 4;

    protected PlayRecordHelpler playrecord;// 播放记录

    private int ipos=0;

    private boolean iscomplete=false;

    private String mType;

    private ArrayList<MPlayRecordInfo> allFilmList;

   private GridLayoutManager mLayoutManager;

   private boolean isBuffer=false;

    private AlertDialog.Builder mDialog;

    private  int totalEpisode; // 总集数

    private  int NumText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_player_bangtv;
    }

    @Override
    public String setPageName() {
        return getClass().getSimpleName();
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){
            set("en");
        }else{
            set("ch");

        }

        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getStringExtra("midVideo");
            seasonId=intent.getStringExtra("mSelectId");
            mNumber=intent.getStringExtra("mNumber");
            mType=intent.getStringExtra("isType");
            title = intent.getStringExtra(ConstantUtil.EXTRA_TITLE);
            if(!TextUtils.isEmpty(mType)&&!mType.equals("MOVIECATG")) {
                mTitle.setText(title + "    " + mNumber);
            }else{
                mTitle.setText(title);
                mTvSelect.setVisibility(View.GONE);
            }
            ipos=Integer.parseInt(mNumber);
        }
        instance=this;
        mContext=this;
        movieDetailsBean = new ArrayList<>();
        movieDetailsInfo = new MovieDetailsInfo();
        playrecord = new PlayRecordHelpler(this);
        mSeekBar.setOnSeekBarChangeListener(new MySeekBar());
        mBtnPause.setOnClickListener(mPauseListener);
        mBack.setOnClickListener(mBackClick);
        mLock.setOnClickListener(mLockListener);
        mTvSelect.setOnClickListener(mTvSelectListener);
        mTvSelect.setText(R.string.activity_video_player_select);
        mTextSelect.setText(R.string.activity_video_player_select);
        mLoading.setText(R.string.video_loading);
        mReclerSelectDialog.scrollToPosition(Integer.parseInt(mNumber));
        mediaController=(RelativeLayout)findViewById(R.id.media_actions);
        too_layout.requestFocus();
        too_layout.setLongClickable(true);
        too_layout.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyOnGestureListener());
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        screenWidthPixels = this.getResources().getDisplayMetrics().widthPixels;
        /** 获取视频播放窗口的尺寸 */
        ViewTreeObserver viewObserver = too_layout.getViewTreeObserver();
        viewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                too_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                playerWidth = too_layout.getWidth();
                playerHeight = too_layout.getHeight();
            }
        });
        mBufferingIndicator.setVisibility(View.VISIBLE);
        getTotalEInfo();
        initMediaPlayer();
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

public void refresh(String id,String seasonid,String number,String _title){
    this.mId=id;
    this.seasonId=seasonid;
    if(mNumber.equals(number)){
        return;
    }else{
        this.mNumber=number;
        ipos=Integer.parseInt(mNumber);
    }
    this.title=_title;
    mTitle.setText(title+"    "+mNumber);
    stopTimer();
    movieDetailsBean = new ArrayList<>();
    movieDetailsInfo = new MovieDetailsInfo();
    if(mSelectDialog.getVisibility()==View.VISIBLE) {
        mSelectDialog.setVisibility(View.GONE);
    }
    if(mPlayerView!=null){
        mPlayerView.stopPlayback();
    }

    mBufferingIndicator.setVisibility(View.VISIBLE);
    loadData();

}
    /**
     * 暂停按钮切换
     * */
    private View.OnClickListener mPauseListener = v -> {
        doPauseResume();
        mediaController.setVisibility(View.VISIBLE);
        mLock.setVisibility(View.VISIBLE);
        mediaController.setVisibility(View.VISIBLE);

    };
    /**
     * 锁屏按钮切换
     * */
    private View.OnClickListener mLockListener = v -> {
        updateLock();
    };
    private View.OnClickListener mTvSelectListener=view -> {
        try {
                mAdapter.notifyItemForeground(ipos);
                mReclerSelectDialog.scrollToPosition(ipos);
                if (mSelectDialog.getVisibility() == View.VISIBLE) {
                    mSelectDialog.setVisibility(View.GONE);
                } else {
                    mSelectDialog.setVisibility(View.VISIBLE);
                    mediaController.setVisibility(View.GONE);
                    mLock.setVisibility(View.GONE);
                }
        }catch (Exception e){
            LogUtil.d(e+"");
        }

};


    /**
     * 进返回監聽
     * */

    private View.OnClickListener mBackClick = v -> {
        StopPlayer();

    };


    /**
     * 进度条监听
     * */
    public class MySeekBar implements SeekBar.OnSeekBarChangeListener {

        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
           mCurrentTime.setText(DateTools.generateTime(progress));
            if(progress>=AppGlobalVars.ISTRY_TIME&&movieDetailsInfo.getServerbuy()==0&&totalEpisode<=1){
                if(mPlayerView.isPlaying()){
                    mPlayerView.stopPlayback();
                }
                getDialog();
            }

        }

        /*滚动时,应当暂停后台定时器*/
        public void onStartTrackingTouch(SeekBar seekBar) {
            mediaController.setVisibility(View.VISIBLE);
            isSeekBarChanging = true;
        }
        /*滑动结束后，重新设置值*/
        public void onStopTrackingTouch(SeekBar seekBar) {
            isSeekBarChanging = false;
            mPlayerView.seekTo(seekBar.getProgress());
        }
    }

    public  void initMediaPlayer() {
        //配置播放器
        too_layout.setLongClickable(true);
        too_layout.setOnTouchListener(this);
        mPlayerView.requestFocus();
        mPlayerView.setMediaBufferingIndicator(mBufferingIndicator);
        mPlayerView.setOnInfoListener(onInfoListener);
        mPlayerView.setOnSeekCompleteListener(onSeekCompleteListener);
        mPlayerView.setOnCompletionListener(onCompletionListener);
        mPlayerView.setOnControllerEventsListener(onControllerEventsListener);
        loadData();
    }


    /**
     * 初始化加载动画
     */
    private void initAnimation() {

//        mVideoPrepareLayout.setVisibility(View.VISIBLE);
//        mLoadingAnim = (AnimationDrawable) mAnimImageView.getBackground();
//        mLoadingAnim.start();
    }


    @Override
    public void initToolBar() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setBackgroundDrawable(null);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    /**
     * 获取视频数据以及解析弹幕
     */
    @Override
    public void loadData() {
        iscomplete = false;
        RetrofitHelper.getNoCacheAppAPI()
                .getVideoUrl(BangtvApp.versionCodeUrl,mId,"cdn", "1")
                .compose(bindToLifecycle())
                .doOnSubscribe(this::showProgressBar)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieListBeans -> {
                    movieDetailsBean.addAll(movieListBeans.getData().get(0).getPlay());
                    mPlayerView.setVideoURI(Uri.parse(movieDetailsBean.get(0).getUrl()));
                    mPlayerView.setOnPreparedListener(mp -> {
                        mBufferingIndicator.setVisibility(View.GONE);
                        mBtnPause.setImageResource(R.drawable.bili_player_play_can_pause);
                        startText = getString(R.string.VideoPlayerActivity_InitPlayer) + getString(R.string.VideoPlayerActivity_InitPlayerComplete) + "\n" + getString(R.string.VideoPlayerActivity_InitPlayerBuff);
                        mPrepareText.setText(startText);
                        mSeekBar.setMax(mPlayerView.getDuration());
                        videoTotalTime=mPlayerView.getDuration();
                        mTotalTime.setText(DateTools.generateTime(mPlayerView.getDuration()));
                        mVideoPrepareLayout.setVisibility(View.GONE);
                        isBuffer=true;
                        //程序启动时，初始化并发送消息
                        if(isActive){
                            toggleMediaControlsVisiblity(5000);
                        }else{
                            toggleLockVisiblity(5000);
//                            toLockVisiblity(5000);
                        }
                    });
                    mPlayerView.start();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if(!isSeekBarChanging&& !TextUtils.isEmpty(mPlayerView.getCurrentPosition()+"")&&mPlayerView.getCurrentPosition()!=0){
                                mSeekBar.setProgress(mPlayerView.getCurrentPosition());
                            }
                            }
                    },0,50);

                }, throwable -> {
                    Log.i("","");
                    startText = getString(R.string.VideoPlayerActivity_InitPlayer) + getString(R.string.VideoPlayerActivity_InitPlayerFail) + "\n" + getString(R.string.VideoPlayerActivity_InitPlayerBuff);
                    mPrepareText.setText(startText);
                    startText = getString(R.string.VideoPlayerActivity_InitPlayer) + getString(R.string.VideoPlayerActivity_InitPlayerFail) + "\n" + throwable.getMessage();
                    mPrepareText.setText(startText);
                    hideProgressBar();

                });
    }

private void getTotalEInfo() {
    Intent intent = getIntent();
    if (intent != null) {
        String mMovieDetailsInfo = intent.getStringExtra("Mdetailsinfo");
        movieDetailsInfo = new Gson().fromJson(mMovieDetailsInfo, MovieDetailsInfo.class);
    }
    totalEpisode = movieDetailsInfo.getSources().size();
    for (int i = 0; i < movieDetailsInfo.getSources().size(); i++) {
        if (movieDetailsInfo.getSources().get(i).getIstry() == 1) {
            NumText++;
        }
    }
    if (movieDetailsInfo.getServerbuy() == 0) {
        if (totalEpisode <= 1) {
            mTestMsg.setText(getString(R.string.VideoPlayerActivity_FreeLook) + AppGlobalVars.ISTRY_TIME / 60 / 1000 + getString(R.string.VideoPlayerActivity_FreeLookMin));
        } else {
            mTestMsg.setText(getString(R.string.VideoPlayerActivity_FreeLook) + NumText + getString(R.string.VideoPlayerActivity_FreeLookPart));
            NumText = 0;
        }

    } else {
        mTestMsg.setVisibility(View.GONE);
    }

    if (movieDetailsInfo.getSources().size() > 1) {
        mTvSelect.setVisibility(View.VISIBLE);
        if (movieDetailsInfo.getSources().get(0).getVolumncount().length() < 4) {
            mLayoutManager = new GridLayoutManager(this, 5);
        } else {
            mLayoutManager = new GridLayoutManager(this, 3);
        }
        mReclerSelectDialog.setLayoutManager(mLayoutManager);
        mAdapter = new VideoSelectListAdapter(this, mReclerSelectDialog, movieDetailsInfo);
        mReclerSelectDialog.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

}
    /**
     * 视频缓冲事件回调
     */
    private IMediaPlayer.OnInfoListener onInfoListener = new IMediaPlayer.OnInfoListener() {

        @Override
        public boolean onInfo(IMediaPlayer mp, int what, int extra) {

            if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                if (mBufferingIndicator != null) {
                    mBufferingIndicator.setVisibility(View.VISIBLE);
                }
                mBtnPause.setImageResource(R.drawable.bili_player_play_can_play);

            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                Log.i("Buffer_i", "MEDIA_INFO_BUFFERING_END");
                if (mBufferingIndicator != null) {
                    mBufferingIndicator.setVisibility(View.GONE);
                }
                mBtnPause.setImageResource(R.drawable.bili_player_play_can_pause);
              if(mediaController.getVisibility()==View.VISIBLE){
                  if(isActive){
                      mediaController.setVisibility(View.GONE);
                      mLock.setVisibility(View.GONE);
                  }else{
                      mLock.setVisibility(View.GONE);
                  }


              }
            }
            return true;
        }
    };

    /**
     * 视频跳转事件回调
     */
    private IMediaPlayer.OnSeekCompleteListener onSeekCompleteListener
            = new IMediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(IMediaPlayer mp) {

        }
    };

    /**
     * 视频播放完成事件回调
     */
    private IMediaPlayer.OnCompletionListener onCompletionListener
            = new IMediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(IMediaPlayer mp) {
            iscomplete=true;
            mPlayerView.pause();
            if (movieDetailsInfo != null && movieDetailsInfo.getSources() != null
                    && movieDetailsInfo.getSources().size() > 0
                    && ipos < movieDetailsInfo.getSources().size() - 1) {
                if(movieDetailsInfo.getSources().get(ipos).getIstry()==1|movieDetailsInfo.getServerbuy()==1){
                    nextPlayer(true);
                }else{
                    getDialog();
                }
            }else{
                StopPlayer();
            }
        }
    };
private void nextPlayer(boolean isNext){
        if(isNext){
            refresh(movieDetailsInfo.getSources().get(ipos).getId(),movieDetailsInfo.getId(),ipos+1+"",movieDetailsInfo.getName());

}
    }
    /**
     * 控制条控制状态事件回调
     */
    private VideoPlayerView.OnControllerEventsListener onControllerEventsListener
            = new VideoPlayerView.OnControllerEventsListener() {

        @Override
        public void onVideoPause() {

        }


        @Override
        public void OnVideoResume() {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if(AppGlobalConsts.CHANNELS_ID.equals(AppGlobalConsts.CHANNEL_NIHAOTV_NETRANGE_EN)){
            set("en");
        }else{
            set("ch");

        }
        hideBottomUIMenu();
        if (mPlayerView != null && !mPlayerView.isPlaying()) {
            mPlayerView.seekTo(LastPosition);
        }
    }


    @Override
    protected void onPause() {

        super.onPause();
        if (mPlayerView != null) {
            LastPosition = mPlayerView.getCurrentPosition();
            mPlayerView.pause();
        }
    }




    @Override
    protected void onDestroy() {

        super.onDestroy();
        StopPlayer();
        if (mPlayerView != null && mPlayerView.isDrawingCacheEnabled()) {
            mPlayerView.destroyDrawingCache();
        }
    }




    /**
     * 退出界面回调
     */
    @Override
    public void back() {

        onBackPressed();
    }

    /*
     * 设置屏幕亮度
     * 0 最暗
     * 1 最亮
     */
    public void setBrightness(float brightness) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
        } else if (lp.screenBrightness < 0.1) {
            lp.screenBrightness = (float) 0.1;
        }
        getWindow().setAttributes(lp);
        float sb = lp.screenBrightness;
        geture_tv_bright_percentage.setText((int) Math.ceil(sb * 100) + "%");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch(motionEvent.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        // 处理手势结束
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            if(mSelectDialog.getVisibility()==View.VISIBLE){
                mSelectDialog.setVisibility(View.GONE);
                return true;
            }
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_bright_layout.setVisibility(View.GONE);
            endGesture();
        }
        return gestureDetector.onTouchEvent(motionEvent);
    }


    private void endGesture() {
        currentVolume = -1;;
        if (newPosition >= 0) {
            handlerSeek.removeMessages(MESSAGE_SEEK_NEW_POSITION);
            handlerSeek.sendEmptyMessage(MESSAGE_SEEK_NEW_POSITION);
        }
        handlerSeek.removeMessages(MESSAGE_HIDE_CENTER_BOX);
        handlerSeek.sendEmptyMessageDelayed(MESSAGE_HIDE_CENTER_BOX, 500);

    }
    private Handler handlerSeek = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_HIDE_CENTER_BOX:
                    gesture_progress_layout.setVisibility(View.GONE);
                    break;
                case MESSAGE_SEEK_NEW_POSITION:
                    if ( newPosition > 0) {
                        mPlayerView.seekTo((int) newPosition);
                        newPosition = -1;
                    }else{
                        mPlayerView.seekTo(1000);
                        newPosition = -1;
                    }
                    break;
            }
        }
    };

    class MyOnGestureListener extends GestureDetector.SimpleOnGestureListener {
    @Override
    public void onLongPress(MotionEvent motionEvent) {
        Log.i(
                "",""
        );
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if(isActive){
                doPauseResume();
            }else{
                toggleLockVisiblity(5000);
            }
             
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {

            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {

        if(isActive&&isBuffer){
            toggleMediaControlsVisiblity(5000);
        }else{
            toggleLockVisiblity(5000);
        }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            firstScroll = true;// 设定是触摸屏幕后第一次scroll的标志

            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.i(
                    "",""
            );
        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(isActive&&e1!=null){
                 float mOldX = e1.getX(), mOldY = e1.getY();
                float deltaX = mOldX - e2.getX();
                float deltaY = mOldY - e2.getY();
                if (firstScroll) {// 以触摸屏幕后第一次滑动为标准，避免在屏幕上操作切换混乱
                // 横向的距离变化大则调整进度，纵向的变化大则调整音量
                toSeek = Math.abs(distanceX) >= Math.abs(distanceY);
                  volumeControl = mOldX > screenWidthPixels * 0.5f;

                if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                    gesture_progress_layout.setVisibility(View.VISIBLE);
                    gesture_volume_layout.setVisibility(View.GONE);
                    gesture_bright_layout.setVisibility(View.GONE);
                    GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                } else {
                    if (mOldX > playerWidth * 3.0 / 5) {// 音量
                        gesture_volume_layout.setVisibility(View.VISIBLE);
                        gesture_bright_layout.setVisibility(View.GONE);
                        gesture_progress_layout.setVisibility(View.GONE);
                        GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                    } else if (mOldX < playerWidth * 2.0 / 5) {// 亮度
                        gesture_bright_layout.setVisibility(View.VISIBLE);
                        gesture_volume_layout.setVisibility(View.GONE);
                        gesture_progress_layout.setVisibility(View.GONE);
                        GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
                    }
                }
            }
            if(toSeek ){
                mediaController.setVisibility(View.VISIBLE);
                mLock.setVisibility(View.VISIBLE);
                onProgressSlide(-deltaX / mPlayerView.getWidth());
            }else {
                float percent = deltaY /playerHeight;
                if (volumeControl) {
                    onVolumeSlide(percent);
               } else {
                     gesture_iv_player_bright.setImageResource(R.drawable.souhu_player_bright);
                    final double FLING_MIN_DISTANCE = 0.5;
                    final double FLING_MIN_VELOCITY = 0.5;
                    if (distanceY > FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        setBrightness(10);
                    }
                    if (distanceY < FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        setBrightness(-10);
                    }
                }
            }
            firstScroll = false;// 第一次scroll执行完成，修改标志
            }
            return false;
        }
}


        /**
         * 滑动改变声音大小
         *
         * @param percent
         */
        private void onVolumeSlide(float percent) {
            if (currentVolume == -1) {
                currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVolume < 0)
                    currentVolume = 0;
            }
            int index = (int) (percent * maxVolume) + currentVolume;
            if (index > maxVolume)
                index = maxVolume;
            else if (index < 0)
                index = 0;

            // 变更声音
            audiomanager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);

            // 变更进度条
            int i = (int) (index * 1.0 / maxVolume * 100);
            String s = i + "%";
            gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_volume);

            if (i == 0) {
                gesture_iv_player_volume.setImageResource(R.drawable.souhu_player_silence);
            }
              geture_tv_volume_percentage.setText(s);
        }

/*
* 快进、快退
* */
    private void onProgressSlide(float percent) {
        long position = mPlayerView.getCurrentPosition();
        long duration = mPlayerView.getDuration();
        long deltaMax = Math.min(100 * 1000, duration - position);
        long delta = (long) (deltaMax * percent);

        newPosition = delta + position;
        if (newPosition > duration) {
            newPosition = duration;
        } else if (newPosition <= 0) {
            newPosition = 0;
            delta = -position;
        }
        int showDelta = (int) delta / 1000;
        if (showDelta != 0) {
            String text = showDelta > 0 ? ("+" + showDelta) : "" + showDelta;
                gesture_tv_progress.setText(text + "s");
            geture_tv_progress_time.setText(DateTools.millisToString(newPosition) + "/" + DateTools.millisToString(duration));
        }
    }

//    隐藏控制栏
    private void toggleMediaControlsVisiblity(int times) {

        if (mediaController.getVisibility()==View.GONE) {
            mediaController.setVisibility(View.VISIBLE);
            mLock.setVisibility(View.VISIBLE);
            if(handler==false) {
               handler = new Handler(new Handler.Callback() {
                   @Override
                   public boolean handleMessage(Message arg0) {
                       mediaController.setVisibility(View.GONE);
                       if(mLock!=null&&mLock.getVisibility()==View.VISIBLE){
                           mLock.setVisibility(View.GONE);
                       }
                       handler=false;
                       return false;
                   }
               }).sendEmptyMessageDelayed(0, 5000);
            }
        } else if(mediaController.getVisibility()==View.VISIBLE) {
            mediaController.setVisibility(View.GONE);
            mLock.setVisibility(View.GONE);

        }
    }
    private void toggleLockVisiblity(int times) {

        if (mLock.getVisibility()==View.GONE) {
            mLock.setVisibility(View.VISIBLE);
            if(handler2==false) {
                handler2  = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message arg0) {
                        mLock.setVisibility(View.GONE);
                        handler2=false;
                        return false;
                    }
                }).sendEmptyMessageDelayed(0, times);
            }
        } else if(mLock.getVisibility()==View.VISIBLE) {
            mLock.setVisibility(View.GONE);
        }
    }



    private void doPauseResume() {

        if (mPlayerView.isPlaying()) {
            mPlayerView.pause();
                mBtnPause.setImageResource(R.drawable.bili_player_play_can_play);
        } else {
            mPlayerView.start();
            mBtnPause.setImageResource(R.drawable.bili_player_play_can_pause);
        }

    }

    private void updateLock() {
        if (isActive) {
            mLock.setImageResource(R.drawable.ic_player_open_2);
            mediaController.setVisibility(View.GONE);
//            toggleLockVisiblity(5000);
            isActive = false;
        } else {
            mLock.setImageResource(R.drawable.ic_player_locked_2);
            toggleMediaControlsVisiblity(5000);
            isActive = true;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           exitApp();
        }
        return false;
    }
    /**
     * 双击退出App
     */
    private void exitApp() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            ToastUtil.ShortToast(getString(R.string.VideoPlayerActivity_AgainExit));
            exitTime = System.currentTimeMillis();
        } else {
            StopPlayer();
        }
    }
    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void StopPlayer(){
        clear();
        stopTimer();
        handlerSeek=null;
        if(mPlayerView!=null){
            mPlayerView.stopPlayback();
            mPlayerView=null;
        }
        VideoPlayerActivity.this.finish();
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void clear() {
        if (mPlayerView != null) {
            if (iscomplete) {
                playingTime = 0;
                videoTotalTime = 0;
                ipos = 0;
            } else {
                playingTime = mPlayerView.getCurrentPosition();
                videoTotalTime = mPlayerView.getDuration();
            }
if(movieDetailsInfo.getServerbuy()==1){
    saveRecordPoint();
}

        }
    }
    /**
     * 断点记录
     */
    protected void saveRecordPoint() {
        if (movieDetailsInfo == null || playrecord == null)
            return;
        MPlayRecordInfo minfo = new MPlayRecordInfo();
        minfo.setEpgId(movieDetailsInfo.getId());
        // fix by gz 2018/6/4 begin
        try {
            if (movieDetailsInfo.getSources().size() >=1) {
                minfo.setDetailsId(movieDetailsInfo.getSources().get(ipos-1).getId());
            } else {
                minfo.setDetailsId(movieDetailsInfo.getSources().get(ipos).getId());
            }
        }catch (Exception e){

        }

        // fix by gz 2018/6/4 end
        minfo.setType(movieDetailsInfo.getType());
        minfo.setPlayerName(movieDetailsInfo.getName());
        //如果没有缓冲完成，时间默认为00:00:00
        if (videoTotalTime > 0&&videoTotalTime!=314684416) {
            minfo.setTotalTime(videoTotalTime);
        }else{
            minfo.setTotalTime(0);
        }
        if(playingTime>0&&playingTime!=314684416){
            minfo.setPonitime(playingTime);
        }else{
            minfo.setPonitime(0);
        }
        if (movieDetailsInfo.getSources().size() > 1) {
            minfo.setPlayerpos(ipos);
            minfo.setVolumncount(Integer.parseInt(movieDetailsInfo.getSources().get(ipos-1).getVolumncount()));
        } else {
            minfo.setPlayerpos(-1);
        }
        allFilmList=playrecord.getAllPlayRecord();
        if(allFilmList.size()>=40){
            playrecord
                    .deletePlayRecordBy(allFilmList.get(39).getEpgId());
            Log.i("","");
        }
        minfo.setPicUrl(movieDetailsInfo.getPicurl());
        minfo.setDateTime(System.currentTimeMillis());
        minfo.setIsSingle(-1);// 在全屏中不更新单集多集状态
        minfo.setActionUrl("");
        playrecord.savePlayRecord(minfo);
    }
public  void getDialog(){
        if(mPlayerView.isPlaying()){
            mPlayerView.pause();
        }
    mDialog = new AlertDialog.Builder(this);
    mDialog.setTitle(getString(R.string.VideoPlayerActivity_FreeLookEnd));
    mDialog.setNegativeButton(getString(R.string.VideoPlayerActivity_Back), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            finish();
        }
    });
    mDialog.setPositiveButton(getString(R.string.VideoPlayerActivity_LogExit), new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            VideoPlayerActivity.this.finish();
            if(movieDetailsInfo.getIslogin()==0){
                startActivity(new Intent(VideoPlayerActivity.this, LoginActivity.class));
            }else{
                startActivity(new Intent(VideoPlayerActivity.this, VipActivity.class));
            }
        }
    }).setCancelable(false).create().show();
}
    public static void launch(Activity activity, String cid, String title, String selectid, String number,String type,MovieDetailsInfo _moviedetailsinfo) {
        Intent mIntent = new Intent(activity, VideoPlayerActivity.class);
        mIntent.putExtra("midVideo", cid);
        mIntent.putExtra("mSelectId",selectid);
        mIntent.putExtra("mNumber",number);
        mIntent.putExtra(ConstantUtil.EXTRA_TITLE, title);
        mIntent.putExtra("isType", type);
        mIntent.putExtra("Mdetailsinfo",  new Gson().toJson(_moviedetailsinfo));


        activity.startActivity(mIntent);
    }
    }