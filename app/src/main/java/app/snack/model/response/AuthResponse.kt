package app.snack.model.response

class AuthResponse {

    data class SignIn(val data: Data, val status: Int, val version: String)

    data class SignUp(val data: Data, val status: Int, val version: String)

    data class Data(val token: String?, val error: String?)

}