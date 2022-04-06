package app.snack.model.result

import app.snack.model.Error
import app.snack.model.response.ErrorResponse
import app.snack.model.response.Transaction
import app.snack.model.response.TransactionResponse
import com.google.gson.Gson
import retrofit2.Response

sealed class TransactionsResult {

    companion object {
        fun fromException(exception: Exception): TransactionsResult {
            return Failure(
                error = Error(
                    code = 0,
                    message = exception.message
                )
            )
        }

        fun <T> parseError(response: Response<T>): TransactionsResult {
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

    data class Success(val transactions: MutableList<Transaction>) : TransactionsResult()
    data class Failure(val error: Error) : TransactionsResult()
}
