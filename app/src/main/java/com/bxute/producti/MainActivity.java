/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements VerticalSlider.SliderProgressListener {

  private TextView valueTv;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    valueTv = findViewById(R.id.value);
    VerticalSlider verticalSlider = findViewById(R.id.slider);
    verticalSlider.setSliderProgressListener(this);
  }

  @Override
  public void onSliderValueChanged(int value) {
    valueTv.setText(String.valueOf(value));
  }
}
