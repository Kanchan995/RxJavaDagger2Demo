package com.example.rxjavademo.ui.detail

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxjavademo.data.model.Article
import javax.inject.Inject

class DetailsViewModel @Inject constructor(): ViewModel() {

    private val selectedArticle: MutableLiveData<Article> = MutableLiveData()

    fun getSelectedArticle(): LiveData<Article> {
        return selectedArticle
    }

    fun setSelectedArticle(article: Article) {
        selectedArticle.setValue(article)
    }

    fun saveToBundle(outState: Bundle) {
        if (selectedArticle.getValue() != null) {
            outState.putParcelable(
                "news_details",
                selectedArticle.getValue()
            )
        }
    }

    fun restoreFromBundle(savedInstanceState: Bundle?) {
        if (selectedArticle.getValue() == null) {
            if (savedInstanceState != null && savedInstanceState.containsKey("news_details")) {
                selectedArticle.value=savedInstanceState.getParcelable("news_details")
            }
        }
    }
}
