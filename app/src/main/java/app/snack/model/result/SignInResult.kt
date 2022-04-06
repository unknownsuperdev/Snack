package app.snack.model.result

import android.util.Log
import app.snack.model.Error
import app.snack.model.response.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response

sealed class SignInResult {

    companion object {
        fun fromException(exception: Exception): SignInResult {
            return Failure(
                error = Error(
                    code = 0,
                    message = "Something goes wrong, try again later"
                )
            )
        }

        fun <T> parseError(response: Response<T>): SignInResult {
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

    object Success : SignInResult()
    data class Failure(val error: Error) : SignInResult()
}
