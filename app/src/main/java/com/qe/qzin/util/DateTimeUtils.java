package com.qe.qzin.util;

import android.support.annotation.Nullable;
import android.text.format.DateFormat;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

/**
 * Created by Shyam Rokde on 10/16/16.
 */

public class DateTimeUtils {
  private static final PrettyTime prettyTime = new PrettyTime();

  /**
   * Formats a past or future {Date} relative to current time.
   * Example: "5 days ago", "moments from now"
   *
   * @param date  {Date} to format
   * @return      a relative datetime string like "3 weeks ago"
   */
  public static String formatRelative(@Nullable Date date) {
    if (date == null) throw new IllegalArgumentException("date cannot be null!");
    return prettyTime.format(date);
  }

  public static String formatDate(@Nullable Date date){
    if (date == null) throw new IllegalArgumentException("date cannot be null!");

    DateFormat df = new DateFormat();
    return df.format("MMM dd, yyyy", date.getTime()).toString();
  }
}
