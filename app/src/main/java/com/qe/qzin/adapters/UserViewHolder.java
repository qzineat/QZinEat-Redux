package com.qe.qzin.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qe.qzin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shyam Rokde on 3/13/17.
 */

public class UserViewHolder extends RecyclerView.ViewHolder {

  @BindView(R.id.ivProfileImage) ImageView ivProfileImage;
  @BindView(R.id.tvName) TextView tvName;

  public UserViewHolder(View itemView) {
    super(itemView);
    ButterKnife.bind(this, itemView);
  }
}
