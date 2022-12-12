package com.practice.wastemanagment;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    String BASE_URL="https://newsdata.io/api/1/";





    @GET("news")
    Call<ModelNews2> getNews (
            @Query("apikey") String apikey,
            @Query("country") String con,
            @Query("category") String category,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("news")
    Call<ModelNews2> getLanguageNews (
            @Query("apikey") String apikey,
            @Query("language") String language
    );
}
