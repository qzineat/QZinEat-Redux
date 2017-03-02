package com.qe.qzin.models.imgur;

/**
 * Created by Shyam Rokde on 3/1/17.
 */

public class ImageData {
  private String id;
  private String title;
  private String description;
  private String type;
  private boolean animated;
  private int width;
  private int height;
  private int size;
  private int views;
  private int bandwidth;
  private String vote;
  private boolean favorite;
  private String account_url;
  private String deletehash;
  private String name;
  private String link;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public boolean isAnimated() {
    return animated;
  }

  public void setAnimated(boolean animated) {
    this.animated = animated;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getViews() {
    return views;
  }

  public void setViews(int views) {
    this.views = views;
  }

  public int getBandwidth() {
    return bandwidth;
  }

  public void setBandwidth(int bandwidth) {
    this.bandwidth = bandwidth;
  }

  public String getVote() {
    return vote;
  }

  public void setVote(String vote) {
    this.vote = vote;
  }

  public boolean isFavorite() {
    return favorite;
  }

  public void setFavorite(boolean favorite) {
    this.favorite = favorite;
  }

  public String getAccount_url() {
    return account_url;
  }

  public void setAccount_url(String account_url) {
    this.account_url = account_url;
  }

  public String getDeletehash() {
    return deletehash;
  }

  public void setDeletehash(String deletehash) {
    this.deletehash = deletehash;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLink() {
    return link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  @Override public String toString() {
    return "ImageData{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", type='" + type + '\'' +
        ", animated=" + animated +
        ", width=" + width +
        ", height=" + height +
        ", size=" + size +
        ", views=" + views +
        ", bandwidth=" + bandwidth +
        ", vote='" + vote + '\'' +
        ", favorite=" + favorite +
        ", account_url='" + account_url + '\'' +
        ", deletehash='" + deletehash + '\'' +
        ", name='" + name + '\'' +
        ", link='" + link + '\'' +
        '}';
  }
}
