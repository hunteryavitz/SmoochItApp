package com.example.smoochit;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostImageRequest {

    Call<String> uploadImageCall;

    public PostImageRequest(String data) throws IOException {

        UploadImageApiInterface uploadImageApiInterface =
                UploadImageApiClient
                    .getRetrofit()
                    .create(UploadImageApiInterface.class);

        uploadImageCall = uploadImageApiInterface.postImageUpload(data);

    }

    public String postCall() {
        uploadImageCall.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
        return "nullish";
    }
}
