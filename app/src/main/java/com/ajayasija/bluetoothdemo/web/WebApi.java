package com.ajayasija.bluetoothdemo.web;

import com.ajayasija.bluetoothdemo.model.ServerResposnse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WebApi {

    @FormUrlEncoded
    @POST("bluetooth_bakend.php")
    Call<ServerResposnse> uploadData(@Field("data") String data,
                                     @Field("location") String location);
}
