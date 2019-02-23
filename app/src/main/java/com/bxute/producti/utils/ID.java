/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.utils;

public class ID {
  public static int createIDWith(int hour, int day, int month, int year) {
    return (((year * 100 + month) * 100 + day) * 100 + hour);
  }
}
