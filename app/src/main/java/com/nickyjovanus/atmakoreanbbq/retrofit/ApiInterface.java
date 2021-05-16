package com.nickyjovanus.atmakoreanbbq.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("menu")
    Call<MenuResponse> getAllMenu(@Query("data") String data);
}
