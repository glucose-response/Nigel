package com.example.nigel;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface Api {
    @GET("download_template")
    Call<ResponseBody> downloadTemplate();

    @GET("download_all_data")
    Call<ResponseBody> downloadAllData();

    @Multipart
    @PUT("upload_data")
    Call<ResponseBody> uploadExcelFile(@Part MultipartBody.Part file);
}