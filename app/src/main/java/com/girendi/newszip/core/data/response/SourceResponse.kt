package com.girendi.newszip.core.data.response

import com.girendi.newszip.core.domain.model.Source
import com.google.gson.annotations.SerializedName

data class SourceResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("sources")
    val source: List<Source>
)
