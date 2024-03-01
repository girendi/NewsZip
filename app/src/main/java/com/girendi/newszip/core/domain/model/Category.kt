package com.girendi.newszip.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    var category: String,
    var status: Boolean
): Parcelable