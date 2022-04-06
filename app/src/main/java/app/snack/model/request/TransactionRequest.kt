package app.snack.model.request

data class TransactionAdd(
    private val cardNumber: String,
    private val price: Float = 0.02f,
    private val sum: Float)