package app.snack.ui.login.screens.sign_in

import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.BuildConfig
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSignInBinding
import app.snack.ui.login.LoginViewModel
import app.snack.utils.Screen
import app.snack.utils.extensions.isValidEmail
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.onTextChanged
import app.snack.utils.extensions.text
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SignInFragment : BindingFragment<FragmentSignInBinding, LoginViewModel>() {

    override val viewModel by activityViewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignInBinding::inflate


    private var googleResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            viewModel.handleGoogleCredentialsResult(activityResult)
        }

    override fun setupUI() {
        with(binding) {
            tilEmail.isEndIconVisible = false

            etEmail.onTextChanged { tilEmail.isErrorEnabled = false }
            etPassword.onTextChanged { tilPassword.isErrorEnabled = false }

            btnForgot.onClick { addScreen(Screen.SIGN_IN_RESTORE_PASSWORD) }
            btnGoogle.onClick {
                signInByGoogle()
//                Log.d("AAA", "Signin google")
//                addScreen(Screen.GOOGLE)
            }
            btnFacebook.onClick { addScreen(Screen.FACEBOOK) }

            btnSignIn.onClick {
                var hasError = false

                if (etEmail.text().isValidEmail.not()) {
                    hasError = true

                    tilEmail.isErrorEnabled = true
                    tilEmail.error = "Email is not valid"
                }

                if (etPassword.text().isEmpty()) {
                    hasError = true

                    tilPassword.isErrorEnabled = true
                    tilPassword.error = "Enter password"
                }

                if (hasError.not()) {
                    viewModel.enterEmail(etEmail.text())
                    viewModel.enterPassword(etPassword.text())
                    viewModel.signIn()
                }
            }
            btnSignUp.onClick { closeScreen() }
        }
    }

    private fun signInByGoogle() {
        val googleSignInClient = GoogleSignIn.getClient(
            requireActivity(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
                .build()
        )

        googleSignInClient.signOut()

        googleResult.launch(googleSignInClient.signInIntent)
    }

}