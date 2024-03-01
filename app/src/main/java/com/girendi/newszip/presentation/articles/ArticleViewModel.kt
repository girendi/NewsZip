package com.girendi.newszip.presentation.articles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girendi.newszip.core.domain.Result
import com.girendi.newszip.core.domain.UiState
import com.girendi.newszip.core.domain.model.Article
import com.girendi.newszip.core.domain.usecase.FetchArticleBySourceUseCase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class ArticleViewModel(private val fetchArticleBySourceUseCase: FetchArticleBySourceUseCase): ViewModel() {
    private val _listArticle = MutableLiveData<List<Article>>()
    val listArticle: LiveData<List<Article>> = _listArticle

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private var currentPage = 1
    private var isLastPage = false

    fun fetchArticles(sourceId: String, query: String) {
        if (isLastPage) {
            _uiState.postValue(UiState.Success)
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when(val result = fetchArticleBySourceUseCase.fetchArticleBySource(sourceId, query, currentPage)) {
                is Result.Success -> {
                    val articles = _listArticle.value.orEmpty() + result.data.articles
                    _listArticle.value = articles
                    _uiState.value = UiState.Success
                    isLastPage = result.data.articles.isEmpty()
                    currentPage++
                }
                is Result.Error -> {
                    _uiState.value = UiState.Error(result.exception.message ?: "An unknown error occurred")
                }
                is Result.Loading -> {
                    _uiState.value = UiState.Loading
                }
            }
        }
    }

    fun resetCurrentPage() {
        _listArticle.value = arrayListOf()
        isLastPage = false
        currentPage = 1
    }

    fun changeDateFormat(dateString: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val targetFormat = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val date = originalFormat.parse(dateString)
        return targetFormat.format(date)
    }
}