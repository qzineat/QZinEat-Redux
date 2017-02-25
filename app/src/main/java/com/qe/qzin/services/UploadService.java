package com.qe.qzin.services;

import com.qe.qzin.models.imgur.APIService;
import com.qe.qzin.models.imgur.ImgurResponse;
import com.qe.qzin.models.imgur.OAuthResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shyam Rokde on 2/19/17.
 */

public class UploadService {
  private final static String TAG = UploadService.class.getSimpleName();

  public void execute(byte[] data, Callback<ImgurResponse> callback){

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(APIService.server)
        .addConverterFactory(GsonConverterFactory.create())
        .build();


    // TODO: Resize Image and store as a name of event.
    String auth = "Client-ID " + APIService.clientId;
    MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    RequestBody file = RequestBody.create(MEDIA_TYPE_PNG, data);


    APIService service = retrofit.create(APIService.class);
    Call<ImgurResponse> call = service.uploadImage(auth, file);
    call.enqueue(callback);

    //retrofit.create(APIService.class).postImage(auth, file);
  }

  public void executeUpload(byte[] data, String accessToken, Callback<ImgurResponse> callback){
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(APIService.server)
        .addConverterFactory(GsonConverterFactory.create())
        .build();


    // TODO: Resize Image and store as a name of event.
    String auth = "Bearer " + accessToken;
    MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    RequestBody file = RequestBody.create(MEDIA_TYPE_PNG, data);


    APIService service = retrofit.create(APIService.class);
    Call<ImgurResponse> call = service.uploadImageToAlbum(auth, file);
    call.enqueue(callback);

  }


  public void getAccessToken(Callback<OAuthResponse> callback){
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(APIService.server)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    APIService service = retrofit.create(APIService.class);
    Call<OAuthResponse> call = service.getAccessToken(APIService.clientId, APIService.clientSecret, APIService.grantType, APIService.refreshToken);
    call.enqueue(callback);
  }



}
