package app.snack.ui.login.screens.welcome

import android.util.Log
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.text.parseAsHtml
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.BuildConfig
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentWelcomeBinding
import app.snack.ui.login.LoginViewModel
import app.snack.utils.Screen
import app.snack.utils.extensions.onClick
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment : BindingFragment<FragmentWelcomeBinding, LoginViewModel>() {

    override val viewModel by viewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentWelcomeBinding::inflate

    private var googleResult =
        registerForActivityResult(StartActivityForResult()) { activityResult ->
            viewModel.handleGoogleCredentialsResult(activityResult)
        }

    override fun setupUI() {
        binding.title.text = getString(R.string.welcome_to_snack).parseAsHtml()

        binding.btnGoogle.onClick { signInByGoogle() }
        binding.btnFacebook.onClick { addScreen(Screen.FACEBOOK) }
        binding.btnEmailPassword.onClick { addScreen(Screen.SIGN_UP_EMAIL) }
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