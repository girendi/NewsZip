package com.girendi.newszip.core.data.repository

import com.girendi.newszip.core.data.api.ApiService
import com.girendi.newszip.core.data.response.SourceResponse
import com.girendi.newszip.core.domain.Result
import com.girendi.newszip.core.domain.repository.SourceByCategoryRepository

class SourceBySourceByCategoryRepositoryImpl(private val apiService: ApiService): BaseRepositoryImpl(), SourceByCategoryRepository {

    override suspend fun fetchSourceByCategory(category: String): Result<SourceResponse> =
        apiRequest { apiService.fetchSourcesByCategory(category) }
}