package com.qe.qzin.services;

import com.qe.qzin.api.ImgurAPIService;
import com.qe.qzin.models.imgur.ImgurResponse;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shyam Rokde on 3/1/17.
 */

public class ImgurUploadService {
  private final static String TAG = ImgurUploadService.class.getSimpleName();

  public void execute(byte[] data, Callback<ImgurResponse> callback){
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(ImgurAPIService.baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    String auth = "Client-ID " + ImgurAPIService.clientId;
    MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    RequestBody file = RequestBody.create(MEDIA_TYPE_PNG, data);

    ImgurAPIService service = retrofit.create(ImgurAPIService.class);

    Call<ImgurResponse> call = service.uploadImage(auth, file);
    call.enqueue(callback);
  }
}
