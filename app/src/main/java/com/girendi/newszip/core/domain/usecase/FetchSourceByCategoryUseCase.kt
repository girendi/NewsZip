package com.girendi.newszip.core.domain.usecase

import com.girendi.newszip.core.data.response.SourceResponse
import com.girendi.newszip.core.domain.Result
import com.girendi.newszip.core.domain.repository.SourceByCategoryRepository

class FetchSourceByCategoryUseCase(private val sourceByCategoryRepository: SourceByCategoryRepository) {

    suspend fun fetchSourceByCategory(category: String): Result<SourceResponse> =
        sourceByCategoryRepository.fetchSourceByCategory(category)
}