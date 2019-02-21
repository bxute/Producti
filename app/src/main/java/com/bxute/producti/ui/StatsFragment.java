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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bxute.producti.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {

  LineChart lineChart;

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
    plugInDataToLineChart();
  }

  private void plugInDataToLineChart() {
    final String[] quarters = new String[20];
    List<Entry> entries = new ArrayList<>();
    List<Entry> entries1 = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      quarters[i] = "D" + i;
      entries.add(new Entry(i, i * 3));
    }

    for (int i = 0; i < 20; i++) {
      entries1.add(new Entry(i, i * i * 2));
    }

    LineDataSet lineDataSet = new LineDataSet(entries, "Square");
    lineDataSet.setColor(Color.parseColor("#BB1C53"));
    lineDataSet.setCircleColor(Color.parseColor("#BB1C53"));
    LineDataSet cubeDataSet = new LineDataSet(entries1, "Cube");
    cubeDataSet.setColor(Color.parseColor("#2196F3"));
    cubeDataSet.setLineWidth(2);
    cubeDataSet.setCircleHoleRadius(2);
    cubeDataSet.setCircleRadius(6);
    cubeDataSet.setCircleColor(Color.parseColor("#2196F3"));
    List<ILineDataSet> dataSets = new ArrayList<>();
    dataSets.add(lineDataSet);
    dataSets.add(cubeDataSet);

    IAxisValueFormatter formatter = new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        return quarters[(int) value];
      }
    };
    XAxis xAxis = lineChart.getXAxis();
    xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
    xAxis.setValueFormatter(formatter);
    LineData lineData = new LineData(dataSets);


    lineChart.setData(lineData);
    Description description = new Description();
    description.setText("Sample test data");
    lineChart.setDescription(description);
    lineChart.invalidate();
  }
}
