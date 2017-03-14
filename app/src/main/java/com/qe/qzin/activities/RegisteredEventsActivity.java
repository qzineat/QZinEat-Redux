package com.qe.qzin.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.qe.qzin.R;
import com.qe.qzin.adapters.RegisteredEventsAdapter;
import com.qe.qzin.listeners.EndlessRecyclerViewScrollListener;
import com.qe.qzin.models.Enrollment;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisteredEventsActivity extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.rvEvents) RecyclerView rvEvents;
  @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
  @BindView(R.id.progressBar) ProgressBar progressBar;

  private List<Event> mEvents;
  private RegisteredEventsAdapter registeredEventsAdapter;
  private EndlessRecyclerViewScrollListener scrollListener;
  private int displayLimit = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_registered_events);

    ButterKnife.bind(this);

    toolbar.setTitle("Registered Events");

    setSupportActionBar(toolbar);
    // add back arrow to toolbar
    if (getSupportActionBar() != null){
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    mEvents = new ArrayList<>();

    registeredEventsAdapter = new RegisteredEventsAdapter(getApplicationContext(),mEvents);
    rvEvents.setAdapter(registeredEventsAdapter);
    rvEvents.setLayoutManager(linearLayoutManager);

    addSwipeRefresh();
    addInfiniteScrolling(linearLayoutManager);

    loadEvents(0);
  }

  // Swipe Refresh
  private void addSwipeRefresh(){
    swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        registeredEventsAdapter.clear();
        loadEvents(0);
        swipeContainer.setRefreshing(false);
      }
    });

    // Configure the refreshing colors
    swipeContainer.setColorSchemeResources(
        android.R.color.holo_blue_bright,
        android.R.color.holo_green_light,
        android.R.color.holo_orange_light,
        android.R.color.holo_red_light);
  }

  // load data in recycleview on infinite scrolling
  private void addInfiniteScrolling(LinearLayoutManager linearLayoutManager){
    scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        loadEvents(page);
      }
    };

    rvEvents.addOnScrollListener(scrollListener);
  }

  private void loadEvents(int offset) {

    progressBar.setVisibility(View.VISIBLE);

    ParseQuery<Enrollment> query = ParseQuery.getQuery(Enrollment.class);
    query.whereEqualTo(Enrollment.KEY_USER, User.getCurrentUser());
    query.include(Enrollment.KEY_EVENT);
    query.setLimit(displayLimit);
    query.setSkip(offset * displayLimit);
    query.findInBackground(new FindCallback<Enrollment>() {
      @Override
      public void done(List<Enrollment> enrollments, ParseException e) {

        if(e == null){
          List<Event> eventList = new ArrayList<Event>();
          for(Enrollment enrollment: enrollments){
            if(enrollment.getEvent() != null){
              eventList.add(enrollment.getEvent());
            }
          }

          registeredEventsAdapter.addAll(eventList);
        }else {
          Toast.makeText(RegisteredEventsActivity.this, "Unable to get events!!", Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.INVISIBLE);
      }
    });
  }
}
