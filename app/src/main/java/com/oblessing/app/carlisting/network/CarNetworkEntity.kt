package com.oblessing.app.carlisting.network

import com.google.gson.annotations.SerializedName

// Api response data class
data class CarNetworkEntity(
    @SerializedName("id")
    val id: Int,
    @SerializedName("make")
    val make: String,
    @SerializedName("model")
    val model: String,
    @SerializedName("price")
    val price: Double,
    @SerializedName("fuel")
    val fuel: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("images")
    val pictures: List<CarImageNetworkEntity>?
)

data class CarImageNetworkEntity(
    @SerializedName("url")
    val imageUrl: String,
)

