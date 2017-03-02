package com.qe.qzin.models.imgur;

/**
 * Created by Shyam Rokde on 3/1/17.
 */

public class ImgurResponse {
  private boolean success;
  private int status;
  private ImageData data;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public ImageData getData() {
    return data;
  }

  public void setData(ImageData data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "ImgurResponse{" +
        "success=" + success +
        ", status=" + status +
        ", data=" + data.toString() +
        '}';
  }
}
