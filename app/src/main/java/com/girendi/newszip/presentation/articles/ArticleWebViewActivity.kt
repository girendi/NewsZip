package com.girendi.newszip.presentation.articles

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.girendi.newszip.databinding.ActivityArticleWebViewBinding

class ArticleWebViewActivity: AppCompatActivity() {

    private lateinit var binding: ActivityArticleWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val url = intent.getStringExtra(EXTRA_URL)
        binding.webView.settings.javaScriptEnabled = true
        if (url != null) {
            binding.webView.loadUrl(url)
        }
    }

    companion object {
        const val EXTRA_URL = "extraUrl"
    }
}