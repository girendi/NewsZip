package com.girendi.newszip.core.di

import com.girendi.newszip.BuildConfig
import com.girendi.newszip.core.data.api.ApiService
import com.girendi.newszip.core.data.repository.ArticleBySourceRepositoryImpl
import com.girendi.newszip.core.data.repository.SourceBySourceByCategoryRepositoryImpl
import com.girendi.newszip.core.domain.repository.ArticleBySourceRepository
import com.girendi.newszip.core.domain.repository.SourceByCategoryRepository
import com.girendi.newszip.core.domain.usecase.FetchArticleBySourceUseCase
import com.girendi.newszip.core.domain.usecase.FetchSourceByCategoryUseCase
import com.girendi.newszip.presentation.articles.ArticleViewModel
import com.girendi.newszip.presentation.main.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val originalHttpUrl = original.url

                val url = originalHttpUrl.newBuilder()
                    .addQueryParameter("apiKey", BuildConfig.APP_KEY)
                    .build()

                val requestBuilder = original.newBuilder()
                    .url(url)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ApiService::class.java)
    }

    single<SourceByCategoryRepository> { SourceBySourceByCategoryRepositoryImpl(apiService = get()) }
    single<ArticleBySourceRepository> { ArticleBySourceRepositoryImpl(apiService = get()) }

    factory { FetchSourceByCategoryUseCase(sourceByCategoryRepository = get()) }
    factory { FetchArticleBySourceUseCase(articleBySourceRepository = get()) }

    viewModel { MainViewModel(fetchSourceByCategoryUseCase = get()) }
    viewModel { ArticleViewModel(fetchArticleBySourceUseCase = get()) }
}