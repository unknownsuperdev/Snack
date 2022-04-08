package app.snack.ui.main.screens.settings.screens.change_email

import android.os.Build
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
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
        setupKeyboard()
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

    private fun setupKeyboard() {
        binding.root.setOnApplyWindowInsetsListener { _, windowInsets ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val imeHeight = windowInsets.getInsets(WindowInsets.Type.ime()).bottom
                binding.root.setPadding(0, 0, 0, imeHeight)
                val insets =
                    windowInsets.getInsets(WindowInsets.Type.ime() or WindowInsets.Type.systemGestures())
                insets
            } else {
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            }

            windowInsets
        }
    }

}
