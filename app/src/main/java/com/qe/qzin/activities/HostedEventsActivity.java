package com.qe.qzin.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.qe.qzin.R;
import com.qe.qzin.adapters.HostedEventsAdapter;
import com.qe.qzin.listeners.EndlessRecyclerViewScrollListener;
import com.qe.qzin.models.Event;
import com.qe.qzin.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HostedEventsActivity extends BaseActivity {

  @BindView(R.id.rvEvents) RecyclerView rvEvents;
  //@BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

  private List<Event> mEvents;
  private HostedEventsAdapter hostedEventsAdapter;
  private EndlessRecyclerViewScrollListener scrollListener;
  private int displayLimit = 10;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_hosted_events);

    ButterKnife.bind(this);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    toolbar.setTitle("Hosted Events");

    setSupportActionBar(toolbar);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    mEvents = new ArrayList<>();

    hostedEventsAdapter = new HostedEventsAdapter(getApplicationContext(), mEvents);
    rvEvents.setAdapter(hostedEventsAdapter);
    rvEvents.setLayoutManager(linearLayoutManager);

    // load data in recycleview on infinite scrolling
    scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
      @Override
      public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
        loadHostedEvents(page);
      }
    };

    rvEvents.addOnScrollListener(scrollListener);

    // load first page
    loadHostedEvents(0);
  }

  /**
   * Load Hosted Events
   * @param offset
   */
  private void loadHostedEvents(int offset){
    ParseQuery<Event> query = ParseQuery.getQuery("Event");
    query.whereEqualTo(Event.KEY_HOST_USER, User.getCurrentUser());
    query.setLimit(displayLimit);
    query.setSkip(offset * displayLimit);
    query.orderByAscending(Event.KEY_EVENT_DATE);

    query.findInBackground(new FindCallback<Event>() {
      @Override
      public void done(List<Event> eventList, ParseException e) {
        if(e == null){
          hostedEventsAdapter.addAll(eventList);
        }else{
          Log.e("ERROR", "Unable to load events from Parse: " +e.getMessage());
        }
      }
    });

  }
}
