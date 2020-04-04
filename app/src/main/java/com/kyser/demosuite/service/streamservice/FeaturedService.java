package com.kyser.demosuite.service.streamservice;

import com.kyser.demosuite.service.model.FeaturedModel;
import com.kyser.demosuite.service.model.ListingModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FeaturedService {

    @GET("getFeatured")
    Call<List<FeaturedModel>> getFeatured();


    @GET("/getSynopsisModel/{cid}")
    Call<List<ListingModel>> getSynopsisModel(@Path("cid") int cid);

    @GET("/test")
    Call<ResponseBody> test();

}
