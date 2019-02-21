/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti;

import com.bxute.producti.database.LocalDbContract;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test
  public void test_createIdFrom() {
    int year = 2019;
    int month = 2;
    int date = 3;
    int hour = 4;
    int id = LocalDbContract.createIDFrom(hour, date, month, year);
    System.out.print(id);
    assertEquals(2019020304, id);
  }
}