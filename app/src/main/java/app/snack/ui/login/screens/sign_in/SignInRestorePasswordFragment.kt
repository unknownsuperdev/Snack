package app.snack.ui.login.screens.sign_in

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSignInRestorePasswordBinding
import app.snack.ui.login.LoginViewModel
import app.snack.utils.Screen
import app.snack.utils.extensions.*

class SignInRestorePasswordFragment :
    BindingFragment<FragmentSignInRestorePasswordBinding, LoginViewModel>() {

    override val viewModel by activityViewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignInRestorePasswordBinding::inflate

    override fun setupUI() {
        with(binding) {
            tilEmail.isEndIconVisible = false

            etEmail.addOnImeActionClick { btnGetLink.callOnClick() }
            etEmail.onTextChanged { email ->
                tilEmail.isErrorEnabled = false
                tilEmail.isEndIconVisible = email.isValidEmail
            }

            btnGetLink.onClick {
                tilEmail.isErrorEnabled = false

                if (etEmail.text().isValidEmail.not()) {
                    tilEmail.isErrorEnabled = true
                    tilEmail.error = "Email is not valid"
                } else {
                    viewModel.enterEmail(etEmail.text())
                    viewModel.restorePassword()
                }
            }
        }
    }
}
