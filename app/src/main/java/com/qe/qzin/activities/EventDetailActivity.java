package com.qe.qzin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.qe.qzin.R;
import com.qe.qzin.models.Enrollment;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;
import com.qe.qzin.util.DateTimeUtils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar) AppBarLayout appbar;
  @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
  @BindView(R.id.tvEventTitle) TextView tvEventTitle;
  @BindView(R.id.tvLocality) TextView tvLocality;
  @BindView(R.id.tvEventDate) TextView tvEventDate;
  @BindView(R.id.tvTime) TextView tvTime;
  @BindView(R.id.ivEventImage) ImageView ivEventImage;
  @BindView(R.id.tvStreet) TextView tvStreet;
  @BindView(R.id.tvCityStateZip) TextView tvCityStateZip;
  @BindView(R.id.tvEventDescription) TextView tvEventDescription;
  @BindView(R.id.tvHostName) TextView tvHostName;
  @BindView(R.id.tvWhats) TextView tvWhats;
  @BindView(R.id.tvMenu) TextView tvMenu;
  @BindView(R.id.btnReserve) Button btnReserve;
  @BindView(R.id.progressBar) ProgressBar progressBar;

  private String eventObjectId;
  private Event mEvent;
  private boolean isAlreadyReserved = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);

    ButterKnife.bind(this);

    setCollapsingToolbar();

    // Get EventId and load
    eventObjectId = getIntent().getExtras().getString("eventObjectId");
    loadEvent(eventObjectId);

    // Reserve
    btnReserve.setOnClickListener(mOnReserveClickListener);
  }

  private View.OnClickListener mloginClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      // on login click call Auth acivity
      Intent intentLoginSignup = new Intent(EventDetailActivity.this, AuthActivity.class);
      startActivity(intentLoginSignup);
      finish();
    }
  };

  private View.OnClickListener mOnReserveClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      // check login
      if(!User.isLoggedIn()){
        Snackbar.make(view, R.string.login_to_reserve, Snackbar.LENGTH_LONG)
            .setAction(R.string.login, mloginClickListener)
            .setActionTextColor(getResources().getColor(R.color.colorYellow))
            .show();
        return;
      }

      // check for host user.. - if I am host and trying to reserve then message him.
      if(Objects.equals(mEvent.getHostUser().getObjectId(), User.getCurrentUser().getObjectId())){
        Snackbar.make(view, "You are host for this event!!", Snackbar.LENGTH_LONG).show();
        return;
      }

      // Reserve Event
      btnReserve.setEnabled(false);
      reserveEvent(view);
    }
  };



  // Enroll user for an event.
  // TODO: Allow user to enter the guest count.
  private void reserveEvent(View view){
    // Check if event is full
    int guestCount = 1;
    if(mEvent.getEnrolledGuestCount() >= mEvent.getMaxGuestCount()){
      Snackbar.make(view, "Sorry!! Event is full...", Snackbar.LENGTH_LONG).show();
      return;
    }else{
      int afterEnrollCount = mEvent.getEnrolledGuestCount() + guestCount;
      if(afterEnrollCount > mEvent.getMaxGuestCount()){
        int remainingSeats = mEvent.getMaxGuestCount() - mEvent.getEnrolledGuestCount();
        Snackbar.make(view, String.format("Only %d seats remaining!", remainingSeats), Snackbar.LENGTH_LONG).show();
        btnReserve.setEnabled(true);
        return;
      }
    }

    // TODO: Check if user already registered for an Event
    showProgressBar();


    Enrollment en = new Enrollment();
    en.setEnrollUser((User) User.getCurrentUser());
    en.setEvent(mEvent);
    en.setGuestCount(guestCount);

    // save entry in Enrollment
    en.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        hideProgressBar();
        if (e != null) {
          Log.d("DEBUG", e.getMessage());
          btnReserve.setEnabled(true);
          return;
        }

        Toast.makeText(EventDetailActivity.this, "Thanks!!", Toast.LENGTH_SHORT).show();
        btnReserve.setVisibility(View.INVISIBLE);
      }
    });

    // update enrolled guest count
    mEvent.setEnrolledGuestCount(mEvent.getEnrolledGuestCount() + guestCount);
    mEvent.saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        Log.d("DEBUG", "User enrolled for an event");
      }
    });
  }

  private void setCollapsingToolbar(){
    toolbar.setTitle("");
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    // title
    collapsingToolbar.setTitleEnabled(false);

    appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
      private State state;
      private int prvVerticalOffset = 0;
      @Override
      public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (verticalOffset == 0) {
          if (state != State.EXPANDED) {
            collapsingToolbar.setTitleEnabled(false);
          }
          state = State.EXPANDED;
          // button slide up
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
          if (state != State.COLLAPSED) {
            collapsingToolbar.setTitleEnabled(true);
          }
          state = State.COLLAPSED;
        } else {
          if (state != State.IDLE) {
            collapsingToolbar.setTitleEnabled(false);
          }
          state = State.IDLE;
          // button slide up
        }

        // Log.d("DEBUG", "offset - " + verticalOffset);
      }
    });
  }

  private void showProgressBar(){
    progressBar.setVisibility(View.VISIBLE);
  }

  private void hideProgressBar(){
    progressBar.setVisibility(View.INVISIBLE);
  }

  private void loadEvent(String eventObjectId){
    // progress bar
    showProgressBar();

    ParseQuery<Event> query = ParseQuery.getQuery(Event.class);
    // Include the post data with each comment
    query.include(Event.KEY_HOST_USER);
    query.getInBackground(eventObjectId, new GetCallback<Event>() {
      @Override
      public void done(Event event, ParseException e) {
        hideProgressBar();

        if(e != null){
          return;
        }

        mEvent = event;

        String title = event.getTitle();
        collapsingToolbar.setTitle(title);
        tvEventTitle.setText(title);
        tvLocality.setText(event.getLocality());
        tvEventDate.setText(DateTimeUtils.formatDate(event.getDate()));
        if(event.getEventTimeFrom() != null){
          tvTime.setText(String.format("%s - %s",
              DateTimeUtils.formatHourInAmPm(event.getEventTimeFrom()),
              DateTimeUtils.formatHourInAmPm(event.getEventTimeTo())));
        }
        // address
        if(event.getStreetAddress() == null){
          tvStreet.setVisibility(View.GONE);
        }else {
          tvStreet.setText(event.getStreetAddress());
        }

        tvCityStateZip.setText(String.format("%s, %s", event.getLocality(), event.getAdministrativeArea()));
        tvEventDescription.setText(event.getDescription());

        if(event.getHostUser() != null){
          if(event.getHostUser().getFirstName() != null){
            tvHostName.setText(event.getHostUser().getFirstName());
          }

        }

        // Menu
        if(event.getEventMenu() != null){
          tvMenu.setText(event.getEventMenu());
        }else{
          tvMenu.setText(R.string.no_menu);
        }

        if(event.getEventImageUrl() == null){
          Picasso.with(getApplicationContext())
              .load(R.drawable.ev_image)
              .fit()
              .into(ivEventImage);
        }else{
          Picasso.with(getApplicationContext())
              .load(event.getEventImageUrl())
              .fit()
              .into(ivEventImage);
        }

        //enable button
        btnReserve.setEnabled(true);
      }
    });
  }

  private enum State {
    EXPANDED,
    COLLAPSED,
    IDLE
  }
}
