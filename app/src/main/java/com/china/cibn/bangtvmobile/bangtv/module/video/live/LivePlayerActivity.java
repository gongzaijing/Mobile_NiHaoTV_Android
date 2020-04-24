package com.china.cibn.bangtvmobile.bangtv.module.video.live;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.china.cibn.bangtvmobile.BangtvApp;
import com.china.cibn.bangtvmobile.R;
import com.china.cibn.bangtvmobile.bangtv.apapter.liveplay.LiveFirstListAdapter;
import com.china.cibn.bangtvmobile.bangtv.apapter.liveplay.LiveSecondListAdapter;
import com.china.cibn.bangtvmobile.bangtv.dal.PlayRecordHelpler;
import com.china.cibn.bangtvmobile.bangtv.entity.LiveListInfo;
import com.china.cibn.bangtvmobile.bangtv.entity.LivePlayURlInfo;
import com.china.cibn.bangtvmobile.bangtv.base.RxBaseActivity;
import com.china.cibn.bangtvmobile.bangtv.module.ToastUtil;
import com.china.cibn.bangtvmobile.bangtv.module.video.media.VideoPlayerView;
import com.china.cibn.bangtvmobile.bangtv.module.video.media.callback.VideoBackListener;
import com.china.cibn.bangtvmobile.bangtv.network.RetrofitHelper;
import com.china.cibn.bangtvmobile.bangtv.utils.AppGlobalConsts;
import com.china.cibn.bangtvmobile.bangtv.utils.ConstantUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by gzj on 2018/4/26
 *
 * <p/>
 * 直播视频播放界面
 */
