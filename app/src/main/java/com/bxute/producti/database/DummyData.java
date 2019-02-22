/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.database;

import android.content.Context;
import android.util.Log;

import com.bxute.producti.model.FEMModel;

import java.util.Random;

public class DummyData {
  Random random;
  LocalDatabase localDatabase;
  private DummyDataCallback dummyDataCallback;

  public DummyData(Context context) {
    localDatabase = new LocalDatabase(context);
    random = new Random();
  }

  public void insertDummyData() {
    int days = 7;
    FEMModel model = new FEMModel();
    for (int j = 1; j < days; j++) {
      //0 to 23 Hrs.
      for (int i = 0; i < 24; i++) {
        int h = i;
        int d = j;
        int m = (j / 30) + 1;
        int y = 2019;
        model.setRemarks("Thought of id " + i);
        model.setEnergy(i == 2 ? -1 : getRandomInt());
        model.setFocus(getRandomInt());
        model.setMotivation(getRandomInt());
        model.setDataID(LocalDbContract.createIDFrom(h, d, m, y));
        model.setCreatedAt("--ca--");
        model.setModifiedAt("--ma--");
        localDatabase.insertRow(model);
      }
    }
    if (dummyDataCallback != null) {
      dummyDataCallback.onDataInsertionFinished();
    }
  }

  private int getRandomInt() {
    return random.nextInt(10);
  }

  public void deleteDummyData() {
    localDatabase.allRows();
  }

  public void setDummyDataCallback(DummyDataCallback dummyDataCallback) {
    this.dummyDataCallback = dummyDataCallback;
  }

  public interface DummyDataCallback {
    void onDataInsertionFinished();
  }
}
