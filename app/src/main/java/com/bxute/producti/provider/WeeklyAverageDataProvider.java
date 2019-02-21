/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.provider;

import android.content.Context;
import android.graphics.Color;

import com.bxute.producti.datastore.DataStore;
import com.bxute.producti.model.FEMModel;
import com.bxute.producti.utils.HourFormatter;
import com.cleancalendar.CalendarDayModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class WeeklyAverageDataProvider implements IChartDataProvider {
  int hourMode;
  DataStore dataStore;
  LineChart mLineChart;

  public WeeklyAverageDataProvider(Context context, LineChart lineChart) {
    dataStore = new DataStore(context);
    this.mLineChart = lineChart;
    hourMode = HourFormatter.TWENTY_FOUR_HOUR_MODE;
  }

  @Override
  public LineData getLineData() {
    ArrayList<FEMModel> dataList = dataStore.getDataInRange(
     CalendarDayModel.from(LocalDate.of(2019, 1, 1)),
     CalendarDayModel.from(LocalDate.of(2019, 1, 1).plusWeeks(1))
    );

    final String[] xAxisValues = new String[dataList.size()];
    List<Entry> focus = new ArrayList<>();
    List<Entry> energy = new ArrayList<>();
    List<Entry> motivationsLevelList = new ArrayList<>();

    //x - time of the day
    //y - respective level (focus , energy etc.)
    for (int i = 0; i < dataList.size(); i++) {
      focus.add(new Entry(HourFormatter.getHour(dataList.get(i).getDataID()), dataList.get(i).getFocus()));
      motivationsLevelList.add(new Entry(HourFormatter.getHour(dataList.get(i).getDataID()), dataList.get(i).getMotivation()));
      energy.add(new Entry(HourFormatter.getHour(dataList.get(i).getDataID()), dataList.get(i).getEnergy()));
      xAxisValues[i] = HourFormatter.getFormattedHour(dataList.get(i).getDataID(), hourMode);
    }

    //set x-axis values
    IAxisValueFormatter formatter = new IAxisValueFormatter() {
      @Override
      public String getFormattedValue(float value, AxisBase axis) {
        return xAxisValues[(int) value];
      }
    };
    XAxis xAxis = mLineChart.getXAxis();
    xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
    xAxis.setValueFormatter(formatter);

    //Prepare data sets
    LineDataSet focusDataSet = new LineDataSet(focus, "Focus");
    focusDataSet.setColor(Color.parseColor("#BB1C53"));
    focusDataSet.setCircleColor(Color.parseColor("#BB1C53"));

    LineDataSet energyDataSet = new LineDataSet(energy, "Energy");
    energyDataSet.setColor(Color.parseColor("#2196F3"));
    energyDataSet.setCircleColor(Color.parseColor("#2196F3"));

    LineDataSet motivationDataSet = new LineDataSet(motivationsLevelList, "Motivation");
    motivationDataSet.setColor(Color.parseColor("#9806B9"));
    motivationDataSet.setCircleColor(Color.parseColor("#9806B9"));

    List<ILineDataSet> dataSets = new ArrayList<>();
    dataSets.add(energyDataSet);
    dataSets.add(focusDataSet);
    dataSets.add(motivationDataSet);

    return new LineData(dataSets);
  }

  @Override
  public Description getDescription() {
    return null;
  }
}
