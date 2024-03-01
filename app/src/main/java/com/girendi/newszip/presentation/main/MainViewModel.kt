package com.girendi.newszip.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girendi.newszip.core.domain.Result
import com.girendi.newszip.core.domain.UiState
import com.girendi.newszip.core.domain.model.Category
import com.girendi.newszip.core.domain.model.Source
import com.girendi.newszip.core.domain.usecase.FetchSourceByCategoryUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val fetchSourceByCategoryUseCase: FetchSourceByCategoryUseCase): ViewModel() {

    private val _listCategory = MutableLiveData<List<Category>>()
    val listCategory: LiveData<List<Category>> = _listCategory

    private val _listSource = MutableLiveData<List<Source>>()
    val listSource: LiveData<List<Source>> = _listSource

    private val _uiState = MutableLiveData<UiState>()
    val uiState: LiveData<UiState> = _uiState

    private var tempListSource: List<Source>

    init {
        _listCategory.value = fetchCategories()
        fetchSourceByCategory("General")
        tempListSource = arrayListOf()
    }

    fun fetchSourceByCategory(category: String) {
        changeCategory(category)
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            when(val result = fetchSourceByCategoryUseCase.fetchSourceByCategory(category)) {
                is Result.Success -> {
                    val sources = result.data.source
                    tempListSource = sources
                    _listSource.value = sources
                    _uiState.value = UiState.Success
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

    private fun fetchCategories() : List<Category> {
        return listOf(
            Category("General", true),
            Category("Business", false),
            Category("Entertainment", false),
            Category("Health", false),
            Category("Science", false),
            Category("Sports", false),
            Category("Technology", false)
        )
    }

    private fun changeCategory(category: String) {
        val listData = _listCategory.value?.map { item ->
            item.status = item.category == category
            item
        }
        _listCategory.postValue(listData!!)
    }

    fun filterSource(query: String) {
        val filter = tempListSource!!.filter {
            it.name?.contains(query, ignoreCase = true) ?: false
        }
        _listSource.value = filter
    }
}