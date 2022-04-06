package app.snack.model.request

class UserRequest {
    data class Credentials(val cardNumber: String, val cardHolder: String)
    data class Profile(val price: Float = 25f / (1024f * 1024f * 1024f))
    data class Email(val email: String)
    data class Password(val password: String)
}

