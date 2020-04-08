package com.kyser.demosuite.service.streamservice;

import com.kyser.demosuite.service.model.CategoryModel;
import com.kyser.demosuite.service.model.ListingModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AudioService {

    @GET("getVideoModel/{cid}")
    Call<List<CategoryModel>> getAudioModel(@Path("cid") int cid);

    @GET("getMediaModel/{cid}")
    Call<List<ListingModel>> getListingModel(@Path("cid") int cid);
}

