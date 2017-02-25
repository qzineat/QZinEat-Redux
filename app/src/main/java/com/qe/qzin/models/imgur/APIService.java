package com.qe.qzin.models.imgur;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Shyam Rokde on 2/19/17.
 */

public interface APIService {
  String server = "https://api.imgur.com/";
  String clientId = "c785ade02dd67d0";
  String clientSecret = "cb5c43dcf9512f0cc044f070ffb7275ddc4456ee";
  //state=qzin&code=
  String authCode = "d4c0e1f1c63397b0d6d71286a4b5da3a6beebc45";
  String grantType = "refresh_token";
  String refreshToken = "2990e10717d34527c711501abda8f1ec26dc6dea";


  @Multipart
  @POST("3/image")
  Call<ImgurResponse> uploadImage(@Header("Authorization") String auth, @Part("image") RequestBody image);

  @Multipart
  @POST("3/album/1ATvN/add")
  Call<ImgurResponse> uploadImageToAlbum(@Header("Authorization") String auth, @Part("image") RequestBody image);


  @FormUrlEncoded
  @POST("oauth2/token")
  Call<OAuthResponse> getAccessToken(@Field("client_id") String clientId, @Field("client_secret") String clientSecret, @Field("grant_type") String grantType, @Field("refresh_token") String authCode);
}
