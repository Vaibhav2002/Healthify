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

val waterFOTD = listOf(
    "Water makes up approximately 70% of a human’s body weight – but DON’T stop drinking water to lose weight!",
    "Approximately 80% of your brain tissue is made of water (about the same percentage of water found in a living tree – maybe is this why people hit their heads and say “knock on wood”?).",
    "The average amount of water you need per day is about 3 liters (13 cups) for men and 2.2 liters (9 cups) for women.",
    "By the time you feel thirsty, your body has lost more than 1 percent of its total water – so let’s not feel thirst. Take a break right now and have a glass of water.",
    "Refreshed? Let’s continue. Drinking water can help you lose weight by increasing your metabolism, which helps burn calories faster.",
    "Smile. Good hydration can help reduce cavities and tooth decay. Water helps produce saliva, which keeps your mouth and teeth clean.",
    "Good hydration can prevent arthritis. With plenty of water in your body, there is less friction in your joints, thus less chance of developing arthritis."
)

fun getGreeting(progress: Float): String {
    return when {
        progress >= 100 -> "You're done !"
        progress > 50f -> "You're almost done !"
        progress == 50f -> "You're half way done !"
        else -> "You can do it !"
    }
}
