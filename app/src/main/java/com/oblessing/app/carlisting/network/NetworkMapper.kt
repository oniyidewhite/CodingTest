package com.oblessing.app.carlisting.network

import com.oblessing.app.carlisting.models.Car
import com.oblessing.app.carlisting.models.MSImage
import com.oblessing.app.core.utils.EntityMapper
import javax.inject.Inject

class NetworkMapper
@Inject
constructor() : EntityMapper<CarNetworkEntity, Car> {
    override fun mapFromEntity(entity: CarNetworkEntity) = Car(
        id = entity.id,
        title = "${entity.make} ${entity.model}",
        fuel = entity.fuel,
        pictures = entity.pictures?.map { MSImage(it.imageUrl) } ?: emptyList(),
        price = entity.price,
        description = entity.description
    )

    fun mapFromEntityList(entities: List<CarNetworkEntity>) = entities.map { mapFromEntity(it) }

    override fun mapToEntity(model: Car) =
        CarNetworkEntity(
            id = model.id,
            make = model.title.split(" ")[0],
            model = model.title.split(" ")[1],
            fuel = model.fuel,
            pictures = model.pictures.map { CarImageNetworkEntity(it.imageUrl) },
            price = model.price,
            description = model.description
        )
}