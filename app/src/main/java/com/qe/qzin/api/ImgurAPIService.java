package com.qe.qzin.api;

import com.qe.qzin.models.imgur.ImgurResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Shyam Rokde on 3/1/17.
 */

public interface ImgurAPIService {
  String baseUrl = "https://api.imgur.com/";
  String clientId = "c785ade02dd67d0";
  String clientSecret = "cb5c43dcf9512f0cc044f070ffb7275ddc4456ee";
  //state=qzin&code=
  String authCode = "d4c0e1f1c63397b0d6d71286a4b5da3a6beebc45";
  String grantType = "refresh_token";
  String refreshToken = "2990e10717d34527c711501abda8f1ec26dc6dea";

  @Multipart
  @POST("3/image")
  Call<ImgurResponse> uploadImage(@Header("Authorization") String auth, @Part("image") RequestBody image);
}
