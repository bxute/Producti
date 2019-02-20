/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataCollectionFragment extends Fragment {

  VerticalSlider energySlider;
  VerticalSlider focusSlider;
  VerticalSlider motivationSlider;

  TextView energyLevel;
  TextView focusLevel;
  TextView motivationLevel;

  public DataCollectionFragment() {
    // Required empty public constructor
  }


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_data_collection, container, false);
    energySlider = view.findViewById(R.id.energy_slider);
    focusSlider = view.findViewById(R.id.focus_slider);
    motivationSlider = view.findViewById(R.id.motivation_slider);
    energyLevel = view.findViewById(R.id.energy_level_text);
    focusLevel = view.findViewById(R.id.focus_level_text);
    motivationLevel = view.findViewById(R.id.motivation_level_text);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    attachListeners();
  }

  private void attachListeners() {
    energySlider.setSliderProgressListener(new VerticalSlider.SliderProgressListener() {
      @Override
      public void onSliderValueChanged(int value) {
        energyLevel.setText(String.valueOf(value));
      }
    });
    focusSlider.setSliderProgressListener(new VerticalSlider.SliderProgressListener() {
      @Override
      public void onSliderValueChanged(int value) {
        focusLevel.setText(String.valueOf(value));
      }
    });
    motivationSlider.setSliderProgressListener(new VerticalSlider.SliderProgressListener() {
      @Override
      public void onSliderValueChanged(int value) {
        motivationLevel.setText(String.valueOf(value));
      }
    });
  }
}
