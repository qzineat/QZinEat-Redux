package com.qe.qzin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SignUpCallback;
import com.qe.qzin.R;
import com.qe.qzin.models.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {

  @BindView(R.id.etSignupEmail) EditText etSignupEmail;
  @BindView(R.id.etSignupPassword) EditText etSignupPassword;
  @BindView(R.id.etSignupPhone) EditText etSignupPhone;
  @BindView(R.id.ivSplashBagSignup) ImageView ivSplashBagSignup;
  @BindView(R.id.btnSignUp) Button btnSignUp;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_sign_up);

    ButterKnife.bind(this);

    Picasso.with(getApplicationContext())
        .load(R.drawable.feast)
        .fit()
        .centerCrop()
        .into(ivSplashBagSignup);

    btnSignUp.setOnClickListener(mSignUpButtonListener);
  }


  // SignUp button click
  View.OnClickListener mSignUpButtonListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      String userNameAsEmail = etSignupEmail.getText().toString();
      String password = etSignupPassword.getText().toString();
      String phone = etSignupPhone.getText().toString();

      //Force user to fill up the sign up form.
      if (userNameAsEmail.equals("") || password.equals("")) {
        Toast.makeText(getApplicationContext(), "Please complete the sign up form", Toast.LENGTH_SHORT).show();
      } else {

        User user = new User();
        user.setUsername(userNameAsEmail);
        user.setPassword(password);
        user.setEmail(userNameAsEmail); // set userName as email
        user.setPhone(phone);

        user.signUpInBackground(new SignUpCallback() {
          @Override
          public void done(ParseException e) {
            if (e == null) {
              // TODO: We should do user verification
              Intent i = new Intent(SignUpActivity.this, MainActivity.class);
              startActivity(i);
              //finish();
              Toast.makeText(getApplicationContext(), "Successfully signed up !", Toast.LENGTH_SHORT).show();

            } else {
              Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
              e.printStackTrace();
            }

          }
        });

      }
    }
  };


}
