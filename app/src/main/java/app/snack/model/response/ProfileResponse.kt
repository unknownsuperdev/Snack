package app.snack.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ProfileResponse(val data: Profile, val status: Int, val version: String) {

    companion object {
//        fun mockProfileResponse() = ProfileResponse(
//            data = Profile(
//                "user@example.com",
//                8.45f,
//                8.03f,
//                545345,
//                8.12f,
//                listOf(
//                    MoneyEarnedByDays("2022-01-14", 1.49f),
//                    MoneyEarnedByDays("2022-01-13", 0.25f),
//                    MoneyEarnedByDays("2022-01-12", 1.21f),
//                    MoneyEarnedByDays("2022-01-11", 0.84f),
//                    MoneyEarnedByDays("2022-01-10", 0.37f),
//                    MoneyEarnedByDays("2022-01-09", 0.15f),
//                    MoneyEarnedByDays("2022-01-08", 0.56f),
//                ),
//                1,
//                2.48f,
//                listOf(
//                    MoneyEarnedByDays("2022-01-14", 1.49f),
//                    MoneyEarnedByDays("2022-01-13", 0.25f),
//                    MoneyEarnedByDays("2022-01-12", 1.21f),
//                    MoneyEarnedByDays("2022-01-11", 0.84f),
//                    MoneyEarnedByDays("2022-01-10", 0.37f),
//                    MoneyEarnedByDays("2022-01-09", 0.15f),
//                    MoneyEarnedByDays("2022-01-08", 0.56f),
//                ),
//            ),
//            200,
//            "1.1"
//        )
    }
}

data class Profile(

    @SerializedName("email")
    val email: String,

    @SerializedName("current_price")
    val currentPrice: Float,      //  +

    @SerializedName("current_balance")
    val currentBalance: Float,      //  +

    @SerializedName("current_balance_paid")
    val currentBalancePaid: Float,  // ?

    @SerializedName("traffic_per_week")
    val trafficPerWeek: Long,

    @SerializedName("sc_earned_per_week")
    val moneyEarnedPerWeek: Float,                          // ?

    @SerializedName("sc_earned_per_last_week_by_day")
    val moneyEarnedByWeekByDay: List<MoneyEarnedByDays> = emptyList(),  // +

    @SerializedName("traffic_per_today")
    val trafficPerToday: Long,      // +

    @SerializedName("sc_earned_per_today")
    val moneyEarnedPerToday: Float,  // +

    @SerializedName("sc_earned_by_week_by_day")
    val moneyEarnedPerLastWeekByDay: List<MoneyEarnedByDays> = emptyList(),

    @SerializedName("email_confirmed")
    val emailConfirmed: Boolean = false

) {
    val completeForPayoutPercent
        get() = currentBalance /1000 / 10f * 100f

}

data class MoneyEarnedByDays(
    @SerializedName("day")
    val day: String,
    @SerializedName("value")
    val value: Float)

//data class Balance(val dollar: Float, val cent: Float)