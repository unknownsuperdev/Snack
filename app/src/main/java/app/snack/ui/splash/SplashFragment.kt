package app.snack.ui.splash

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSplashBinding
import app.snack.ui.main.SharedViewModel
import app.snack.utils.NavAction
import dagger.hilt.android.AndroidEntryPoint

class SplashFragment : BindingFragment<FragmentSplashBinding, SplashViewModel>() {

     override val viewModel by activityViewModels<SplashViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSplashBinding::inflate
}