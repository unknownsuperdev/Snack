package app.snack.model.response

import app.snack.utils.extensions.convertUnixTimestampToMediumDate

data class TransactionResponse(val data: TransactionsList, val status: Int, val version: String) {
    companion object {
//        fun mockTransactionResponse() = TransactionResponse(
//            mutableListOf(
//                Transaction(id = 3, cardNumber = "**** 1234", "Pending", 54.80f, datetime = "1641887175691"),
//                Transaction(id = 4, cardNumber = "**** 5235", "Failed", 12.42f, datetime = "1641887178185"),
//                Transaction(id = 5, cardNumber = "**** 1536", "Pending", 4.50f, datetime = "1641887947311"),
//                Transaction(id = 5, cardNumber = "**** 4236", "Sent", 14.54f, datetime = "1641887947311"),
//            ),
//            200
//        )
    }
}

data class TransactionsList(val transactions: MutableList<Transaction>)

data class Transaction(
    val id: Int,
    val cardNumber: String,
    val status: String,
    val sum: Float,
    val datetime: String
) {
    val mediumDate
        get() = datetime.convertUnixTimestampToMediumDate()
}

data class ValidationResponse(val data: ValidationResult, val status: Int, val version: String)

data class ValidationResult(val validation: Boolean)
