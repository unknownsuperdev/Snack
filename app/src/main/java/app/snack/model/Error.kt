package app.snack.model

data class Error(
    var code: Int = 0,
    var message: String? = "Ooops! Unknown error...",
    var messageRes: Int? = -1
)
