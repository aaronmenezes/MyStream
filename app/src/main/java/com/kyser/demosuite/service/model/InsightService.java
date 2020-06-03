package com.kyser.demosuite.service.model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InsightService {

    @GET("getSelectionHistory")
    Call<List<InsightModel>> getSelectionHistory(@Query("filter") String filter);
}
