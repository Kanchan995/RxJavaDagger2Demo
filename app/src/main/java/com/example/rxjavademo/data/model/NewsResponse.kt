package com.example.rxjavademo.data.model

import java.util.ArrayList

data class NewsResponse(
    val articles: ArrayList<Article>,
    val status: String,
    val totalResults: Int
)