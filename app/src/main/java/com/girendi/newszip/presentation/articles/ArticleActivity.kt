package com.girendi.newszip.presentation.articles

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.girendi.newszip.R
import com.girendi.newszip.core.data.ui.CustomTextWatcher
import com.girendi.newszip.core.data.ui.SimpleRecyclerAdapter
import com.girendi.newszip.core.domain.UiState
import com.girendi.newszip.core.domain.model.Article
import com.girendi.newszip.core.domain.model.Source
import com.girendi.newszip.databinding.ActivityArticleBinding
import com.girendi.newszip.databinding.ItemArticleListBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleActivity: AppCompatActivity() {

    private lateinit var binding: ActivityArticleBinding
    private lateinit var adapterArticles: SimpleRecyclerAdapter<Article>
    private val viewModelArticle: ArticleViewModel by viewModel()
    private var source: Source? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        source = intent.getParcelableExtra(EXTRA_SOURCE)
        if (source != null) {
            supportActionBar?.title = source?.name
        }
        binding.toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
        handleAdapter()
        handleObserveViewModel()
        handleSearch()
    }

    private fun handleSearch() {
        val textWatcher = CustomTextWatcher{
            runOnUiThread{
                viewModelArticle.resetCurrentPage()
                handleReloadArticle()
            }
        }
        binding.etSearch.addTextChangedListener(textWatcher)
    }

    private fun handleAdapter() {
        adapterArticles = SimpleRecyclerAdapter(
            context = this@ArticleActivity,
            layoutResId = R.layout.item_article_list,
            bindViewHolder = { view, item ->
                val itemBinding = ItemArticleListBinding.bind(view)
                Glide.with(this)
                    .load(item.urlToImage)
                    .into(itemBinding.imageView)
                itemBinding.tvTitle.text = item.title
                itemBinding.tvDateTime.text = viewModelArticle.changeDateFormat(item.publishedAt)
                itemBinding.root.setOnClickListener {
                    val intent = Intent(this, ArticleWebViewActivity::class.java)
                    intent.putExtra(ArticleWebViewActivity.EXTRA_URL, item.url)
                    startActivity(intent)
                }
            }
        )
        binding.rvListArticles.apply {
            layoutManager = LinearLayoutManager(this@ArticleActivity)
            adapter = adapterArticles
        }
        binding.rvListArticles.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!binding.rvListArticles.canScrollVertically(1)) {
                        handleReloadArticle()
                    }
                }
            }
        )
    }

    private fun handleObserveViewModel() {
        viewModelArticle.listArticle.observe(this@ArticleActivity) { articles ->
            if (articles != null) {
                adapterArticles.setListItem(articles)
                handleViewContent("Data Not Found!","", articles.isEmpty())
            } else {
                handleViewContent("Data Not Found!")
            }
        }
        viewModelArticle.uiState.observe(this@ArticleActivity) { state ->
            handleUiState(state)
        }
        handleReloadArticle()
    }

    private fun handleReloadArticle() {
        source?.let { viewModelArticle.fetchArticles(it.id, binding.etSearch.text.toString()) }
    }

    private fun handleUiState(state: UiState) {
        when (state) {
            is UiState.Loading -> showLoading(true)
            is UiState.Success -> showLoading(false)
            is UiState.Error -> {
                showLoading(false)
                handleViewContent(state.message)
            }
        }
    }

    private fun handleViewContent(message: String) {
        handleViewContent("", message, true)
    }

    private fun handleViewContent(title: String, message: String, status: Boolean) {
        binding.errorMessage.text = message
        if (title != "") {
            binding.tvErrorTitle.text = title
        }
        binding.rvListArticles.visibility = if (status) View.GONE else View.VISIBLE
        binding.contentEmpty.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun showLoading(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_SOURCE = "extraSource"
    }
}