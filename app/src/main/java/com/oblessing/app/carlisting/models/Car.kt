package com.oblessing.app.carlisting.models

data class Car(
    val id: Int,
    val title: String,
    val price: Double,
    val fuel: String,
    val description: String,
    val pictures: List<MSImage>
)