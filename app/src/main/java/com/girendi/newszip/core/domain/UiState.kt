package com.girendi.newszip.core.domain

sealed class UiState {
    data object Loading : UiState()
    data object Success : UiState()
    data class Error(val message: String) : UiState()
}