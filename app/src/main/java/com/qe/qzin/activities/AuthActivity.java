package com.qe.qzin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.qe.qzin.R;
import com.qe.qzin.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AuthActivity extends AppCompatActivity {

  @BindView(R.id.etUserName) EditText etUserName;
  @BindView(R.id.etPassword) EditText etPassword;
  @BindView(R.id.btnLogin) Button btnLogin;
  @BindView(R.id.btnSignup) Button btnSignup;
  @BindView(R.id.btnFBLogin) Button btnFBLogin;
  //List<String> permissions = Arrays.asList("user_birthday", "user_location", "user_friends", "email", "public_profile");



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);

    ButterKnife.bind(this);

    getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    btnLogin.setOnClickListener(mLogInButtonListener);
    btnSignup.setOnClickListener(mSignUpButtonListener);
    btnFBLogin.setOnClickListener(mFBLoginBtnListener);


  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode,data);
  }

  // Signup button click
  View.OnClickListener mSignUpButtonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {



      String userNameAsEmail = etUserName.getText().toString();
      String password = etPassword.getText().toString();

      //Force user to fill up the sign up form.
      if (userNameAsEmail.equals("") || password.equals("")) {
        Toast.makeText(getApplicationContext(), "Please complete the sign up form", Toast.LENGTH_SHORT).show();
      } else {

        User user = new User();
        user.setUsername(userNameAsEmail);
        user.setPassword(password);
        user.setEmail(userNameAsEmail); // set userName as email

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              // TODO: We should do user verification
              Intent i = new Intent(AuthActivity.this, MainActivity.class);
              startActivity(i);
              finish();
              Toast.makeText(getApplicationContext(), "Successfully signed up, Please log in.", Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(getApplicationContext(), "Sign up Error. UserName exists", Toast.LENGTH_SHORT).show();
              e.printStackTrace();
            }

          }
        });

      }
    }
  };

  // LogIn Button Click
  View.OnClickListener mLogInButtonListener = new View.OnClickListener() {

    @Override
    public void onClick(View v) {

      String userName = etUserName.getText().toString();
      String password = etPassword.getText().toString();

      ParseUser.logInInBackground(userName, password, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {

          if (user != null) {
            Toast.makeText(getApplicationContext(),"Successfully Logged in !", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AuthActivity.this, MainActivity.class);
            startActivity(i);
            finish();
          }
          else {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"No such user exist, please sign up.", Toast.LENGTH_SHORT).show();
          }
        }
      });

    }
  };


// Facebook Login
  View.OnClickListener mFBLoginBtnListener = new View.OnClickListener(){
    @Override
    public void onClick(View v) {

      List<String> permissions = new ArrayList<>();
      permissions.add("email");

      ParseFacebookUtils.logInWithReadPermissionsInBackground(AuthActivity.this, permissions, new LogInCallback() {

        @Override
        public void done(ParseUser user, ParseException err) {

            if(user == null) {
              Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
            }else if(user.isNew()){
              Intent i = new Intent(AuthActivity.this, MainActivity.class);
              startActivity(i);
              finish();
              Log.d("MyApp", "User signed up and logged in through Facebook!");
            }else
            {
              Intent i = new Intent(AuthActivity.this, MainActivity.class);
              startActivity(i);
              finish();
              Toast.makeText(AuthActivity.this,"Logged In", Toast.LENGTH_SHORT).show();
              Log.d("MyApp", "User logged in through Facebook!");
            }

        }

      });
    }
  };

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }
}
