package com.oblessing.app.core.utils

interface EntityMapper<EntityModel, DomainModel> {
    fun mapFromEntity(entity: EntityModel): DomainModel
    fun mapToEntity(model: DomainModel): EntityModel
}