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

    val email: String,

    val currentPrice: Float,      //  +

    val currentBalance: Balance,      //  +

    val currentBalancePaid: Balance,  // ?

    val trafficPerWeek: Long,

    val moneyEarnedPerWeek: Balance,                          // ?

    val moneyEarnedByWeekByDay: List<MoneyEarnedByDays> = emptyList(),  // +

    val trafficPerToday: Long,      // +

    val moneyEarnedPerToday: Balance,  // +

    val moneyEarnedPerLastWeekByDay: List<MoneyEarnedByDays> = emptyList(),

    val emailConfirmed: Boolean = false

) {
    val completeForPayoutPercent
        get() = currentBalance.dollar / 10f * 100f

}

data class MoneyEarnedByDays(val day: String, val sum: Balance)

data class Balance(val dollar: Float, val cent: Float)