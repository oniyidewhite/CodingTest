package com.oblessing.app.carlisting.database

import com.oblessing.app.carlisting.models.Car
import com.oblessing.app.carlisting.models.MSImage
import com.oblessing.app.core.utils.EntityMapper
import javax.inject.Inject

class CarCacheMapper
@Inject
constructor() : EntityMapper<CarCacheEntity, Car> {
    override fun mapFromEntity(entity: CarCacheEntity) = Car(
        title = entity.title,
        fuel = entity.fuel,
        pictures = entity.pictures.map { MSImage(it) },
        price = entity.price,
        description = entity.description,
        id = entity.id
    )

    fun mapFromEntityList(entities: List<CarCacheEntity>) = entities.map { mapFromEntity(it) }

    override fun mapToEntity(model: Car) = CarCacheEntity(
        id = model.id,
        title = model.title,
        fuel = model.fuel,
        pictures = model.pictures.map { it.imageUrl },
        price = model.price,
        description = model.description
    )
}