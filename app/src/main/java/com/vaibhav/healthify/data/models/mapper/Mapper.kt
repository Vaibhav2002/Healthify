package com.vaibhav.healthify.data.models.mapper

interface Mapper<Entity, DTO> {

    fun toEntity(dto: DTO): Entity

    fun toEntityList(dtos: List<DTO>): List<Entity>

    fun toDTO(entity: Entity): DTO
}
