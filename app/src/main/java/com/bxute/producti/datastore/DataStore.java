/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.datastore;

import android.content.Context;

import com.bxute.producti.database.LocalDatabase;
import com.bxute.producti.database.LocalDbContract;
import com.bxute.producti.model.FEMModel;
import com.cleancalendar.CalendarDayModel;

import java.util.ArrayList;

public class DataStore {
  LocalDatabase localDatabase;

  //Hour starts at: 00:00
  //Hour ends at: 23:00
  public DataStore(Context context) {
    localDatabase = new LocalDatabase(context);
  }

  public ArrayList<FEMModel> getSingleDayData(CalendarDayModel calendarDay) {
    return getDataInRange(calendarDay, calendarDay);
  }

  /**
   * Returns average data in given range.
   *
   * @param fromDay day from which data is to be taken.
   * @param toDay   day to which data is to be taken.
   * @return Average calculated data list.
   */
  public ArrayList<FEMModel> getDataInRange(CalendarDayModel fromDay, CalendarDayModel toDay) {
    ArrayList<FEMModel> avgDataList = new ArrayList<>();
    //get data for each day and fillIn rows
    CalendarDayModel currentDay = fromDay;
    int fromDataID = LocalDbContract.createIDFrom(0, currentDay.getDay(), currentDay.getMonth(), currentDay.getYear());
    int toDataID = LocalDbContract.createIDFrom(0, currentDay.getDay(), currentDay.getMonth(), currentDay.getYear());
    ArrayList<FEMModel> dataListInGivenRange = localDatabase.getAllRowsWithinRange(fromDataID, toDataID);
    avgDataList = calculateAverageFor(dataListInGivenRange);
    return avgDataList;
  }

  private ArrayList<FEMModel> calculateAverageFor(ArrayList<FEMModel> dataListInGivenRange) {
    //we assume dateList has items(real or default) at each hour.
    int days = dataListInGivenRange.size() / 24;
    ArrayList<FEMModel> avgDataList = new ArrayList<>();
    for (int i = 0; i < days; i++) {

    }
    return avgDataList;
  }

  private FEMModel getDefaultVoidModel() {
    FEMModel femModel = new FEMModel();
    femModel.setModifiedAt("");
    femModel.setCreatedAt("");
    femModel.setDataID((int) System.currentTimeMillis());
    femModel.setMotivation(-1);
    femModel.setEnergy(-1);
    femModel.setFocus(-1);
    femModel.setRemarks("");
    return femModel;
  }
}
