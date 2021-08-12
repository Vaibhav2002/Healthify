package com.vaibhav.healthify.data.models.mapper

import com.vaibhav.healthify.data.models.local.Sleep
import com.vaibhav.healthify.data.models.remote.SleepDTO

class SleepMapper : Mapper<Sleep, SleepDTO> {
    override fun toEntity(dto: SleepDTO) = Sleep(
        sleepDuration = dto.duration,
        timeStamp = dto.timeStamp
    )

    override fun toEntityList(dtos: List<SleepDTO>) = dtos.map {
        toEntity(it)
    }

    override fun toDTO(entity: Sleep) = SleepDTO(
        duration = entity.sleepDuration,
        timeStamp = entity.timeStamp
    )
}
