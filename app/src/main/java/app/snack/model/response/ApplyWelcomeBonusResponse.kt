package app.snack.model.response

import com.google.gson.annotations.SerializedName

data class ApplyWelcomeBonusResponse(
    @SerializedName("data")
    val data: Data,
    @SerializedName("status")
    val status: Int,
    @SerializedName("traceID")
    val traceID: String,
    @SerializedName("version")
    val version: String) {

    data class Data(
        @SerializedName("message")
        val message: String)

}