public class LivePlayerActivity extends RxBaseActivity
        implements VideoBackListener, View.OnTouchListener{


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


    @BindView(R.id.geture_tv_volume_percentage)
    TextView geture_tv_volume_percentage;

    @BindView(R.id.geture_tv_bright_percentage)
    TextView geture_tv_bright_percentage;

    @BindView(R.id.gesture_iv_player_volume)
    ImageView gesture_iv_player_volume;

    @BindView(R.id.gesture_iv_player_bright)
    ImageView gesture_iv_player_bright;


    public static  RelativeLayout mediaController;

    @BindView(R.id.media_controller_controls)
    LinearLayout mMedia_controller;


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

    @BindView(R.id.video_loading_text)
    TextView mLoading;

    @BindView(R.id.clumnlist)
    ListView mClumnList;

    @BindView(R.id.channellist)
    ListView mChannelList;

    @BindView(R.id.re_vip_isdis)
    RelativeLayout mVipIsdis;


    private AnimationDrawable mLoadingAnim;

    public static  String mId;

    private String title;

    private int LastPosition = 0;

    private String startText = "";

    private List<LiveListInfo> liveListInfos;

    private List<LiveListInfo> columnlist;

    private List<LiveListInfo> channellist;

    /** 视频窗口的宽和高 */
    private int playerWidth;

    private GestureDetector gestureDetector;

    private AudioManager audiomanager;

    private int maxVolume, currentVolume;

    private static final float STEP_VOLUME = 2f;// 协调音量滑动时的步长，避免每次滑动都改变，导致改变过快

    private boolean firstScroll = false;// 每次触摸屏幕后，第一次scroll的标志

    private int GESTURE_FLAG = 0;// 1,调节进度，2，调节音量,3.调节亮度

    private static final int GESTURE_MODIFY_PROGRESS = 1;

    private static final int GESTURE_MODIFY_VOLUME = 2;

    private static final int GESTURE_MODIFY_BRIGHT = 3;


    private long exitTime=0;


    private  boolean isActive=true;

    private  Context mContext;


    boolean handler=false;

    boolean handler2=false;

    private String seasonId;


    private String mNumber;

    public static LivePlayerActivity instance;

    protected PlayRecordHelpler playrecord;// 播放记录


    private List<LivePlayURlInfo.DataBean.PlayBean> playUrlList;

    private LiveSecondListAdapter channelapter;

    private LiveFirstListAdapter columndpater;

    private int channelIndex = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_video_liveplayer_bangtv;
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
            title = intent.getStringExtra(ConstantUtil.EXTRA_TITLE);
            mTitle.setText(title+"    "+mNumber);
        }
        instance=this;
        mContext=this;
        liveListInfos = new ArrayList<>();
        playUrlList = new ArrayList<>();
        playrecord = new PlayRecordHelpler(this);
        columndpater = new LiveFirstListAdapter(this);
        channelapter = new LiveSecondListAdapter(this);
        columnlist = new ArrayList<>();
        channellist = new ArrayList<>();
        mBack.setOnClickListener(mBackClick);
        mLock.setOnClickListener(mLockListener);
        mTvSelect.setOnClickListener(mTvSelectListener);
        mTvSelect.setText(R.string.activity_video_liveplayer_select);
        mLoading.setText(R.string.video_loading);
        mediaController=(RelativeLayout)findViewById(R.id.media_actions);
        too_layout.requestFocus();
        too_layout.setLongClickable(true);
        too_layout.setOnTouchListener(this);
        gestureDetector = new GestureDetector(this, new MyOnGestureListener());
        audiomanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
        currentVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值

        /** 获取视频播放窗口的尺寸 */
        ViewTreeObserver viewObserver = too_layout.getViewTreeObserver();
        viewObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                too_layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                playerWidth = too_layout.getWidth();
            }
        });
        mBufferingIndicator.setVisibility(View.VISIBLE);
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
    public void refresh(int pos){
        playUrlList = new ArrayList<>();
        if(mPlayerView!=null){
            mPlayerView.stopPlayback();
        }

        //是否是会员频道
        if(channellist.get(pos).getIsOpen()==0){
            mVipIsdis.setVisibility(View.VISIBLE);
            if(mBufferingIndicator.getVisibility()==View.VISIBLE){
                mBufferingIndicator.setVisibility(View.GONE);
            }
        }else {
            mVipIsdis.setVisibility(View.GONE);
            playChannel(channellist.get(pos).getChannelId());
            mBufferingIndicator.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 锁屏按钮切换
     * */
    private View.OnClickListener mLockListener = v -> {
        updateLock();
    };
    private View.OnClickListener mTvSelectListener=view -> {
        if(mSelectDialog.getVisibility()==View.VISIBLE){
            mSelectDialog.setVisibility(View.GONE);
        }else{
            mSelectDialog.setVisibility(View.VISIBLE);
            mediaController.setVisibility(View.GONE);
            mLock.setVisibility(View.GONE);
        }
    };


    /**
     * 进返回監聽
     * */

    private View.OnClickListener mBackClick = v -> {
        StopPlayer();

    };

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
//
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
     * 获取视频数据
     */
    @Override
    public void loadData() {
        RetrofitHelper.getNoCacheAppAPI()
                .getLiveList(BangtvApp.versionCodeUrl)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieListBeans -> {
                    liveListInfos.addAll(movieListBeans);
                    _initColumnListView();
                }, throwable -> {
                });

    }

//   右侧列表
    private void _initColumnListView() {
        columnlist = _getCloumnList();
        columndpater.setData(columnlist);
        mClumnList.setAdapter(columndpater);
        columndpater.setSelector(0);
        columndpater.notifyDataSetChanged();
        mClumnList.setSelection(0);
        mClumnList.setOnItemClickListener(columnItemClickLinstener);

        channellist.addAll(liveListInfos);
        channelapter.setData(channellist);
        mChannelList.setAdapter(channelapter);
        channelapter.setSelector(getChannelPos());
        mChannelList.setSelection(getChannelPos());
        channelapter.notifyDataSetChanged();
        mChannelList.setOnItemClickListener(channelItemClickLinstener);
        if(channellist.get(getChannelPos()).getIsOpen()==0){
            mVipIsdis.setVisibility(View.VISIBLE);
            if(mBufferingIndicator.getVisibility()==View.VISIBLE){
                mBufferingIndicator.setVisibility(View.GONE);
            }
        }else {
            mVipIsdis.setVisibility(View.GONE);
            playChannel(mId);
            mBufferingIndicator.setVisibility(View.VISIBLE);
        }


    }

    //根据id获取当前位置
    private int getChannelPos() {
        if (liveListInfos != null && channellist != null
                && channellist.size() > 0) {
            for (int i = 0; i < channellist.size(); i++) {
                if (channellist.get(i).getChannelId()
                        .equals(mId)) {
                    channelIndex = i;
                    break;
                }
            }

        }
        return channelIndex;
    }

    private AdapterView.OnItemClickListener columnItemClickLinstener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
                columndpater.setSelector(pos);
                columndpater.notifyDataSetChanged();
                _setChannelListView(columnlist.get(pos).getGroupid());

            }

        };
    private AdapterView.OnItemClickListener channelItemClickLinstener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View v, int pos, long arg3) {
            if(playUrlList!=null){
                playUrlList.clear();
            }
            mTitle.setText(channellist.get(pos).getChannelName()+"    "+channellist.get(pos).getNo());
            channelapter.setSelector(pos);
            channelapter.notifyDataSetChanged();
            refresh(pos);

                }


    };

    private void _setChannelListView(String groupId) {
        if (liveListInfos != null && liveListInfos.size() > 0) {
            channellist.clear();
            if (groupId.equals("all")) {
                channellist.addAll(liveListInfos);
            } else {
                for (int j = 0; j < liveListInfos.size(); j++) {
                    if (groupId.equals(liveListInfos.get(j).getGroupid())) {
                        channellist.add(liveListInfos.get(j));
                    }
                }
            }
            channelapter.setData(channellist);
            channelapter.notifyDataSetChanged();
        }
    }

