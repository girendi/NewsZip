package com.girendi.newszip.core.domain.repository

import com.girendi.newszip.core.data.response.ArticleResponse
import com.girendi.newszip.core.domain.Result

interface ArticleBySourceRepository {
    suspend fun fetchArticleBySource(sourceId: String, query: String, page: Int): Result<ArticleResponse>
}