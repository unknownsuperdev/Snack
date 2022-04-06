package app.snack.ui.login.screens.sign_up

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSignUpConfirmPasswordBinding
import app.snack.ui.login.LoginViewModel
import app.snack.utils.extensions.addOnImeActionClick
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.onTextChanged
import app.snack.utils.extensions.text
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpConfirmPasswordFragment :
    BindingFragment<FragmentSignUpConfirmPasswordBinding, LoginViewModel>() {

    override val viewModel by activityViewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignUpConfirmPasswordBinding::inflate

    override fun setupUI() {
        with(binding) {
            etPassword.addOnImeActionClick { fabNext.callOnClick() }
            etPassword.onTextChanged {
                binding.tilPassword.isErrorEnabled = false
            }
            fabNext.onClick {
                viewModel.checkSignUpPassword(etPassword.text())
            }
        }
    }

    override fun observeViewModels() {
        super.observeViewModels()

        viewModel.passwordError.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                binding.tilPassword.error = getString(R.string.password_mismatch)
                binding.tilPassword.isErrorEnabled = true
            } else {
                binding.tilPassword.isErrorEnabled = false
            }
        }
    }
}