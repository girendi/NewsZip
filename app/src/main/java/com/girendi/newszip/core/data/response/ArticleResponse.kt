package com.girendi.newszip.core.data.response

import com.girendi.newszip.core.domain.model.Article

data class ArticleResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
)
