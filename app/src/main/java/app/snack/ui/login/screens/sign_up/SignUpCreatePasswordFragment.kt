package app.snack.ui.login.screens.sign_up

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSignUpCreatePasswordBinding
import app.snack.ui.login.LoginViewModel
import app.snack.utils.Screen
import app.snack.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint
import splitties.views.textColorResource

@AndroidEntryPoint
class SignUpCreatePasswordFragment :
    BindingFragment<FragmentSignUpCreatePasswordBinding, LoginViewModel>() {

    override val viewModel by activityViewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignUpCreatePasswordBinding::inflate

    override fun setupUI() {
        with(binding) {
            etPassword.addOnImeActionClick { fabNext.callOnClick() }
            etPassword.onTextChanged(::checkPassword)
            fabNext.onClick {
                viewModel.enterPassword(etPassword.text())
                addScreen(Screen.SIGN_UP_CONFIRM_PASSWORD)
            }
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

            fabNext.disable()

            if (password.contains8Characters) {
                viewPasswordLevel1.setTintResource(R.color.yellow)
                viewPasswordLevel2.setTintResource(R.color.yellow)
                viewPasswordLevel3.setTintResource(R.color.gray_default)
                viewPasswordLevel4.setTintResource(R.color.gray_default)

                tvPasswordLevel.textColorResource = R.color.yellow
                tvPasswordLevel.text = "Average"

                fabNext.disable()

                if (password.containsDigits) {
                    viewPasswordLevel1.setTintResource(R.color.green_light)
                    viewPasswordLevel2.setTintResource(R.color.green_light)
                    viewPasswordLevel3.setTintResource(R.color.green_light)
                    viewPasswordLevel4.setTintResource(R.color.gray_default)

                    tvPasswordLevel.textColorResource = R.color.green_light
                    tvPasswordLevel.text = "Good to go, but can be better"

                    tvPasswordComment.text = "Add special character"

                    fabNext.enable()

                    if (password.containsSpecialSymbols) {
                        viewPasswordLevel1.setTintResource(R.color.green)
                        viewPasswordLevel2.setTintResource(R.color.green)
                        viewPasswordLevel3.setTintResource(R.color.green)
                        viewPasswordLevel4.setTintResource(R.color.green)

                        tvPasswordLevel.textColorResource = R.color.green
                        tvPasswordLevel.text = "Just perfect!"

                        tvPasswordComment.hide()

                        fabNext.enable()
                    }
                } else {
                    tvPasswordComment.text = "Add numbers"
                }
            } else if (password.isEmpty()) {
                viewPasswordLevel1.setTintResource(R.color.gray_default)
                viewPasswordLevel2.setTintResource(R.color.gray_default)
                viewPasswordLevel3.setTintResource(R.color.gray_default)
                viewPasswordLevel4.setTintResource(R.color.gray_default)
                
                fabNext.disable()
            } else {
                viewPasswordLevel1.setTintResource(R.color.orange)
                viewPasswordLevel2.setTintResource(R.color.gray_default)
                viewPasswordLevel3.setTintResource(R.color.gray_default)
                viewPasswordLevel4.setTintResource(R.color.gray_default)

                tvPasswordLevel.textColorResource = R.color.orange
                tvPasswordLevel.text = "Weak"

                fabNext.disable()
            }
        }
    }
}
