package com.qe.qzin.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.qe.qzin.R;
import com.qe.qzin.models.Event;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends BaseActivity {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.appbar) AppBarLayout appbar;
  @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;

  private String eventObjectId;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_event_detail);

    ButterKnife.bind(this);

    setCollapsingToolbar();

    // Get EventId and load
    eventObjectId = getIntent().getExtras().getString("eventObjectId");
    loadEvent(eventObjectId);
  }

  private void setCollapsingToolbar(){
    toolbar.setTitle("");
    setSupportActionBar(toolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    // title
    collapsingToolbar.setTitle(getString(R.string.sample_party_title));
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

  private void loadEvent(String eventObjectId){
    ParseQuery<Event> query = ParseQuery.getQuery("Event");
    query.getInBackground(eventObjectId, new GetCallback<Event>() {
      @Override
      public void done(Event event, ParseException e) {
        if(e != null){
          return;
        }
        collapsingToolbar.setTitle(event.getTitle());
      }
    });
  }

  private enum State {
    EXPANDED,
    COLLAPSED,
    IDLE
  }
}
