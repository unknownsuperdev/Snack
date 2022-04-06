package app.snack.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentLoginBinding
import app.snack.utils.Screen
import com.zackratos.ultimatebarx.ultimatebarx.statusBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BindingFragment<FragmentLoginBinding, LoginViewModel>() {

    override val viewModel by viewModels<LoginViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentLoginBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (childFragmentManager.backStackEntryCount != 0) {
                        childFragmentManager.popBackStack()
                    } else {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }
            })
    }

    override fun setupUI() {
        statusBar {
            fitWindow = true
            background.colorRes = R.color.status_bar_sign_nup
        }

        binding.toolbar.setNavigationOnClickListener { childFragmentManager.popBackStack() }

        showScreen(Screen.WELCOME)
    }
}