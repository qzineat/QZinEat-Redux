package com.qe.qzin.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qe.qzin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shyam Rokde on 3/3/17.
 */

public class HostedEventsViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.ivEventImage) ImageView ivEventImage;
  @BindView(R.id.tvEventTitle) TextView tvEventTitle;
  @BindView(R.id.tvLocality) TextView tvLocality;
  @BindView(R.id.tvEventDate) TextView tvEventDate;
  @BindView(R.id.tvGuestCountData) TextView tvGuestCountData;

  public HostedEventsViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }
}
