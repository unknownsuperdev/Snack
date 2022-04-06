package app.snack.model.response

data class ErrorResponse(val message: Message, val status: Int, val version: String) {

    data class Message(val error: String)

}
