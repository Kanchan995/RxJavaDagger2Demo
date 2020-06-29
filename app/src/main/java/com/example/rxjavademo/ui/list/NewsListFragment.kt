package com.example.rxjavademo.ui.list

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxjavademo.R
import com.example.rxjavademo.base.BaseFragment
import com.example.rxjavademo.data.model.Article
import com.example.rxjavademo.data.model.NewsResponse
import com.example.rxjavademo.data.rest.Output
import com.example.rxjavademo.ui.detail.DetailsViewModel
import com.example.rxjavademo.ui.detail.NewsDetailFragment
import com.example.rxjavademo.utils.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_news_list.*
import javax.inject.Inject


class NewsListFragment : BaseFragment(), NewsItemSelectionListerner {


    lateinit var viewModel: NewListViewModel

    lateinit var viewModelFactory: ViewModelFactory
        @Inject set

    val recyclerViewAdapter = NewsRecyclerViewAdapter(arrayListOf(), this)

    val gson = Gson()

    override fun layoutRes(): Int {
        return R.layout.fragment_news_list;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.apply {

            this.addItemDecoration(
                DividerItemDecoration(
                    getBaseActivity(),
                    DividerItemDecoration.VERTICAL
                )
            )
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = recyclerViewAdapter
        }
        setLocalDataUntilFromSealedClass()
        callObserveViewModel()
    }


    private fun callObserveViewModel() {
        getBaseActivity()?.let {
            viewModel =
                ViewModelProviders.of(it, viewModelFactory).get(NewListViewModel::class.java)

            viewModel.fetchNewList(COUNTRY, API_KEY).observe(it, Observer {
                when (it) {
                    is Output.Success -> {
                        if (it.data.articles.isNotEmpty()) {
                            loading_view.visibility = View.GONE
                            context?.let { it1 ->
                                SharedPref.storeString(
                                    it1,
                                    SharedPref.NEWS_RESPONSE,
                                    gson.toJson(it.data)
                                )
                            }
                            recyclerViewAdapter.newsRefresh(it.data.articles)
                        }
                    }
                    is Output.Failure -> {
                        if (it.e != null) {
                            loading_view.visibility = View.GONE
                        }
                    }
                }
            })
        }

    }

    override fun onNewsSelected(article: Article) {
        Toast.makeText(context, "", Toast.LENGTH_LONG).show()
        if (article != null) {
            val detailsViewModel: DetailsViewModel =
                ViewModelProviders.of(getBaseActivity()!!, viewModelFactory).get(
                    DetailsViewModel::class.java
                )
            detailsViewModel.setSelectedArticle(article)
            getBaseActivity()!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, NewsDetailFragment())
                .addToBackStack(null).commit()
        }
    }


    private fun setLocalDataUntilFromSealedClass() {
        if (!InternetUtil.isInternetOn()) {
            val jsonString = context?.let { it1 ->
                SharedPref.getStringValue(
                    it1,
                    SharedPref.NEWS_RESPONSE
                )
            }.toString()
            if (!jsonString.isEmpty()) {
                val token = object : TypeToken<NewsResponse>() {}.type
                val newsResponse: NewsResponse = gson.fromJson<NewsResponse>(jsonString, token)
                recyclerViewAdapter.newsRefresh(newsResponse.articles)
            }
        }
    }

}