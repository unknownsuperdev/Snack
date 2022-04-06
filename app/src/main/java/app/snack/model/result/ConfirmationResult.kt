package app.snack.model.result

import app.snack.model.Error
import app.snack.model.response.ConfirmationResponse
import app.snack.model.response.ErrorResponse
import app.snack.model.response.ProfileResponse
import com.google.gson.Gson
import retrofit2.Response

sealed class ConfirmationResult {

    companion object {
        fun fromException(exception: Exception): ConfirmationResult {
            return Failure(
                error = Error(
                    code = 0,
                    message = exception.message
                )
            )
        }

        fun <T> parseError(response: Response<T>): ConfirmationResult {
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

    data class Success(val confirmationResponse: ConfirmationResponse) : ConfirmationResult()
    data class Failure(val error: Error) : ConfirmationResult()
}