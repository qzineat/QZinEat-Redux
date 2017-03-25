package com.qe.qzin.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.qe.qzin.R;
import com.qe.qzin.dialogs.DatePickerFragment;
import com.qe.qzin.dialogs.TimePickerFragment;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;
import com.qe.qzin.models.imgur.ImgurResponse;
import com.qe.qzin.services.ImgurUploadService;
import com.qe.qzin.util.BitmapScaler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

  @BindView(R.id.etTitle) EditText etTitle;
  @BindView(R.id.etDescription) EditText etDescription;
  @BindView(R.id.etServiceFee) EditText etServiceFee;
  @BindView(R.id.etEventDate) EditText etEventDate;
  @BindView(R.id.etTimeFrom) EditText etTimeFrom;
  @BindView(R.id.etTimeTo) EditText etTimeTo;
  @BindView(R.id.etStreet) EditText etStreet;
  @BindView(R.id.etCity) EditText etCity;
  @BindView(R.id.etState) EditText etState;
  @BindView(R.id.etCountry) EditText etCountry;
  @BindView(R.id.btnAddEvent) Button btnAddEvent;
  @BindView(R.id.ivEventImage) ImageView ivEventImage;
  @BindView(R.id.etGuestCount) EditText etGuestCount;
  @BindView(R.id.etMenu) EditText etMenu;


  private static final int SELECT_PICTURE = 100;

  private Bitmap mImageBitmap = null;
  private Date mEventDate = null;
  private boolean mIsTimeFrom = false;
  private boolean mIsTimeTo = false;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_host);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Host Event");

    setSupportActionBar(toolbar);


    // add back arrow to toolbar
    if (getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // select date
    etEventDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        DatePickerFragment pickerFragment = new DatePickerFragment();
        pickerFragment.setDatePickerListener(HostActivity.this);
        pickerFragment.show(getSupportFragmentManager(), "datePicker");
      }
    });

    // select time
    etTimeFrom.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mIsTimeFrom = true;
        TimePickerFragment pickerFragment = new TimePickerFragment();
        pickerFragment.setTimePickerListener(HostActivity.this);
        pickerFragment.show(getSupportFragmentManager(), "fromTimePicker");
      }
    });
    etTimeTo.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mIsTimeTo = true;
        TimePickerFragment pickerFragment = new TimePickerFragment();
        pickerFragment.setTimePickerListener(HostActivity.this);
        pickerFragment.show(getSupportFragmentManager(), "toTimePicker");
      }
    });

    // select image
    ivEventImage.setOnClickListener(mEventImageListener);

    // Save Event if user logged in
    btnAddEvent.setOnClickListener(mAddEventButtonListener);


  }


  View.OnClickListener mAddEventButtonListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      // Check user is logged in or not - just to protect
      if(!User.isLoggedIn()){
        Toast.makeText(HostActivity.this, "Please LogIn to Host Event!!!", Toast.LENGTH_SHORT).show();
        return;
      }

      // validate
      if(!isValidEvent()){
        Toast.makeText(HostActivity.this, "Invalid Event Data", Toast.LENGTH_SHORT).show();
        return;
      }

      // 1. save event
      // 2. upload image
      // 3. update event
      saveEvent();
    }
  };

  View.OnClickListener mEventImageListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      Intent i = new Intent();
      i.setType("image/*");
      i.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }
  };


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()){

      // handle back arrow click
      case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public boolean isValidEvent(){

    if(etTitle.getText().toString().isEmpty()){
      return false;
    }

    return true;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(resultCode == RESULT_OK){
      if(requestCode == SELECT_PICTURE){
        Uri selectedImageUri = data.getData();
        if (null != selectedImageUri) {
          // Resize Image
          Bitmap bitmap = null;

          try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            if(bitmap.getByteCount() > (1024 * 512)){
              mImageBitmap = BitmapScaler.scaleToFill(bitmap, 800, 700);
            }else{
              mImageBitmap = bitmap;
            }
          }catch (IOException ex){
            Log.d("ERROR", "Unable to resize bitmap -" + ex.getMessage());
          }

          if(mImageBitmap != null){
            ivEventImage.setImageBitmap(mImageBitmap);
          }
        }
      }
    }

    super.onActivityResult(requestCode, resultCode, data);
  }

  private void saveEvent(){
    // Save Event
    final Event ev = new Event();
    ev.setHostUser(User.getCurrentUser());
    ev.setTitle(etTitle.getText().toString());
    ev.setDescription(etDescription.getText().toString());
    ev.setLocality(etCity.getText().toString());
    ev.setAdministrativeArea(etState.getText().toString());
    ev.setDate(mEventDate);
    ev.setEventTimeFrom(etTimeFrom.getText().toString());
    ev.setEventTimeTo(etTimeTo.getText().toString());
    double serviceFee = Double.parseDouble(etServiceFee.getText().toString());
    ev.setAmount(serviceFee);
    ev.setCurrency("USD");
    ev.setMaxGuestCount(Integer.valueOf(etGuestCount.getText().toString()));
    ev.setEventMenu(etMenu.getText().toString());

    ev.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e != null){
          Log.d("ERROR", "Unable to save event " + e.getMessage());
          return;
        }
        // upload image
        uploadImage(ev.getObjectId());
        //ev.getObjectId()
        Toast.makeText(HostActivity.this, "Event Saved Successfully!!!", Toast.LENGTH_SHORT).show();
      }
    });
  }

  private void updateEventImage(String eventId, String imageUrl){
    Event ev = new Event();
    ev.setObjectId(eventId);
    ev.setEventImageUrl(imageUrl);

    ev.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e != null){
          Log.e("ERROR", "Unable to save event: " + e.getMessage());
        }
      }
    });
  }

  private void uploadImage(final String eventId){
    if(mImageBitmap == null){
      return;
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
    byte[] data = baos.toByteArray();

    ImgurUploadService uploadService = new ImgurUploadService();
    uploadService.execute(data, new Callback<ImgurResponse>() {
      @Override
      public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {
        // now update event - we could do this in background
        updateEventImage(eventId, response.body().getData().getLink());
      }

      @Override
      public void onFailure(Call<ImgurResponse> call, Throwable t) {
        Log.d("ERROR", "Failed to upload event image" + t.getMessage());
      }
    });
  }


  @Override
  public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
    int day = datePicker.getDayOfMonth();
    int month = datePicker.getMonth();
    int year =  datePicker.getYear();

    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);

    // Set the date
    mEventDate = calendar.getTime();
    etEventDate.setText(DateFormat.getDateInstance().format(calendar.getTime()));
  }


  @Override
  public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
    // check which time is clicked
    if(mIsTimeFrom){
      mIsTimeFrom = false;
      etTimeFrom.setText(new StringBuilder().append(selectedHour).append(":").append(selectedMinute));
    }else if(mIsTimeTo){
      mIsTimeTo = false;
      etTimeTo.setText(new StringBuilder().append(selectedHour).append(":").append(selectedMinute));
    }
  }
}
