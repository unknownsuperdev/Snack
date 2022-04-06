package app.snack.model.result

import app.snack.model.Error
import app.snack.model.response.ErrorResponse
import com.google.gson.Gson
import org.json.JSONObject
import retrofit2.Response

sealed class SignUpResult {

    companion object {
        fun fromException(exception: Exception): SignUpResult {
            return Failure(
                error = Error(
                    code = 0,
                    message = exception.message
                )
            )
        }

        fun <T> parseError(response: Response<T>): SignUpResult {
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

    object Success : SignUpResult()
    data class Failure(val error: Error) : SignUpResult()
}