private void playChannel(String channelId){
    RetrofitHelper.getNoCacheAppAPI()
            .getLiveUrl(BangtvApp.versionCodeUrl,channelId,"cdn","1")
            .compose(bindToLifecycle())
            .doOnSubscribe(this::showProgressBar)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(livelisturl -> {
                playUrlList.addAll(livelisturl.getData().get(0).getPlay());
                mPlayerView.setVideoURI(Uri.parse(playUrlList.get(0).getUrl()));
                mPlayerView.setOnPreparedListener(mp -> {
                    mBufferingIndicator.setVisibility(View.GONE);
                    startText = getString(R.string.LivePlayerActivity_InitPlayer) + getString(R.string.LivePlayerActivity_InitPlayerComplete) + "\n" + getString(R.string.LivePlayerActivity_InitPlayerBuff);
                    mPrepareText.setText(startText);
                    mVideoPrepareLayout.setVisibility(View.GONE);
                    if(isActive){
                        toggleMediaControlsVisiblity(5000);
                    }else{
                        toggleLockVisiblity(5000);
                    }
                });
                mPlayerView.start();

            }, throwable -> {
                Log.i("","");
                startText = getString(R.string.LivePlayerActivity_InitPlayer) + getString(R.string.LivePlayerActivity_InitPlayerFail) + "\n" + getString(R.string.LivePlayerActivity_InitPlayerBuff);
                mPrepareText.setText(startText);
                startText = getString(R.string.LivePlayerActivity_InitPlayer) + getString(R.string.LivePlayerActivity_InitPlayerFail) + "\n" + throwable.getMessage();
                mPrepareText.setText(startText);
                hideProgressBar();

            });
}
    private List<LiveListInfo> _getCloumnList() {
        if (liveListInfos != null && liveListInfos.size() > 0) {
            LiveListInfo bean = new LiveListInfo();
            bean.setGroupid("all");
            bean.setGroupname(String.valueOf(getString(R.string.LivePlayerActivity_AllChannal)));
            columnlist.add(bean);
            for (int i = 0; i < liveListInfos.size(); i++) {
                if (_findCloumn(liveListInfos.get(i))) {
                    columnlist.add(liveListInfos.get(i));
                }
            }
        }
        return columnlist;
    }
    private boolean _findCloumn(LiveListInfo itemBean) {
        if (columnlist.isEmpty())
            return true;
        for (int j = 0; j < columnlist.size(); j++) {
            String jgroupId = columnlist.get(j).getGroupid();
            if (itemBean.getGroupid().equals(jgroupId)) {
                return false;
            }
        }
        return true;
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

            } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                Log.i("Buffer_i", "MEDIA_INFO_BUFFERING_END");
                if (mBufferingIndicator != null) {
                    mBufferingIndicator.setVisibility(View.GONE);
                }
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
            mPlayerView.pause();
        }
    };
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

        }        hideBottomUIMenu();
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
                if(mSelectDialog.getVisibility()==View.VISIBLE){
                    mSelectDialog.setVisibility(View.GONE);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        // 处理手势结束
        if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            GESTURE_FLAG = 0;// 手指离开屏幕后，重置调节音量或进度的标志
            gesture_volume_layout.setVisibility(View.GONE);
            gesture_bright_layout.setVisibility(View.GONE);
           currentVolume = -1;
        }
        return gestureDetector.onTouchEvent(motionEvent);
    }



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

            if(isActive){
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

                    if (Math.abs(distanceX) >= Math.abs(distanceY)) {
                        gesture_volume_layout.setVisibility(View.GONE);
                        gesture_bright_layout.setVisibility(View.GONE);
                        GESTURE_FLAG = GESTURE_MODIFY_PROGRESS;
                    } else {
                        if (mOldX > playerWidth * 3.0 / 5) {// 音量
                            gesture_volume_layout.setVisibility(View.VISIBLE);
                            gesture_bright_layout.setVisibility(View.GONE);
                            GESTURE_FLAG = GESTURE_MODIFY_VOLUME;
                        } else if (mOldX < playerWidth * 2.0 / 5) {// 亮度
                            gesture_bright_layout.setVisibility(View.VISIBLE);
                            gesture_volume_layout.setVisibility(View.GONE);
                            GESTURE_FLAG = GESTURE_MODIFY_BRIGHT;
                        }
                    }
                }
                // 如果每次触摸屏幕后第一次scroll是调节音量，那之后的scroll事件都处理音量调节，直到离开屏幕执行下一次操作
                else if (GESTURE_FLAG == GESTURE_MODIFY_VOLUME) {
                    float percent = deltaY /mPlayerView.getHeight();
                        onVolumeSlide(percent);
                }
                // 如果每次触摸屏幕后第一次scroll是调节亮度，那之后的scroll事件都处理亮度调节，直到离开屏幕执行下一次操作
                else if (GESTURE_FLAG == GESTURE_MODIFY_BRIGHT) {
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
//        updatePausePlay();
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
            ToastUtil.ShortToast(R.string.LivePlayerActivity_AgainExit);
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
        if(mPlayerView!=null){
            mPlayerView.stopPlayback();
            mPlayerView=null;
        }
        LivePlayerActivity.this.finish();
    }


    public static void launch(Activity activity, String cid, String title, String selectid, String number) {
        Intent mIntent = new Intent(activity, LivePlayerActivity.class);
        mIntent.putExtra("midVideo", cid);
        mIntent.putExtra("mSelectId",selectid);
        mIntent.putExtra("mNumber",number);
        mIntent.putExtra(ConstantUtil.EXTRA_TITLE, title);
        activity.startActivity(mIntent);
    }
}