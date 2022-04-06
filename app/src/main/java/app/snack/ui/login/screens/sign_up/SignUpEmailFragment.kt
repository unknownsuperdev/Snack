package app.snack.ui.login.screens.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSignUpEmailBinding
import app.snack.ui.login.LoginViewModel
import app.snack.utils.Screen
import app.snack.utils.extensions.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpEmailFragment : BindingFragment<FragmentSignUpEmailBinding, LoginViewModel>() {

    override val viewModel by activityViewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSignUpEmailBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragment?.view?.findViewById<Toolbar>(R.id.toolbar)?.show()
        viewModel.amplitudeSignUpViewed()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        parentFragment?.view?.findViewById<Toolbar>(R.id.toolbar)?.hide()
    }

    override fun setupUI() {
        with(binding) {
            tilEmail.isEndIconVisible = false

            etEmail.addOnImeActionClick { fabNext.callOnClick() }
            etEmail.onTextChanged {
                tilEmail.isErrorEnabled = false
                checkNextEnable()
            }

            cbTerms.setOnCheckedChangeListener { _, _ -> checkNextEnable() }

            btnTerms.onClick {
                // addScreen(Screen.SETTINGS_TERMS)
            }
            btnSignIn.onClick { addScreen(Screen.SIGN_IN) }
            fabNext.onClick {
                viewModel.enterEmail(etEmail.text())
                viewModel.checkEmail(etEmail.text()).observe(viewLifecycleOwner) {
                    if(it) {
                        addScreen(Screen.SIGN_UP_CREATE_PASSWORD)
                    } else {
                        tilEmail.isErrorEnabled = true
                        tilEmail.error = "Email already registered"
                    }
                }
            }
        }
    }

    private fun checkNextEnable() {
        val isEmailValid = binding.etEmail.text().isValidEmail

        binding.fabNext.enabled(isEmailValid && binding.cbTerms.isChecked)
        binding.tilEmail.isEndIconVisible = isEmailValid
    }
}