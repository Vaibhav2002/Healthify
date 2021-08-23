package com.vaibhav.healthify.util

import com.vaibhav.healthify.R
import com.vaibhav.healthify.data.models.OnBoarding
import java.util.*

const val DATASTORE = "HealthifyDataStore"
const val USER_DOES_NOT_EXIST = "User does not exist"
const val USER_DETAILS_UPDATED = "Details updated successfully"
const val USER_DETAILS_UPDATE_FAILED = "Failed to update details"
const val NO_INTERNET_MESSAGE = "Looks like you don't have an active internet connection"

const val USER_COLLECTION = "users"
const val WATER_COLLECTION = "water"
const val SLEEP_COLLECTION = "sleep"

const val OPEN_ADD_SLEEP_DIALOG = "AddSleep"
const val OPEN_ADD_WATER_DIALOG = "AddWater"
const val OPEN_WATER_LIMIT_DIALOG = "SelectWaterLimit"
const val OPEN_SLEEP_LIMIT_DIALOG = "SelectSleepLimit"
const val OPEN_NO_INTERNET_DIALOG = "NoInternetDialog"

const val WATER_EXP = 5
const val SLEEP_EXP = 5

const val NOTIFICATION_INTERVAL = 60 * 60 * 1000L

enum class WATER(val quantity: Int, val image: Int) {
    ML_200(200, R.drawable.hot_cup), ML_400(400, R.drawable.mug),
    ML_600(600, R.drawable.water_glass), ML_800(800, R.drawable.mineral_water),
    ML_1000(1000, R.drawable.water_bottle)
}

enum class ERROR_TYPE {
    NO_INTERNET, UNKNOWN
}

val onBoardingList = listOf(
    OnBoarding(
        id = 0,
        anim = R.raw.hello,
        title = "Welcome!",
        subtitle = "Stay fit and improve your productivity\n" +
            "with Healthify"
    ),
    OnBoarding(
        id = 1,
        anim = R.raw.water,
        title = "Stay hydrated",
        subtitle = "Healthify helps you track your water\n" +
            "intake and reminds you to drink water"
    ),
    OnBoarding(
        id = 2,
        anim = R.raw.sleep,
        title = "Maintain Good Sleep",
        subtitle = "Healthify tracks your daily sleep\n" +
            "and helps you maintain a good sleep cycle"
    ),
    OnBoarding(
        id = 3,
        anim = R.raw.leaderboard,
        title = "Earn XP as you go",
        subtitle = "Earn XP when you drink water or record sleep. Make your way up to the leaders"
    )
)

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
val sleepFOTD = listOf(
    "Research shows that in the days leading up to a full moon, people go to bed later and sleep less, although the reasons are unclear.",
    "Sea otters hold hands when they sleep so they don’t drift away from each other.",
    "Tiredness peaks twice a day: Around 2 a.m. and 2 p.m. for most people. That’s why you’re less alert after lunch.",
    "Have trouble waking up on Monday morning? Blame “social jet lag” from your altered weekend sleep schedule.",
    "Today, 75% of us dream in color. Before color television, just 15% of us did.",
    "Regular exercise usually improves your sleep patterns. Strenuous exercise right before bed may keep you awake.",
    "Whales and dolphins literally fall half asleep. Each side of their brain takes turns so they can come up for air."
)

fun getMainGreeting(progress: Float): String {
    return when {
        progress >= 100 -> "Yay !"
        progress > 50f -> "Yay !"
        progress == 50f -> "Good going !"
        else -> "It's okay !"
    }
}

fun getGreeting(progress: Float): String {
    return when {
        progress >= 100 -> "You're done !"
        progress > 50f -> "You're almost done !"
        progress == 50f -> "You're half way done !"
        else -> "You can do it !"
    }
}

fun getTodaysTime(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    return calendar.timeInMillis
}

fun getTimeOfLastWeek(): Long {
    val calendar = Calendar.getInstance()
    calendar[Calendar.HOUR_OF_DAY] = 0
    return calendar.timeInMillis - (7 * 24 * 60 * 60 * 1000)
}

fun getTodayDayNo(): Int {
    val cal = Calendar.getInstance()
    cal.timeInMillis = System.currentTimeMillis()
    return cal[Calendar.DAY_OF_WEEK] - 1
}

enum class DAYS(index: Int) {
    SUNDAY(1), MONDAY(2), TUESDAY(3), WEDNESDAY(4), THURSDAY(5), FRIDAY(6), SATURDAY(7);

    companion object {
        fun getDayFromNumber(d: Int) = when (d) {
            1 -> SUNDAY
            2 -> MONDAY
            3 -> TUESDAY
            4 -> WEDNESDAY
            5 -> THURSDAY
            6 -> FRIDAY
            7 -> SATURDAY
            else -> SUNDAY
        }
    }

    fun getShortFormFromNumber() = when (this) {
        SUNDAY -> getFirstChars()
        MONDAY -> getFirstChars()
        TUESDAY -> getFirstChars()
        WEDNESDAY -> getFirstChars()
        THURSDAY -> getTwoChars()
        FRIDAY -> getFirstChars()
        SATURDAY -> getTwoChars()
        else -> getTwoChars()
    }

    fun getFullName() = this.name.lowercase().capitalize()

    private fun getFirstChars() = this.name.substring(0, 1)

    private fun getTwoChars() = this.name.substring(0, 2).lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}
