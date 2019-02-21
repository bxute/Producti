/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bxute.producti.model.FEMModel;

import java.util.ArrayList;

import androidx.annotation.Nullable;

public class LocalDatabase extends SQLiteOpenHelper {
  private static final String TAG = LocalDatabase.class.getSimpleName();
  private static final String DB_NAME = "productiDB";
  private static final int DB_VERSION = 1;

  public final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS " + LocalDbContract.TABLE_FEM + "" +
   "(" +
   LocalDbContract.COL_DATA_ID + " INTEGER PRIMARY KEY, " +
   LocalDbContract.COL_ENERGY + " INTEGER, " +
   LocalDbContract.COL_FOCUS + " INTEGER," +
   LocalDbContract.COL_MOTIVATION + " INTEGER," +
   LocalDbContract.COL_CREATED_AT + " TEXT," +
   LocalDbContract.COL_MODIFIED_AT + " TEXT," +
   LocalDbContract.COL_REMARKS + " TEXT" +
   ");";

  public LocalDatabase(@Nullable Context context) {
    super(context, DB_NAME, null, DB_VERSION);
    //force-call onCreate();
    getReadableDatabase();
  }

  public void insertRow(FEMModel femModel) {
    SQLiteDatabase database = getWritableDatabase();
    long id = database.insert(LocalDbContract.TABLE_FEM, null, getCVObject(femModel));
    if (id > 0) {
      Log.d(TAG, "inserted at " + id);
    }
  }

  private ContentValues getCVObject(FEMModel femModel) {
    ContentValues contentValues = new ContentValues();
    contentValues.put(LocalDbContract.COL_DATA_ID, femModel.getDataID());
    contentValues.put(LocalDbContract.COL_ENERGY, femModel.getEnergy());
    contentValues.put(LocalDbContract.COL_FOCUS, femModel.getFocus());
    contentValues.put(LocalDbContract.COL_MOTIVATION, femModel.getMotivation());
    contentValues.put(LocalDbContract.COL_REMARKS, femModel.getRemarks());
    contentValues.put(LocalDbContract.COL_CREATED_AT, femModel.getCreatedAt());
    contentValues.put(LocalDbContract.COL_MODIFIED_AT, femModel.getModifiedAt());
    return contentValues;
  }

  public void updateRow(FEMModel femModel) {
    SQLiteDatabase database = getWritableDatabase();
    long id = database.update(LocalDbContract.TABLE_FEM,
     getCVObject(femModel),
     LocalDbContract.COL_DATA_ID + "=?",
     new String[]{String.valueOf(femModel.getDataID())});
    if (id > 0) {
      Log.d(TAG, "updated at " + id);
    }
  }

  public void deleteRow(int dataID) {
    SQLiteDatabase database = getWritableDatabase();
    long id = database.delete(LocalDbContract.TABLE_FEM, LocalDbContract.COL_DATA_ID + "=?",
     new String[]{String.valueOf(dataID)});
    if (id > 0) {
      Log.d(TAG, "deleted at " + id);
    }
  }

  public FEMModel getRowWith(int dataID) {
    ArrayList<FEMModel> femModel = getAllRowsWithinRange(dataID, dataID);
    return femModel.size() > 0 ? femModel.get(0) : null;
  }

  public ArrayList<FEMModel> getAllRowsWithinRange(int minDataId, int maxDataId) {
    ArrayList<FEMModel> femModels = new ArrayList<>();
    SQLiteDatabase database = getReadableDatabase();
    String selection = LocalDbContract.COL_DATA_ID + " >= ? AND " + LocalDbContract.COL_DATA_ID + " <= ?";
    Cursor cursor = database.query(LocalDbContract.TABLE_FEM,
     LocalDbContract.COLUMNS,
     selection, new String[]{String.valueOf(minDataId), String.valueOf(maxDataId)},
     null, null, null);
    if (cursor != null) {
      boolean hasMore = cursor.getCount() > 0;
      cursor.moveToFirst();
      int dataId;
      int focus;
      int energy;
      int motivation;
      String remarks;
      String ca;
      String ma;
      FEMModel femModel = new FEMModel();
      while (hasMore) {
        dataId = cursor.getInt(cursor.getColumnIndex(LocalDbContract.COL_DATA_ID));
        focus = cursor.getInt(cursor.getColumnIndex(LocalDbContract.COL_FOCUS));
        energy = cursor.getInt(cursor.getColumnIndex(LocalDbContract.COL_ENERGY));
        motivation = cursor.getInt(cursor.getColumnIndex(LocalDbContract.COL_MOTIVATION));
        remarks = cursor.getString(cursor.getColumnIndex(LocalDbContract.COL_REMARKS));
        ca = cursor.getString(cursor.getColumnIndex(LocalDbContract.COL_CREATED_AT));
        ma = cursor.getString(cursor.getColumnIndex(LocalDbContract.COL_MODIFIED_AT));
        femModel.setDataID(dataId);
        femModel.setFocus(focus);
        femModel.setEnergy(energy);
        femModel.setMotivation(motivation);
        femModel.setRemarks(remarks);
        femModel.setCreatedAt(ca);
        femModel.setModifiedAt(ma);
        femModels.add(femModel);
        hasMore = cursor.moveToNext();
      }
    }
    return femModels;
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE_SQL);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + LocalDbContract.TABLE_FEM);
    onCreate(db);
  }
}
