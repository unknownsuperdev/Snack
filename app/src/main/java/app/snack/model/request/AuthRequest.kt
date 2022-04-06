package app.snack.model.request

class AuthRequest {

    data class SignIn(val email: String, val password: String)

    data class SignInGoogle(val token: String)

    data class SignUp(val email: String, val password: String)

    data class RecoverPassword(val email: String)
}