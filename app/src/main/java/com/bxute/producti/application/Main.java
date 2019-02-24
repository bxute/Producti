/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.application;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;

public class Main extends MultiDexApplication {
  private static Context mContext;

  public static Context context() {
    return mContext;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    MultiDex.install(this);
    mContext = getApplicationContext();
    Stetho.initializeWithDefaults(this);
  }
}
