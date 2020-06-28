package com.example.rxjavademo.ui.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxjavademo.data.model.NewsResponse
import com.example.rxjavademo.data.rest.Errors
import com.example.rxjavademo.data.rest.NewsRepository
import com.example.rxjavademo.data.rest.Output
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewListViewModel @Inject constructor(val newsRepository: NewsRepository) : ViewModel() {

    private var disposable: CompositeDisposable? = CompositeDisposable()

    private var newsResponse = MutableLiveData<Output<NewsResponse>>()


    fun fetchNewList(country: String, apiKey: String): MutableLiveData<Output<NewsResponse>> {

        disposable?.add(
            newsRepository.getTopHeadlines(country, apiKey).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(object :
                    DisposableSingleObserver<NewsResponse>() {
                    override fun onSuccess(value: NewsResponse) {
                        newsResponse.value = Output.success(value)
                    }

                    override fun onError(e: Throwable) {
                        e.message?.let {
                            val error = Errors("500", it)
                            newsResponse.value = Output.failure(error)
                        }
                    }
                })
        )
        return newsResponse;
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }
}