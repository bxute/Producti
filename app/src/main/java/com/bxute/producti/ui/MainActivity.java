/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bxute.producti.R;

public class MainActivity extends AppCompatActivity {
  private final int METER_TAB = 0;
  private final int STATS_TAB = 1;
  private final int CALENDAR_TAB = 2;
  private final int SETTING_TAB = 3;

  DataCollectionFragment dataCollectionFragment;
  StatsFragment statsFragment;
  CalendarFragment calendarFragment;
  SettingsFragment settingsFragment;
  ImageView statsIcon;
  ImageView meterIcon;
  ImageView calendarIcon;
  ImageView settingsIcon;
  private int mCurrentTab = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initializeTabIcons();
    attachClickListenersOnTabs();
    //by default
    selectTab(METER_TAB);
  }

  private void initializeTabIcons() {
    statsIcon = findViewById(R.id.stats_icon);
    meterIcon = findViewById(R.id.meter_icon);
    calendarIcon = findViewById(R.id.calendar_icon);
    settingsIcon = findViewById(R.id.settings_icon);
  }

  private void selectTab(int tab) {
    //handle all the NPE at once! :)
    try {
      if (mCurrentTab == tab)
        return;
      meterIcon.clearColorFilter();
      statsIcon.clearColorFilter();
      calendarIcon.clearColorFilter();
      settingsIcon.clearColorFilter();
      switch (tab) {
        case METER_TAB:
          meterIcon.setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);
          if (dataCollectionFragment == null) {
            dataCollectionFragment = new DataCollectionFragment();
          }
          transactFragment(dataCollectionFragment);
          break;
        case STATS_TAB:
          statsIcon.setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);
          if (statsFragment == null) {
            statsFragment = new StatsFragment();
          }
          transactFragment(statsFragment);
          break;
        case CALENDAR_TAB:
          calendarIcon.setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);
          if (calendarFragment == null) {
            calendarFragment = new CalendarFragment();
          }
          transactFragment(calendarFragment);
          break;
        case SETTING_TAB:
          settingsIcon.setColorFilter(Color.parseColor("#2196F3"), PorterDuff.Mode.SRC_ATOP);
          if (settingsFragment == null) {
            settingsFragment = new SettingsFragment();
          }
          transactFragment(settingsFragment);
          break;
      }
      mCurrentTab = tab;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void transactFragment(Fragment fragment) {
    getSupportFragmentManager()
     .beginTransaction()
     .replace(R.id.frame_container, fragment)
     .commit();
  }

  private void attachClickListenersOnTabs() {
    if (statsIcon != null)
      statsIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          selectTab(STATS_TAB);
        }
      });

    if (meterIcon != null)
      meterIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          selectTab(METER_TAB);
        }
      });

    if (calendarIcon != null)
      calendarIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          selectTab(CALENDAR_TAB);
        }
      });

    if (settingsIcon != null)
      settingsIcon.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          selectTab(SETTING_TAB);
        }
      });
  }
}
