package com.vaibhav.healthify.data.models.mapper

import com.vaibhav.healthify.data.models.local.Water
import com.vaibhav.healthify.data.models.remote.WaterDTO
import com.vaibhav.healthify.util.WATER

class WaterMapper : Mapper<Water, WaterDTO> {
    override fun toEntity(dto: WaterDTO) = Water(
        quantity = WATER.valueOf(dto.quantity),
        timeStamp = dto.timeStamp,
    )

    override fun toEntityList(dtos: List<WaterDTO>) = dtos.map {
        toEntity(it)
    }

    override fun toDTO(entity: Water) = WaterDTO(
        quantity = entity.quantity.name,
        timeStamp = entity.timeStamp
    )
}
