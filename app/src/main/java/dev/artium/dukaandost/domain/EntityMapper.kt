package dev.artium.dukaandost.domain

interface EntityMapper<Entity, DomainModel> {
    fun mapFromEntity(entity: Entity): DomainModel
    fun mapToEntity(model: DomainModel): Entity
}