package com.qe.qzin.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;
import com.qe.qzin.R;
import com.qe.qzin.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UpdateProfileActivity extends BaseActivity {

  @BindView(R.id.etFirstName) EditText etFirstName;
  @BindView(R.id.etEmail) EditText etEmail;
  @BindView(R.id.etPhone) EditText etPhone;
  @BindView(R.id.btnUpdate) Button btnUpdate;

  private User user;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_update_profile);

    ButterKnife.bind(this);

    // Only used for logged in users to update there profile

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Update Profile");

    setSupportActionBar(toolbar);

    // add back arrow to toolbar
    if (getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    // Load user data
    loadUserData();

    btnUpdate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        // call user save
        saveUserProfile(view);
      }
    });
  }


  private void saveUserProfile(final View view){
    user.setFirstName(etFirstName.getText().toString());

    user.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        if(e != null){
          Toast.makeText(UpdateProfileActivity.this, "Unable to save profile!!", Toast.LENGTH_SHORT).show();
          return;
        }

        Snackbar.make(view, "Profile updated successfully!!", Snackbar.LENGTH_LONG).show();
        finish();
      }
    });
  }

  private void loadUserData(){
    user = (User) User.getCurrentUser();

    // Load user profile data
    if(user.getFirstName() != null){
      etFirstName.setText(user.getFirstName());
    }
    if(user.getEmail() != null){
      etEmail.setText(user.getEmail());
    }
    if(user.getPhone() != null){
      etPhone.setText(user.getPhone());
    }
  }
}
