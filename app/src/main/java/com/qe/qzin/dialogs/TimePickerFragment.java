package com.qe.qzin.dialogs;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by Shyam Rokde on 3/2/17.
 */

public class TimePickerFragment extends DialogFragment {

  private TimePickerDialog.OnTimeSetListener mTimePickerListener;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current date as the default date in the picker
    Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR);
    int min = c.get(Calendar.MINUTE);
    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), mTimePickerListener, hour, min, false);

    return timePickerDialog;
  }

  public void setTimePickerListener(TimePickerDialog.OnTimeSetListener listener){
    mTimePickerListener = listener;
  }
}
