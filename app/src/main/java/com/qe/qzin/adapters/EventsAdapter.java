package com.qe.qzin.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qe.qzin.R;
import com.qe.qzin.activities.MainActivity;
import com.qe.qzin.listeners.OnUserEnrollmentListener;
import com.qe.qzin.models.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

  private List<Event> mEvents;
  private Context mContext;

  public static class ViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.ivEvent) ImageView ivEvent;
    @BindView(R.id.tvEventTitle) TextView tvEventTitle;
    @BindView(R.id.tvLocality) TextView tvLocality;
    @BindView(R.id.tvEventDate) TextView tvEventDate;
    @BindView(R.id.tvEventDescription) TextView tvEventDescription;
    @BindView(R.id.ivShare) ImageView ivShare;
    @BindView(R.id.tvPrice) TextView tvPrice;
    @BindView(R.id.tvReserve) TextView tvReserve;


    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }

  }


  public EventsAdapter(Context context, List<Event> events) {
    mEvents = events;
    mContext = context;
  }


  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    View convetView = inflater.inflate(R.layout.item_event, parent, false);
    ViewHolder viewHolder = new ViewHolder(convetView);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, int position) {

    final Event event = mEvents.get(position);

    // populate the values from above event item
    if(event.getEventImageUrl() == null){
      // TODO: Fix this later - Remove loading remote image and have drawable placeholder in Picasso
      Picasso.with(mContext)
          .load("http://blog.logomyway.com/wp-content/uploads/2013/06/143.jpg")
          .fit()
          .into(viewHolder.ivEvent);
    }else{
      Picasso.with(mContext)
          .load(event.getEventImageUrl())
          .fit()
          .into(viewHolder.ivEvent);
    }

    //set event title, description, locality.
    viewHolder.tvEventTitle.setText(event.getTitle());
    viewHolder.tvLocality.setText(event.getLocality());
    viewHolder.tvEventDescription.setText(event.getDescription());

    //get currency
    // TODO: update this using Java currency
    String currSymbol ="$";
    if(event.getCurrency() != null) {
      switch (event.getCurrency()) {
        case "USD":
          currSymbol = "$";
          break;

        default:
          currSymbol = "$";
          break;
      }
    }

    if((Double)event.getAmount() != null) {
      int priceInt = (int) event.getAmount();
      if (priceInt == event.getAmount()) { // if whole amount. eg $40
        viewHolder.tvPrice.setText(currSymbol + String.format("%d", priceInt));
      } else { // if amount is not whole amount. eg $40.10
        viewHolder.tvPrice.setText(currSymbol + String.format("%.2f", event.getAmount()));
      }
    }

    if(event.getDate() !=null) {
      DateFormat df = new DateFormat();
      viewHolder.tvEventDate.setText(df.format("dd MMM yyyy", event.getDate()));
    }

    // on Reserve click - user enrollment for the event.
    viewHolder.tvReserve.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
          if(mContext instanceof MainActivity){
            OnUserEnrollmentListener listener = (OnUserEnrollmentListener) mContext;
            listener.onUserEnrollment(event);
          }
      }
    });

  }

  @Override
  public int getItemCount() {
    return mEvents.size();
  }

  public void clear() {
    mEvents.clear();
    notifyDataSetChanged();
  }

  public void addAll(List<Event> list){
    mEvents.addAll(list);
    notifyItemRangeInserted(getItemCount(), list.size());
  }
}
