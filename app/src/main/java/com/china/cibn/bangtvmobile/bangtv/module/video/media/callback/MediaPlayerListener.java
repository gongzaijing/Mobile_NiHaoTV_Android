package com.china.cibn.bangtvmobile.bangtv.module.video.media.callback;

/**
 * Created by hcc on 16/8/31 21:42
 *
 * <p/>
 * 视频控制回调接口
 */
public interface MediaPlayerListener {

  void start();

  void pause();

  int getDuration();

  int getCurrentPosition();

  void seekTo(long pos);

  boolean isPlaying();

  int getBufferPercentage();

  boolean canPause();
}
