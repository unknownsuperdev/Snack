package app.snack.model.request

data class TransactionAdd(
    private val cardNumber: String,
    private val sum: Float?
)