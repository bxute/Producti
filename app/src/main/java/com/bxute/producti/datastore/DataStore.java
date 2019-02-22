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
    ArrayList<FEMModel> avgDataList;
    //get data for each day and fillIn rows
    int fromDataID = LocalDbContract.createIDFrom(0, fromDay.getDay(), fromDay.getMonth(), fromDay.getYear());
    int toDataID = LocalDbContract.createIDFrom(23, toDay.getDay(), toDay.getMonth(), toDay.getYear());
    ArrayList<FEMModel> dataListInGivenRange = localDatabase.getAllRowsWithinRange(fromDataID, toDataID);
    avgDataList = calculateAverageFor(dataListInGivenRange);
    return avgDataList;
  }

  public ArrayList<FEMModel> calculateAverageFor(ArrayList<FEMModel> dataListInGivenRange) {
    //we assume dataList has items(real or default) at each hour.
    ArrayList<FEMModel> avgDataList = new ArrayList<>();
    int focus[] = new int[24];
    int energy[] = new int[24];
    int motivation[] = new int[24];
    int[] count = new int[24];
    int arrIndex;
    FEMModel temp;
    for (int i = 0; i < dataListInGivenRange.size(); i++) {
      arrIndex = i % 24;
      temp = dataListInGivenRange.get(i);
      if (temp.getEnergy() > -1 && temp.getFocus() > -1 && temp.getMotivation() > -1) {
        //take the sum and increase the count
        focus[arrIndex] += temp.getFocus();
        energy[arrIndex] += temp.getEnergy();
        motivation[arrIndex] += temp.getMotivation();
        count[arrIndex] += 1;
      }
    }
    // calculate average
    int focusAvg;
    int energyAvg;
    int motivationAvg;
    for (int i = 0; i < 24; i++) {
      if (count[i] > 0) {
        focusAvg = focus[i] / count[i];
        energyAvg = energy[i] / count[i];
        motivationAvg = motivation[i] / count[i];
        FEMModel femModel = new FEMModel();
        femModel.setRemarks("");
        femModel.setFocus(focusAvg);
        femModel.setEnergy(energyAvg);
        femModel.setMotivation(motivationAvg);
        femModel.setCreatedAt("");
        femModel.setModifiedAt("");
        //only hour is relevant for rendering data.
        femModel.setDataID(LocalDbContract.createIDFrom(i, 1, 1, 1999));
        avgDataList.add(femModel);
      } else {
        avgDataList.add(getDefaultVoidModel());
      }
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
