package app.snack.ui.main.screens.settings.screens.change_email

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsChangeEmailBinding
import app.snack.utils.extensions.*
import splitties.toast.toast

class SettingsChangeEmailFragment :
    BindingFragment<FragmentSettingsChangeEmailBinding, SettingsChangeEmailViewModel>() {

    override val viewModel by activityViewModels<SettingsChangeEmailViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsChangeEmailBinding::inflate

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { closeScreen() }
        binding.tilEmail.isEndIconVisible = false
        binding.etEmail.addOnImeActionClick { binding.btnSave.callOnClick() }

        binding.etEmail.onTextChanged {
            binding.tilEmail.isErrorEnabled = it.isValidEmail.not()
        }

        binding.btnSave.onClick {
            val emailToChange = binding.etEmail.text()
            binding.tilEmail.isErrorEnabled = emailToChange.isValidEmail.not()
            if(emailToChange.isValidEmail) {
                viewModel.changeEmail(emailToChange)
            } else {
                binding.tilEmail.error = getString(R.string.invalid_email)
            }
        }
    }

}
