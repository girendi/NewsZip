package com.girendi.newszip.core.data.api

import com.girendi.newszip.core.data.response.ArticleResponse
import com.girendi.newszip.core.data.response.SourceResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v2/top-headlines/sources")
    suspend fun fetchSourcesByCategory(
        @Query("category") category: String,
    ): Response<SourceResponse>

    @GET("v2/everything")
    suspend fun fetchArticlesBySource(
        @Query("sources") sourceId: String,
        @Query("q") query: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ): Response<ArticleResponse>
}