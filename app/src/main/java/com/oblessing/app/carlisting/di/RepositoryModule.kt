package com.oblessing.app.carlisting.di

import com.oblessing.app.carlisting.database.CarCacheMapper
import com.oblessing.app.carlisting.database.CarDao
import com.oblessing.app.carlisting.network.CarListingWebService
import com.oblessing.app.carlisting.network.NetworkMapper
import com.oblessing.app.carlisting.repository.CarListingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providesCarListingRepository(
        cacheMapper: CarCacheMapper,
        networkMapper: NetworkMapper,
        webService: CarListingWebService,
        carDao: CarDao
    ): CarListingRepository = CarListingRepository(cacheMapper, networkMapper, webService, carDao)
}