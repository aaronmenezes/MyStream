package com.kyser.demosuite.service.streamservice

import com.kyser.demosuite.service.model.ListingModel
import com.kyser.demosuite.service.model.UploadModel
import retrofit2.Call
import okhttp3.MultipartBody
import retrofit2.http.*


interface SearchService {

    @GET("getSearchMedia/{title}")
    fun getSearchMedia(@Path("title") title: String): Call<List<ListingModel>>

    @Multipart
    @POST("posterUpdate")
    fun posterUpdate(@Part filePart: MultipartBody.Part, @Part mid:  MultipartBody.Part): Call<UploadModel>
}