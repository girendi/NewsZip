package com.girendi.newszip.core.domain.repository

import com.girendi.newszip.core.data.response.SourceResponse
import com.girendi.newszip.core.domain.Result

interface SourceByCategoryRepository {
    suspend fun fetchSourceByCategory(category: String): Result<SourceResponse>
}