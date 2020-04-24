package com.china.cibn.bangtvmobile.bangtv.module;

import com.china.cibn.bangtvmobile.BangtvApp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

/**
 * Created by hcc on 16/8/4 21:18
 *
 * <p/>
 * Toast工具类
 */
public class ToastUtil {

  public static void showShort(Context context, String text) {

    showToastNoRepeat(BangtvApp.getInstance(), text, Toast.LENGTH_SHORT);
//    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
  }


  public static void showShort(Context context, int resId) {

    showToastNoRepeatInt(BangtvApp.getInstance(), resId, Toast.LENGTH_SHORT);
//    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
  }


  public static void showLong(Context context, String text) {

    showToastNoRepeat(BangtvApp.getInstance(), text, Toast.LENGTH_LONG);
//    Toast.makeText(context, text, Toast.LENGTH_LONG).show();
  }


  public static void showLong(Context context, int resId) {

    showToastNoRepeatInt(BangtvApp.getInstance(), resId, Toast.LENGTH_LONG);
//    Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
  }


  public static void LongToast(final String text) {

    showToastNoRepeat(BangtvApp.getInstance(), text, Toast.LENGTH_LONG);
//    new Handler(Looper.getMainLooper()).post(() -> Toast.
//        makeText(BangtvApp.getInstance(), text, Toast.LENGTH_LONG).show());
  }


  public static void LongToast(final int stringId) {

    showToastNoRepeatInt(BangtvApp.getInstance(), stringId, Toast.LENGTH_LONG);
//    new Handler(Looper.getMainLooper()).post(() -> Toast.
//        makeText(BangtvApp.getInstance(), stringId, Toast.LENGTH_LONG).show());
  }


  public static void ShortToast(final String text) {

    showToastNoRepeat(BangtvApp.getInstance(), text, Toast.LENGTH_SHORT);
//    new Handler(Looper.getMainLooper()).post(() -> Toast.
//        makeText(BangtvApp.getInstance(), text, Toast.LENGTH_SHORT).show());
  }


  public static void ShortToast(final int stringId) {

    showToastNoRepeatInt(BangtvApp.getInstance(), stringId, Toast.LENGTH_SHORT);
//    new Handler(Looper.getMainLooper()).post(() -> Toast.
//        makeText(BangtvApp.getInstance(), stringId, Toast.LENGTH_SHORT).show());
  }



  private static Toast mToast = null;

  public static void showToastNoRepeat(Context context, String text, int duration) {
    if (mToast == null) {
      mToast = Toast.makeText(context, text, duration);
    } else {
      mToast.setText(text);
      mToast.setDuration(duration);
    }

    mToast.show();
  }

  public static void showToastNoRepeatInt(Context context, int stringId, int duration) {
    if (mToast == null) {
      mToast = Toast.makeText(context, stringId, duration);
    } else {
      mToast.setText(stringId);
      mToast.setDuration(duration);
    }

    mToast.show();
  }
}
