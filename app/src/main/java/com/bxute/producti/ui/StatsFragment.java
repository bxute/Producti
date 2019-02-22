/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.ui;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bxute.producti.R;
import com.bxute.producti.database.DummyData;
import com.bxute.producti.provider.MonthlyAverageDataProvider;
import com.bxute.producti.provider.SingleDayDataProvider;
import com.bxute.producti.provider.WeeklyAverageDataProvider;
import com.github.mikephil.charting.charts.LineChart;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment implements DummyData.DummyDataCallback {
  LineChart todaysChart;
  LineChart weeklyAvgChart;
  LineChart monthlyAvgChart;

  SingleDayDataProvider singleDayDataProvider;
  WeeklyAverageDataProvider weeklyAverageDataProvider;
  MonthlyAverageDataProvider monthlyAverageDataProvider;

  DummyData dummyData;
  private Context mContext;

  private Handler mHandler;

  public StatsFragment() {
    // Required empty public constructor
    mHandler = new Handler();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
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
    //createDummyData();
    initializeChartObjects(view);
    initializeDataProviders();
    plugInDataToCharts();
  }

  private void createDummyData() {
    dummyData = new DummyData(mContext);
    dummyData.setDummyDataCallback(this);
    dummyData.deleteDummyData();
    new Thread() {
      @Override
      public void run() {
        dummyData.insertDummyData();
      }
    }.start();
  }

  private void initializeChartObjects(View view) {
    todaysChart = view.findViewById(R.id.today_chart);
    weeklyAvgChart = view.findViewById(R.id.weekly_chart);
    monthlyAvgChart = view.findViewById(R.id.monthly_chart);
  }

  private void initializeDataProviders() {
    singleDayDataProvider = new SingleDayDataProvider(mContext, todaysChart);
    weeklyAverageDataProvider = new WeeklyAverageDataProvider(mContext, weeklyAvgChart);
    monthlyAverageDataProvider = new MonthlyAverageDataProvider(mContext, monthlyAvgChart);
  }

  @Override
  public void onDataInsertionFinished() {
    mHandler.post(new Runnable() {
      @Override
      public void run() {
        plugInDataToCharts();
      }
    });
  }

  private void plugInDataToCharts() {
    todaysChart.setData(singleDayDataProvider.getLineData());
    weeklyAvgChart.setData(weeklyAverageDataProvider.getLineData());
    monthlyAvgChart.setData(monthlyAverageDataProvider.getLineData());
  }
}
