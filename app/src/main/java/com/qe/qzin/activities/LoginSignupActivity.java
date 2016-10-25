package com.qe.qzin.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.qe.qzin.R;
import com.qe.qzin.models.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginSignupActivity extends AppCompatActivity {

  @BindView(R.id.etUserName) EditText etUserName;
  @BindView(R.id.etPassword) EditText etPassword;
  @BindView(R.id.btnLogin) Button btnLogin;
  @BindView(R.id.btnSignup) Button btnSignup;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_loginsignup);

    ButterKnife.bind(this);

    btnLogin.setOnClickListener(mLogInButtonListener);
    btnSignup.setOnClickListener(mSignUpButtonListener);

  }


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
              Toast.makeText(getApplicationContext(), "Successfully signed up, Please log in.", Toast.LENGTH_SHORT).show();
            } else {
              Toast.makeText(getApplicationContext(), "Sign up Error", Toast.LENGTH_SHORT).show();
              e.printStackTrace();
            }

          }
        });
      }
    }
  };

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
          }
          else {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"No such user exist, please sign up.", Toast.LENGTH_SHORT).show();
          }
        }
      });

    }
  };

}
