package com.example.rxjavademo.data.rest

import com.example.rxjavademo.data.model.NewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface NewsService {

    @GET("/v2/top-headlines")
    fun getTopHeadLines(
        @Query("country") country: String?,
        @Query("apiKey") apiKey: String?
    ): Single<NewsResponse?>
}