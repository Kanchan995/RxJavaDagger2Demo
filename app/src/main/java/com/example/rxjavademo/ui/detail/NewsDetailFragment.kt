package com.example.rxjavademo.ui.detail


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.rxjavademo.R
import com.example.rxjavademo.base.BaseFragment
import com.example.rxjavademo.utils.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_news_detail.*
import kotlinx.android.synthetic.main.fragment_news_detail.view.*
import javax.inject.Inject

class NewsDetailFragment : BaseFragment() {

    @Inject
    lateinit var detailsViewModel: DetailsViewModel

    override fun layoutRes(): Int {
        return R.layout.fragment_news_detail
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getBaseActivity()?.let {
            detailsViewModel = ViewModelProviders.of(it).get(
                DetailsViewModel::class.java
            )
            detailsViewModel!!.restoreFromBundle(savedInstanceState)
            getBaseActivity()?.let {
                detailsViewModel!!.getSelectedArticle().observe(it, Observer { article ->
                    if (article != null) {
                        Glide.with(it)
                            .load(article.urlToImage)
                            .into(view.article_image)
                        view.article_title.text = article.title
                        view.description.text = article.description
                        view.author.text = article.author
                        view.publish_date_time.text = article.publishedAt
                    }
                })
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        detailsViewModel!!.saveToBundle(outState!!)
    }

    private fun displayArticle() {

    }
}