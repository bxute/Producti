/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bxute.producti.R;

public class MainActivity extends AppCompatActivity {
  DataCollectionFragment dataCollectionFragment;
  StatsFragment statsFragment;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    statsFragment = new StatsFragment();
    getSupportFragmentManager()
     .beginTransaction()
     .replace(R.id.frame_container, statsFragment)
     .commit();
  }
}
