package com.vaibhav.healthify.data.remote.water

import com.vaibhav.healthify.data.models.remote.WaterDTO
import com.vaibhav.healthify.util.Resource

interface WaterDataSource {

    suspend fun getAllWaterLogs(email: String): Resource<List<WaterDTO>>

    suspend fun addWater(email: String, waterDTO: WaterDTO): Resource<WaterDTO>
}
