/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.database;

public class LocalDbContract {
  public static final String TABLE_FEM = "femTable";
  public static final String COL_DATA_ID = "dataID";
  public static final String COL_ENERGY = "energy";
  public static final String COL_FOCUS = "focus";
  public static final String COL_MOTIVATION = "motivation";
  public static final String COL_CREATED_AT = "created_at";
  public static final String COL_MODIFIED_AT = "modified_at";
  public static final String COL_REMARKS = "remarks";

  public static String[] COLUMNS = new String[]{
   COL_DATA_ID,
   COL_ENERGY,
   COL_FOCUS,
   COL_MOTIVATION,
   COL_CREATED_AT,
   COL_MODIFIED_AT,
   COL_REMARKS
  };
}
