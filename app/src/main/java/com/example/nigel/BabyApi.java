package com.example.nigel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface BabyApi {
    @GET
    Call<ResponseBody> downloadFile(@Url String fileUrl);
}