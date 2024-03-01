package com.girendi.newszip.core.domain.usecase

import com.girendi.newszip.core.data.response.ArticleResponse
import com.girendi.newszip.core.domain.Result
import com.girendi.newszip.core.domain.repository.ArticleBySourceRepository

class FetchArticleBySourceUseCase(private val articleBySourceRepository: ArticleBySourceRepository) {
    suspend fun fetchArticleBySource(sourceId: String, query: String, page: Int): Result<ArticleResponse> =
        articleBySourceRepository.fetchArticleBySource(sourceId, query, page)
}