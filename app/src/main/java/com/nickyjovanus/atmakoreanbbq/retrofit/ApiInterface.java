package com.nickyjovanus.atmakoreanbbq.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("menu")
    Call<MenuResponse> getAllMenu(@Query("data") String data);

    @GET("menu/{id}")
    Call<MenuResponse> getMenuById(@Path("id") String id, @Query("data") String data);

    @GET("meja")
    Call<MejaResponse> getAllMeja(@Query("data") String data);
}
