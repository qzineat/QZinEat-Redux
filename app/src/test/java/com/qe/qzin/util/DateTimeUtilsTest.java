package com.qe.qzin.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Created by Shyam Rokde on 10/16/16.
 */
public class DateTimeUtilsTest {
  @Test
  public void formatRelative() throws Exception {
    assertEquals("moments from now", DateTimeUtils.formatRelative(new Date()));
  }

  @Test
  public void testMinutesFromNow() throws Exception {
    Date d = new Date();
    long newTime = d.getTime() + (1000 * 60 * 15);
    d.setTime(newTime);
    assertEquals("15 minutes from now", DateTimeUtils.formatRelative(d));
  }

  @Test
  public void testMinutesAgo() throws Exception {
    Date d = new Date();
    long newTime = d.getTime() - (1000 * 60 * 15);
    d.setTime(newTime);
    assertEquals("15 minutes ago", DateTimeUtils.formatRelative(d));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentException() {
    DateTimeUtils.formatRelative(null);
  }
}