/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
  DataCollectionFragment dataCollectionFragment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    dataCollectionFragment = new DataCollectionFragment();
    getSupportFragmentManager()
     .beginTransaction()
     .replace(R.id.frame_container, dataCollectionFragment)
     .commit();
  }
}
