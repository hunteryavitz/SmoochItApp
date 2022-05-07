package com.example.smoochit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UploadImageApiInterface {

    @FormUrlEncoded
    @POST("/uploadImage.php")
    Call<String> postImageUpload(@Field("image_data") String imageData);
}
