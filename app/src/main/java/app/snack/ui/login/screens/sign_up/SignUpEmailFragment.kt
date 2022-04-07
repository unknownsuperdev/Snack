package app.snack.ui.login.screens.sign_up

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
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
        setupKeyboard()
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
                    if (it) {
                        addScreen(Screen.SIGN_UP_CREATE_PASSWORD)
                    } else {
                        tilEmail.isErrorEnabled = true
                        tilEmail.error = "Email already registered"
                    }
                }
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

    private fun checkNextEnable() {
        val isEmailValid = binding.etEmail.text().isValidEmail

        binding.fabNext.enabled(isEmailValid && binding.cbTerms.isChecked)
        binding.tilEmail.isEndIconVisible = isEmailValid
    }
}