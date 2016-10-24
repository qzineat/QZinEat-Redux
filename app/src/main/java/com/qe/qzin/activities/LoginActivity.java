package com.qe.qzin.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {

  @BindView(R.id.etUserName) EditText etUserName;
  @BindView(R.id.etPassword) EditText etPassword;
  @BindView(R.id.btnLogin) Button btnLogin;
  @BindView(R.id.btnSignup) Button btnSignup;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    ButterKnife.bind(this);

    btnLogin.setOnClickListener(mLogInButtonListener);
    btnSignup.setOnClickListener(mSignUpButtonListener);

  }

  View.OnClickListener mSignUpButtonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      String userNameAsEmail = etUserName.getText().toString();
      String password = etPassword.getText().toString();

      User user = new User();
      user.setUsername(userNameAsEmail);
      user.setPassword(password);
      user.setEmail(userNameAsEmail); // set userName as email

      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {

          if (e == null)
            Log.d("DEBUG", "Successfully created a new user");
          else {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            Log.d("DEBUG", e.getMessage());
          }

        }
      });
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

          if (user != null)
            Log.d("DEBUG", "User Successfully Logged in");
          else {
            e.printStackTrace();
            Log.d("DEBUG", "Falied to log in");
          }
        }
      });

    }
  };

}
