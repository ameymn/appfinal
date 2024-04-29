package com.example.jeeevandan;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {
    @FormUrlEncoded
    @POST("/driver/driverlog")
    Call<Data> login(@Field("id") String id, @Field("password") String password);

    @FormUrlEncoded
    @POST("/api/location")
    Call<Location> location(@Field("latitude") Double latitude, @Field("longitude") Double longitude);

    @FormUrlEncoded
    @POST("/driver/getDriver")
    Call<Driver> driver(@Field("id") String id);

}

