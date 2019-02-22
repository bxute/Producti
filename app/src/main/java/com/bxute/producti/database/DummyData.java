/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.database;

import android.content.Context;

import com.bxute.producti.model.FEMModel;

import java.util.ArrayList;
import java.util.Random;

public class DummyData {
  Random random;
  LocalDatabase localDatabase;

  public DummyData(Context context) {
    localDatabase = new LocalDatabase(context);
    random = new Random();
  }

  public void insertDummyData() {
    ArrayList<FEMModel> femModels = new ArrayList<>();
    int days = 2;
    int totalHours = 24 * days;
    FEMModel model = new FEMModel();
    for (int i = 0; i < totalHours; i++) {
      int h = i % 24;
      int d = (i / 24) + 1;
      int m = (d / 30) + 1;
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

  private int getRandomInt() {
    return random.nextInt(10);
  }

  public void deleteDummyData() {
    localDatabase.allRows();
  }
}
