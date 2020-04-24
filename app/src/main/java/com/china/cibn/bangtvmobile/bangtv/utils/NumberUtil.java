package com.china.cibn.bangtvmobile.bangtv.utils;

/**
 * Created by hcc on 2016/10/11 14:35
 *
 */

public class NumberUtil {

  public static String converString(int num) {

    if (num < 100000) {
      return String.valueOf(num);
    }
    String unit = "万";
    double newNum = num / 10000.0;

    String numStr = String.format("%." + 1 + "f", newNum);
    return numStr + unit;
  }
}
