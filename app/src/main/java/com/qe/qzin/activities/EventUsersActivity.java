package com.qe.qzin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.qe.qzin.R;
import com.qe.qzin.adapters.UserListAdapter;
import com.qe.qzin.listeners.EndlessRecyclerViewScrollListener;
import com.qe.qzin.models.Enrollment;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventUsersActivity extends AppCompatActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.rvUsers) RecyclerView rvUsers;

  private List<User> mUsers;
  private UserListAdapter userListAdapter;
  private EndlessRecyclerViewScrollListener scrollListener;
  private int displayLimit = 10;
  private String mEventTitle;
  private String mEventId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_users);

    ButterKnife.bind(this);

    // Get Data
    Intent intent = getIntent();
    if(intent != null){
      mEventTitle = intent.getStringExtra("eventTitle");
      mEventId = intent.getStringExtra("eventId");
    }


    toolbar.setTitle(mEventTitle);
    setSupportActionBar(toolbar);

    // add back arrow to toolbar
    if (getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    mUsers = new ArrayList<>();
    userListAdapter = new UserListAdapter(getApplicationContext(), mUsers);

    rvUsers.setAdapter(userListAdapter);
    rvUsers.setLayoutManager(linearLayoutManager);

    addInfiniteScrolling(linearLayoutManager);

    loadUserList(0);
  }

  private void addInfiniteScrolling(LinearLayoutManager linearLayoutManager) {
    // load data in recycleview on infinite scrolling
    scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        loadUserList(page);
      }
    };

    rvUsers.addOnScrollListener(scrollListener);
  }

  private void loadUserList(int offset) {


    ParseQuery<Event> eventQuery = ParseQuery.getQuery(Event.class);
    eventQuery.whereEqualTo("objectId", mEventId);

    ParseQuery<Enrollment> query = ParseQuery.getQuery(Enrollment.class);
    query.whereMatchesQuery(Enrollment.KEY_EVENT, eventQuery);
    query.include(Enrollment.KEY_USER);
    query.setLimit(displayLimit);
    query.setSkip(offset * displayLimit);
    query.findInBackground(new FindCallback<Enrollment>() {
      @Override
      public void done(List<Enrollment> enrollments, ParseException e) {
        if(e != null){
          Toast.makeText(EventUsersActivity.this, "Unable to get users!!", Toast.LENGTH_SHORT).show();
          return;
        }

        List<User> userList = new ArrayList<>();
        for(Enrollment enrollment: enrollments){
          if(enrollment.getEvent() != null){
            userList.add(enrollment.getEnrollUser());
          }
        }

        userListAdapter.addAll(userList);
      }
    });

  }
}
