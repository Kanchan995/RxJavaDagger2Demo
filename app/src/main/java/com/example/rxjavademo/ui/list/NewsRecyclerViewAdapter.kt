package com.example.rxjavademo.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rxjavademo.R
import com.example.rxjavademo.data.model.Article
import com.example.rxjavademo.data.rest.Output
import com.example.rxjavademo.databinding.NewArticleItemBinding
import com.example.rxjavademo.utils.API_KEY
import com.example.rxjavademo.utils.COUNTRY
import kotlinx.android.synthetic.main.fragment_news_detail.*
import kotlin.collections.ArrayList

class NewsRecyclerViewAdapter(
    val articleListL: ArrayList<Article>,
    val itemSelectionListerner: NewsItemSelectionListerner
) :
    RecyclerView.Adapter<NewsRecyclerViewAdapter.NewsViewHolder>() {


    inner class NewsViewHolder : RecyclerView.ViewHolder {
        internal var newItemBinding: NewArticleItemBinding? = null

        constructor(binding: NewArticleItemBinding) : super(binding.root) {
            newItemBinding = binding
        }

        fun bind(article: Article, itemSelectionListerner: NewsItemSelectionListerner) {
            this.newItemBinding?.article = article

            this.newItemBinding?.newsImage?.context?.let {
                this.newItemBinding?.newsImage?.let { it2 ->
                    Glide.with(it)
                        .load(article.urlToImage)
                        .into(it2)
                }
            }

            if (this.newItemBinding?.article != null) {
                newItemBinding?.root?.setOnClickListener {
                    if (it != null && article != null) {
                        itemSelectionListerner.onNewsSelected(article)
                    }
                }
            }
        }
    }

    fun newsRefresh(latestNewsArticle: List<Article>) {
        articleListL.clear()
        articleListL.addAll(latestNewsArticle)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<NewArticleItemBinding>(
            inflater,
            R.layout.new_article_item,
            parent,
            false
        )
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return articleListL.size
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(articleListL[position], itemSelectionListerner);

    }
}