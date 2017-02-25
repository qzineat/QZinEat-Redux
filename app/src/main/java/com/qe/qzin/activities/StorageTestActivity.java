package com.qe.qzin.activities;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qe.qzin.R;
import com.qe.qzin.models.imgur.ImgurResponse;
import com.qe.qzin.models.imgur.OAuthResponse;
import com.qe.qzin.services.UploadService;
import com.qe.qzin.util.BitmapScaler;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StorageTestActivity extends AppCompatActivity implements Callback<ImgurResponse> {

  @BindView(R.id.ivEventImage) ImageView ivEventImage;
  @BindView(R.id.btnUpload) Button btnUpload;

  private FirebaseStorage storage = FirebaseStorage.getInstance();

  private static final int SELECT_PICTURE = 100;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_storage_test);
    ButterKnife.bind(this);


    // load image from firebase
    loadImageFromFirebase();


    ivEventImage.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
      }
    });

    btnUpload.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {

        /*
        // resize image before uploading

        ivEventImage.setDrawingCacheEnabled(true);
        ivEventImage.buildDrawingCache();

        Bitmap bitmap = ivEventImage.getDrawingCache();
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);


        //byte[] data = baos.toByteArray();

        Log.i("BITMAP", "width: " + bitmap.getWidth() + " Height: " + bitmap.getHeight());

        bitmap = BitmapScaler.scaleToFill(bitmap, 800, 700);

        Log.i("BITMAP2", "width: " + bitmap.getWidth() + " Height: " + bitmap.getHeight());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

        ivEventImage.setDrawingCacheEnabled(false);
        byte[] data = baos.toByteArray();

        */

        if(imageBitmap != null){
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
          byte[] data = baos.toByteArray();
          Log.i("BITMAP2", "width: " + imageBitmap.getWidth() + " Height: " + imageBitmap.getHeight());
          Log.i("BITMAP2", "size: " +imageBitmap.getByteCount());
          //uploadToImgur(data);
          getAccessToken(data);
        }

        //uploadToFirebase(data);
        //uploadToImgur(data);

        //getAccessToken();
      }
    });





  }


  Bitmap imageBitmap = null;

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(resultCode == RESULT_OK){
      if(requestCode == SELECT_PICTURE){

        Uri selectedImageUri = data.getData();
        if (null != selectedImageUri) {
          // Get the path from the Uri
          //getPathFromURI(selectedImageUri);
          //Log.i("IMAGE PATH TAG", "Image Path : " + localImagePath);


          // Resize Image
          Bitmap bitmap = null;
          try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
            Log.i("BITMAP", "width: " + bitmap.getWidth() + " Height: " + bitmap.getHeight());
            Log.i("BITMAP", "size: " +bitmap.getByteCount());

            if(bitmap.getByteCount() > (1024 * 512)){
              // resize
              imageBitmap = BitmapScaler.scaleToFill(bitmap, 800, 700);
            }else{
              imageBitmap = bitmap;
            }


          } catch (IOException e) {
            e.printStackTrace();
          }


          // Set the image in ImageView
          ivEventImage.setImageBitmap(imageBitmap);
          //ivEventImage.setImageURI(selectedImageUri);
        }


      }
    }
  }

  private String getPathFromURI(Uri contentUri) {
    String res = null;
    String[] proj = {MediaStore.Images.Media.DATA};
    Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
    if (cursor.moveToFirst()) {
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      res = cursor.getString(column_index);
    }
    cursor.close();
    return res;
  }


  private void loadImageFromFirebase(){
    String url = "https://firebasestorage.googleapis.com/v0/b/qzineat-8809e.appspot.com/o/events%2FTuiOvrK6LJ%2F76ccbbe8-647f-4212-acb5-8f52af46f66e.png?alt=media&token=9576124f-407e-4e87-9bb8-2e2237dfc0ec";
  }


  private void getAccessToken(final byte[] data){
    new UploadService().getAccessToken(new Callback<OAuthResponse>() {
      @Override
      public void onResponse(Call<OAuthResponse> call, Response<OAuthResponse> response) {
        if(response.isSuccessful()){
          Log.i("DEBUG", response.body().toString());
          uploadImage(data, response.body().getAccess_token());
        }
      }

      @Override
      public void onFailure(Call<OAuthResponse> call, Throwable t) {
        Log.i("DEBUG", t.toString());
      }
    });
  }

  private void uploadImage(byte[] data, String accessToken){
    new UploadService().executeUpload(data, accessToken, this);
  }

  private void uploadToImgur(byte[] data){
    new UploadService().execute(data, this);
  }


  private void uploadToFirebase(byte[] data){
    String eventId = "TuiOvrK6LJ";
    String path = "events/" + eventId + "/"+ UUID.randomUUID()+".png";

    StorageReference eventsImgRef = storage.getReference(path);

    UploadTask uploadTask = eventsImgRef.putBytes(data);

    uploadTask.addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(getApplicationContext(), "TASK FAILED", Toast.LENGTH_SHORT).show();
      }
    });
    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
      @Override
      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        Toast.makeText(getApplicationContext(), "TASK SUCCEEDED", Toast.LENGTH_SHORT).show();

        Uri downloadUrl =taskSnapshot.getDownloadUrl();
        String DOWNLOAD_URL = downloadUrl.getPath();
        Log.v("DOWNLOAD URL", DOWNLOAD_URL);
      }
    });

  }


  private void loadImage(String url){
    if(!url.isEmpty()){
      Picasso.with(getApplicationContext())
          .load(url)
          .fit()
          .into(ivEventImage);
    }
  }


  @Override
  public void onResponse(Call<ImgurResponse> call, Response<ImgurResponse> response) {

    Log.i("DEBUG", response.body().toString());


    if(response.isSuccessful()){
      Toast.makeText(getApplicationContext(), "Successfully uploaded file", Toast.LENGTH_LONG).show();

    }else{
      Toast.makeText(getApplicationContext(), "Error uploading file: " + response.code(), Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onFailure(Call<ImgurResponse> call, Throwable t) {
    Toast.makeText(getApplicationContext(), "Error uploading file", Toast.LENGTH_LONG).show();

    Log.i("DEBUG", t.toString());
  }
}
