package com.example.nigel;

import com.example.nigel.Baby;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

//https://reintech.io/blog/developing-android-app-restful-backend-flask-flask-restful
public interface BabyApi {

    // Makes a PUT request to add a Baby
    @PUT("/addBaby")
    Call<ResponseBody> addBaby(@Body Baby baby);

}
