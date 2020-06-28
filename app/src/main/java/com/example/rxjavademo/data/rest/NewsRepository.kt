package com.example.rxjavademo.data.rest

import com.example.rxjavademo.data.model.NewsResponse
import io.reactivex.Single
import javax.inject.Inject

open class NewsRepository @Inject constructor(val newsService: NewsService) {


    fun getTopHeadlines(country: String?, apiKey: String?): Single<NewsResponse?> {
        return newsService.getTopHeadLines(country, apiKey)
    }
}