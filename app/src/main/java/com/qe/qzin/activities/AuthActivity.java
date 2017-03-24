package com.qe.qzin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.qe.qzin.R;

import com.qe.qzin.models.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AuthActivity extends BaseActivity {

  @BindView(R.id.etUserName) EditText etUserName;
  @BindView(R.id.etPassword) EditText etPassword;
  @BindView(R.id.btnLogin) Button btnLogin;
  @BindView(R.id.tvSignUp) TextView tvSignup;
  @BindView(R.id.btnFBLogin) Button btnFBLogin;
  @BindView(R.id.ivSplashBag) ImageView ivSplashBag;
  //List<String> permissions = Arrays.asList("user_birthday", "user_location", "user_friends", "email", "public_profile");


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_auth);

    ButterKnife.bind(this);

    Picasso.with(getApplicationContext())
        .load(R.drawable.feast)
        .fit()
        .centerCrop()
        .into(ivSplashBag);

    getWindow().getDecorView().setSystemUiVisibility(
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

    btnLogin.setOnClickListener(mLogInButtonListener);

    tvSignup.setOnClickListener(mSignUpListener);

    btnFBLogin.setOnClickListener(mFBLoginBtnListener);


  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    ParseFacebookUtils.onActivityResult(requestCode, resultCode,data);
  }

  // Signup button click
  View.OnClickListener mSignUpListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {

      Intent i = new Intent(AuthActivity.this, SignUpActivity.class);
      startActivity(i);
      finish();
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
            }else{
              //if(user.isNew()){
                // TODO: will add notification for first time user
                saveFacebookUserDetails((User) user);
              //}

              Intent i = new Intent(AuthActivity.this, MainActivity.class);
              startActivity(i);
              finish();
              Log.d("MyApp", "User logged in through Facebook!");

            }

        }

      });
    }
  };

  private void saveFacebookUserDetails(final User user){



    GraphRequest request = GraphRequest.newMeRequest(
            AccessToken.getCurrentAccessToken(),
            new GraphRequest.GraphJSONObjectCallback() {
              @Override
              public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                  user.setEmail(object.getString("email"));
                  user.setFirstName(object.getString("first_name"));
                  user.saveInBackground();

                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            });
    Bundle parameters = new Bundle();
    parameters.putString("fields", "id,name,email,first_name,last_name");
    request.setParameters(parameters);
    request.executeAsync();
  }
}
