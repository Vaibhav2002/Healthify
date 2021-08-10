package com.vaibhav.healthify.util

import com.vaibhav.healthify.R

const val DATASTORE = "HealthifyDataStore"
const val USER_DOES_NOT_EXIST = "User does not exist"
const val USER_DETAILS_UPDATED = "Details updated successfully"
const val USER_DETAILS_UPDATE_FAILED = "Failed to update details"

const val USER_COLLECTION = "users"
const val WATER_COLLECTION = "water"

enum class WATER(val quantity: Int, val image: Int) {
    ML_200(200, R.drawable.hot_cup), ML_400(400, R.drawable.mug),
    ML_600(600, R.drawable.water_glass), ML_800(800, R.drawable.mineral_water),
    ML_1000(1000, R.drawable.water_bottle)
}

val waterList = listOf(
    WATER.ML_200, WATER.ML_400, WATER.ML_600, WATER.ML_800, WATER.ML_1000
)

fun getGreeting(progress: Float): String {
    return when {
        progress < 50f -> "Don't lose hope"
        progress == 50f -> "You're half way done"
        else -> "You're almost done"
    }
}
