/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.utils;

import java.util.Locale;

public class HourFormatter {
  public static final int TWELVE_HOUR_MODE = 0;
  public static final int TWENTY_FOUR_HOUR_MODE = 1;

  public static String getFormattedHour(int dataID, int mode) {
    //2019010123
    //Y-2019 M-01 D-01 H-23
    int hour = dataID - (dataID / 100) * 100;
    if (mode == TWELVE_HOUR_MODE) {
      return hour > 12 ? String.format(Locale.getDefault(), "%02d-%02dPM",
       hour - 12, hour - 11) : String.format(Locale.getDefault(),
       "%02d-%02dAM", hour, hour + 1);
    } else {
      return String.format(Locale.getDefault(), "%02d-%02d",
       hour, (hour + 1) > 23 ? 0 : hour + 1);
    }
  }

  public static int getHour(int dataID) {
    int hour = dataID - (dataID / 100) * 100;
    return hour;
  }

}
