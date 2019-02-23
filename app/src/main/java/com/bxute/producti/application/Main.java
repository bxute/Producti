/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.application;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;

public class Main extends Application {
  private static Context mContext;

  public static Context context() {
    return mContext;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mContext = getApplicationContext();
    Stetho.initializeWithDefaults(this);
  }
}
