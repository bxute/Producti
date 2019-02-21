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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bxute.producti.R;
import com.bxute.producti.database.DummyData;
import com.bxute.producti.provider.SingleDayDataProvider;
import com.github.mikephil.charting.charts.LineChart;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

  LineChart lineChart;
  SingleDayDataProvider singleDayDataProvider;
  DummyData dummyData;


  public StatsFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_stats, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    lineChart = view.findViewById(R.id.chart);
//    dummyData = new DummyData(view.getContext());
//    dummyData.deleteDummyData();
//    dummyData.insertDummyData();
    singleDayDataProvider = new SingleDayDataProvider(view.getContext(), lineChart);
    lineChart.setData(singleDayDataProvider.getLineData());
  }
}
