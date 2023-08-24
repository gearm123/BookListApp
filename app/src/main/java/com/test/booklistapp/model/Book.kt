package com.test.booklistapp.model

import com.google.gson.annotations.SerializedName

data class Book(
    @SerializedName("title")
    var title: String,
    @SerializedName("body")
    var body: String,
    @SerializedName("rating")
    var rating: Double,
    @SerializedName("download_url")
    var downloadUrl: String,
)