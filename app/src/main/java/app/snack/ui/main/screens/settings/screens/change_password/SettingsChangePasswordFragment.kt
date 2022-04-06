package app.snack.ui.main.screens.settings.screens.change_password

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsChangePasswordBinding
import app.snack.utils.extensions.*
import splitties.toast.toast
import splitties.views.textColorResource

class SettingsChangePasswordFragment :
    BindingFragment<FragmentSettingsChangePasswordBinding, SettingsChangePasswordViewModel>() {

    override val viewModel by activityViewModels<SettingsChangePasswordViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsChangePasswordBinding::inflate

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { closeScreen() }
        binding.etPassword.addOnImeActionClick { binding.btnSave.callOnClick() }

        binding.etPassword.onTextChanged {
            binding.tilPassword.isErrorEnabled = false
        }

        showOldPasswordState()
        //showNewPasswordState()
    }


    private fun showOldPasswordState() {
        binding.hint.text = "Old password"

        binding.etPassword.hint = "Enter your password"
        binding.etPassword.setText("")

        binding.groupLevels.hide()
        binding.groupPasswordStatus.hide()


        binding.btnSave.onClick {

            if(binding.etPassword.text() == viewModel.currentUserPassword) {
                showNewPasswordState()
            } else {
                binding.tilPassword.isErrorEnabled = true
                binding.tilPassword.error = "Old password does not match"
            }
        }
    }

    private fun showNewPasswordState() {
        binding.hint.text = "New password"

        binding.etPassword.hint = "Enter your new password"
        binding.etPassword.setText("")

        binding.groupLevels.show()
        binding.groupPasswordStatus.show()

        binding.etPassword.onTextChanged(::checkPassword)

        binding.btnSave.onClick {
            viewModel.changePassword(binding.etPassword.text())
        }
    }

    private fun checkPassword(password: String) {
        with(binding) {
            tvPasswordLevel.showed(password.isNotEmpty())
            tvPasswordComment.showed(password.isNotEmpty())

            viewPasswordLevel1.setTintResource(R.color.gray_default)
            viewPasswordLevel2.setTintResource(R.color.gray_default)
            viewPasswordLevel3.setTintResource(R.color.gray_default)
            viewPasswordLevel4.setTintResource(R.color.gray_default)

            btnSave.disable()

            if (password.contains8Characters) {
                viewPasswordLevel1.setTintResource(R.color.yellow)
                viewPasswordLevel2.setTintResource(R.color.yellow)
                viewPasswordLevel3.setTintResource(R.color.gray_default)
                viewPasswordLevel4.setTintResource(R.color.gray_default)

                tvPasswordLevel.textColorResource = R.color.yellow
                tvPasswordLevel.text = "Average"

                btnSave.disable()

                if (password.containsDigits) {
                    viewPasswordLevel1.setTintResource(R.color.green_light)
                    viewPasswordLevel2.setTintResource(R.color.green_light)
                    viewPasswordLevel3.setTintResource(R.color.green_light)
                    viewPasswordLevel4.setTintResource(R.color.gray_default)

                    tvPasswordLevel.textColorResource = R.color.green_light
                    tvPasswordLevel.text = "Good to go, but can be better"

                    tvPasswordComment.text = "Add special character"

                    btnSave.enable()

                    if (password.containsSpecialSymbols) {
                        viewPasswordLevel1.setTintResource(R.color.green)
                        viewPasswordLevel2.setTintResource(R.color.green)
                        viewPasswordLevel3.setTintResource(R.color.green)
                        viewPasswordLevel4.setTintResource(R.color.green)

                        tvPasswordLevel.textColorResource = R.color.green
                        tvPasswordLevel.text = "Just perfect!"

                        tvPasswordComment.hide()

                        btnSave.enable()
                    }
                } else {
                    tvPasswordComment.text = "Add numbers"
                }
            } else {
                viewPasswordLevel1.setTintResource(R.color.orange)
                viewPasswordLevel2.setTintResource(R.color.gray_default)
                viewPasswordLevel3.setTintResource(R.color.gray_default)
                viewPasswordLevel4.setTintResource(R.color.gray_default)

                tvPasswordLevel.textColorResource = R.color.orange
                tvPasswordLevel.text = "Week"

                btnSave.disable()
            }
        }
    }
}
