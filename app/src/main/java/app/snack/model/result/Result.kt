package app.snack.model.result

import app.snack.model.Error
import app.snack.model.response.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response

sealed class Result {

    companion object {
        fun fromException(exception: Exception): Result {
            return Failure(
                error = Error(
                    code = 0,
                    message = exception.message
                )
            )
        }

        fun <T> parseError(response: Response<T>): Result {
            val rawErrorBody = response.errorBody()?.string()
            val rawError = Gson().fromJson(rawErrorBody, ErrorResponse::class.java)

            return Failure(
                error = Error(
                    code = rawError.status,
                    message = rawError.message.error
                )
            )
        }
    }

    object Success : Result()
    data class Failure(val error: Error = Error()) : Result()
}
