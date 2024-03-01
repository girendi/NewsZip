package com.girendi.newszip.core.data.repository

import com.girendi.newszip.core.data.api.ApiService
import com.girendi.newszip.core.data.response.ArticleResponse
import com.girendi.newszip.core.domain.Result
import com.girendi.newszip.core.domain.repository.ArticleBySourceRepository

class ArticleBySourceRepositoryImpl(private val apiService: ApiService): BaseRepositoryImpl(), ArticleBySourceRepository {
    override suspend fun fetchArticleBySource(
        sourceId: String,
        query: String,
        page: Int
    ): Result<ArticleResponse> =
        apiRequest { apiService.fetchArticlesBySource(
            sourceId,
            query,
            10,
            page
        ) }
}