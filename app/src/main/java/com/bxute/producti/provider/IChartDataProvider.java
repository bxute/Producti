/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.provider;

import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.LineData;

public interface IChartDataProvider {
  LineData getLineData();

  Description getDescription();
}
