package com.qe.qzin.adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qe.qzin.R;
import com.qe.qzin.models.Event;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.facebook.FacebookSdk.getApplicationContext;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder>{

/*  ImageView ivEvent;
  TextView tvEventTitle;
  TextView tvLocality;
  TextView tvEventDate;
  TextView tvEventDescription;
  ImageView ivShare;
  TextView tvPrice;*/

  public static class ViewHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.ivEvent) ImageView ivEvent;
    @BindView(R.id.tvEventTitle) TextView tvEventTitle;
    @BindView(R.id.tvLocality) TextView tvLocality;
    @BindView(R.id.tvEventDate) TextView tvEventDate;
    @BindView(R.id.tvEventDescription) TextView tvEventDescription;
    @BindView(R.id.ivShare) ImageView ivShare;
    @BindView(R.id.tvPrice) TextView tvPrice;


    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this,view);
    }

  }

  private List<Event> mEvents;
  private Context mContext;


  public EventsAdapter(Context context, List<Event> events) {
    mContext = context;
    mEvents = events;
  }

  private Context getContext(){
    return mContext;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    View convetView = inflater.inflate(R.layout.item_event, parent, false);

    ViewHolder viewHolder = new ViewHolder(convetView);

    return viewHolder;
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, int position) {

    Event event = mEvents.get(position);

    ImageView ivEvent = viewHolder.ivEvent;
    TextView tvEventTitle = viewHolder.tvEventTitle;
    TextView tvLocality = viewHolder.tvLocality;
    TextView tvEventDate = viewHolder.tvEventDate;
    TextView tvEventDescription = viewHolder.tvEventDescription;
    ImageView ivShare = viewHolder.ivShare;
    TextView tvPrice = viewHolder.tvPrice;

    // Event
    Picasso.with(getApplicationContext())
        .load("http://blog.logomyway.com/wp-content/uploads/2013/06/143.jpg")
        .fit()
        .into(ivEvent);

    tvEventTitle.setText(event.getTitle());
    tvLocality.setText(event.getLocality());
    tvEventDescription.setText(event.getDescription());

    int priceInt =  (int) event.getAmount();
    if(priceInt == event.getAmount()) {
      tvPrice.setText(priceInt);
    }else {
      tvPrice.setText(String.format("%.2f", event.getAmount()));
    }

    android.text.format.DateFormat df = new android.text.format.DateFormat();
    tvEventDate.setText(df.format("dd MMM yyyy", event.getDate()));
  }

  @Override
  public int getItemCount() {
    return mEvents.size();
  }
}
