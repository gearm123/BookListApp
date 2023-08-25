package com.test.booklistapp.model

import com.google.gson.annotations.SerializedName
//Book entity
data class Book(
    @SerializedName("title")
    var title: String,
    @SerializedName("body")
    var body: String,
    @SerializedName("rating")
    var rating: Double,
    @SerializedName("place_holder")
    var placeHolder: Int,
    @SerializedName("download_url")
    var downloadUrl: String,
)