package com.oblessing.app.carlisting.repository

import com.oblessing.app.carlisting.database.CarCacheMapper
import com.oblessing.app.carlisting.database.CarDao
import com.oblessing.app.carlisting.models.Car
import com.oblessing.app.carlisting.network.CarListingWebService
import com.oblessing.app.carlisting.network.NetworkMapper
import com.oblessing.app.core.utils.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

class CarListingRepository
constructor(
    private val cacheMapper: CarCacheMapper,
    private val networkMapper: NetworkMapper,
    private val webService: CarListingWebService,
    private val carDao: CarDao
) {

    suspend fun cars(forceReload: Boolean): Flow<DataState<List<Car>>> = flow {
        val cachedCars = carDao.get()
        if (!forceReload && cachedCars.isNotEmpty()) {
            emit(DataState.Success(cacheMapper.mapFromEntityList(cachedCars), cached = true))
            return@flow
        }

        emit(DataState.Loading)
        try {
            networkMapper.mapFromEntityList(webService.listCars()).forEach { car ->
                carDao.insert(cacheMapper.mapToEntity(car))
            }

            emit(DataState.Success(cacheMapper.mapFromEntityList(carDao.get())))
        } catch (ex: Exception) {
            emit(DataState.Error(ex))
        }
    }
}