package com.qe.qzin.activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.qe.qzin.R;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HostActivity extends BaseActivity {

  @BindView(R.id.etTitle) EditText etTitle;
  @BindView(R.id.etDescription) EditText etDescription;
  @BindView(R.id.etServiceFee) EditText etServiceFee;
  @BindView(R.id.etEventDate) EditText etEventDate;
  @BindView(R.id.etStreet) EditText etStreet;
  @BindView(R.id.etCity) EditText etCity;
  @BindView(R.id.etState) EditText etState;
  @BindView(R.id.etCountry) EditText etCountry;
  @BindView(R.id.btnAddEvent) Button btnAddEvent;




  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_host);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Event");

    setSupportActionBar(toolbar);


    // add back arrow to toolbar
    if (getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


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

      // Save Event
      Event ev = new Event();
      ev.setHostUser(User.getCurrentUser());
      ev.setTitle(etTitle.getText().toString());
      ev.setDescription(etDescription.getText().toString());

      double serviceFee = Double.parseDouble(etServiceFee.getText().toString());
      ev.setAmount(serviceFee);
      ev.setCurrency("USD");
      ev.setLocality(etCity.getText().toString());
      ev.setAdministrativeArea(etState.getText().toString());

      ev.saveInBackground(new SaveCallback() {
        @Override
        public void done(ParseException e) {
          Toast.makeText(HostActivity.this, "Event Saved Successfully!!!", Toast.LENGTH_SHORT).show();
        }
      });
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

}
