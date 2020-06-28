package com.example.rxjavademo.ui.list

import com.example.rxjavademo.data.model.Article

interface NewsItemSelectionListerner {
    fun onNewsSelected(article: Article)
}