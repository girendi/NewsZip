package com.girendi.newszip.presentation.main

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.girendi.newszip.R
import com.girendi.newszip.core.data.ui.CustomTextWatcher
import com.girendi.newszip.core.data.ui.SimpleRecyclerAdapter
import com.girendi.newszip.core.domain.UiState
import com.girendi.newszip.core.domain.model.Category
import com.girendi.newszip.core.domain.model.Source
import com.girendi.newszip.databinding.ActivityMainBinding
import com.girendi.newszip.databinding.ItemCategoryListBinding
import com.girendi.newszip.databinding.ItemNewsListBinding
import com.girendi.newszip.presentation.articles.ArticleActivity
import com.girendi.newszip.presentation.articles.ArticleWebViewActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity: AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterCategory: SimpleRecyclerAdapter<Category>
    private lateinit var adapterSource: SimpleRecyclerAdapter<Source>
    private val viewModelMain: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleAdapter()
        handleObserveViewModel()
        handleSearch()
    }

    private fun handleObserveViewModel() {
        viewModelMain.listCategory.observe(this@MainActivity) { categories ->
            adapterCategory.setListItem(categories)
        }
        viewModelMain.listSource.observe(this@MainActivity) { source ->
            if (source != null) {
                adapterSource.setListItem(source)
                handleViewContent("Data Not Found!","", source.isEmpty())
            } else {
                handleViewContent("Data Not Found!")
            }
        }
        viewModelMain.uiState.observe(this@MainActivity) { state ->
            handleUiState(state)
        }
    }

    private fun handleAdapter() {
        adapterCategory = SimpleRecyclerAdapter(
            context = this@MainActivity,
            layoutResId = R.layout.item_category_list,
            bindViewHolder = { view, item ->
                val itemBinding = ItemCategoryListBinding.bind(view)
                itemBinding.tvTitle.text = item.category
                if (item.status) {
                    itemBinding.tvTitle.background = resources.getDrawable(R.drawable.custom_selected_text)
                    itemBinding.tvTitle.setTextColor(resources.getColor(R.color.white))
                } else {
                    itemBinding.tvTitle.background = resources.getDrawable(R.drawable.custom_unselected_text)
                    itemBinding.tvTitle.setTextColor(resources.getColor(R.color.grey))
                }
                itemBinding.root.setOnClickListener {
                    binding.etSearch.setText("")
                    viewModelMain.fetchSourceByCategory(item.category)
                }
            }
        )
        binding.rvListCategory.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = adapterCategory
        }

        adapterSource = SimpleRecyclerAdapter(
            context = this@MainActivity,
            layoutResId = R.layout.item_news_list,
            bindViewHolder = { view, item ->
                val itemBinding = ItemNewsListBinding.bind(view)
                itemBinding.tvTitle.text = item.name
                itemBinding.tvDescription.text = item.description
                itemBinding.tvLink.setOnClickListener {
                    item.url?.let { url -> openWebPage(url) }
                }
                itemBinding.root.setOnClickListener {
                    val intent = Intent(this, ArticleActivity::class.java)
                    intent.putExtra(ArticleActivity.EXTRA_SOURCE, item)
                    startActivity(intent)
                }
            }
        )
        binding.rvListNews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterSource
        }
    }

    private fun handleSearch() {
        val textWatcher = CustomTextWatcher{ text ->
            runOnUiThread{
                viewModelMain.filterSource(text)
            }
        }
        binding.etSearch.addTextChangedListener(textWatcher)
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
        binding.rvListNews.visibility = if (status) View.GONE else View.VISIBLE
        binding.contentEmpty.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(status: Boolean) {
        binding.progressBar.visibility = if (status) View.VISIBLE else View.GONE
    }

    private fun openWebPage(url: String) {
        val intent = Intent(this, ArticleWebViewActivity::class.java)
        intent.putExtra(ArticleWebViewActivity.EXTRA_URL, url)
        startActivity(intent)
    }
}