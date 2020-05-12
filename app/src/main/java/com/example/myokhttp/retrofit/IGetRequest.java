package com.example.myokhttp.retrofit;

import com.example.myokhttp.retrofit.bean.GetBean;
import com.example.myokhttp.retrofit.bean.GetBean01;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * create by zhangshi on 2020/4/21.
 */
interface IGetRequest {
    @FormUrlEncoded
    @POST("openapi.do?keyfrom=abc&key=2032414398&type=data&doctype=json&version=1.1&q=car")
    Call<GetBean> getCall(@Field("name") String name);


    @GET("query")
    Call<GetBean01> getCall01(@Query("type") String type,
                              @Query("postid") String postid);

    @POST
    Call<String>
}
