package com.example.nigel;

import com.example.nigel.Baby;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/** This class is used to communicate with the database
 * Sourced: https://reintech.io/blog/developing-android-app-restful-backend-flask-flask-restful*/
public interface BabyApi {

    /**
     * Makes a PUT request to add a Baby
     * @input a Baby object
     */
    @PUT("/addBaby")
    Call<ResponseBody> addBaby(@Body Baby baby);

    /**
     * Makes a GET request to get a Baby
     * @return a list of JSON Baby Files
     */
    @GET("/profiles")
    Call<ResponseBody> getBabies();

    /**
     * Makes a GET request to receive all data
     * @return a list of JSON Baby Files
     */
    @GET("/bsp")
    Call<ResponseBody> getData();

//    Requests related to the UploadDownloadFragment

    /**
     * Makes a GET request to download an excel template for users to input their data
     * @return an excel spreadsheet saved in Documents/Nigel in the Files App
     */
    @GET("download_template")
    Call<ResponseBody> downloadTemplate();

    /**
     * Makes a GET request to download an excel with all the data on the Mongodb database
     * @return an excel spreadsheet saved in Documents/Nigel in the Files App
     */
    @GET("download_all_data")
    Call<ResponseBody> downloadAllData();

    /**
     * Makes a PUT request to send the data to the Mongodb database
     * @input an excel spreadsheet with all the data
     */
    @Multipart
    @PUT("upload_data")
    Call<ResponseBody> uploadExcelFile(@Part MultipartBody.Part file);

}
