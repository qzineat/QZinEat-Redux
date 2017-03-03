package com.qe.qzin.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shyam Rokde on 3/2/17.
 */

public class DatePickerFragment extends DialogFragment {

  private DatePickerDialog.OnDateSetListener mDatePickerListener;

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    // Use the current date as the default date in the picker
    Calendar c = Calendar.getInstance();
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), mDatePickerListener, year, month, day);
    datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
    datePickerDialog.getDatePicker().updateDate(year, month, day);

    // Create a new instance of DatePickerDialog and return it
    return datePickerDialog;
  }


  public void setDatePickerListener(DatePickerDialog.OnDateSetListener listener){
    mDatePickerListener = listener;
  }
}
