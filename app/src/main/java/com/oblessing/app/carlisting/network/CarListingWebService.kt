package com.oblessing.app.carlisting.network

import retrofit2.http.GET


interface CarListingWebService {
    @GET("/")
    suspend fun listCars(): List<CarNetworkEntity>
}
