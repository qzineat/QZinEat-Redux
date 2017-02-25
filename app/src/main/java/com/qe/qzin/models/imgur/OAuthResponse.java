package com.qe.qzin.models.imgur;

/**
 * Created by Shyam Rokde on 2/20/17.
 */

public class OAuthResponse {
  private String access_token;
  private String refresh_token;
  private String token_type;

  public String getAccess_token() {
    return access_token;
  }

  public void setAccess_token(String access_token) {
    this.access_token = access_token;
  }

  public String getRefresh_token() {
    return refresh_token;
  }

  public void setRefresh_token(String refresh_token) {
    this.refresh_token = refresh_token;
  }

  public String getToken_type() {
    return token_type;
  }

  public void setToken_type(String token_type) {
    this.token_type = token_type;
  }

  @Override
  public String toString() {
    return "OAuth{" +
        "access_token=" + access_token +
        ", refresh_token=" + refresh_token +
        ", token_type=" + token_type +
        '}';
  }
}
