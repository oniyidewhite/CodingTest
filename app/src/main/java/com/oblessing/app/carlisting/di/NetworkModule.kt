package com.oblessing.app.carlisting.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.oblessing.app.BuildConfig
import com.oblessing.app.carlisting.network.CarListingWebService
import com.oblessing.app.core.utils.debug
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideGsonBuilder() = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val okhttp = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)

        debug {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            okhttp.addNetworkInterceptor(logging)
        }

        return okhttp.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient) =
        Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)).build()

    @Singleton
    @Provides
    fun provideCarListingService(retrofit: Retrofit) =
        retrofit.create(CarListingWebService::class.java)
}