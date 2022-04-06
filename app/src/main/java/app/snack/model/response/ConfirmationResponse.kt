package app.snack.model.response

data class ConfirmationResponse(val data: ConfirmationData, val status: Int, val version: String)

data class ConfirmationData(val message: String)