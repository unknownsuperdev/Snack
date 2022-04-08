package app.snack.ui.setupSnack

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSetupSnackBinding

class SetupSnackFragment: BindingFragment<FragmentSetupSnackBinding, SetupSnackViewModel>() {

    override val viewModel by viewModels<SetupSnackViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSetupSnackBinding::inflate
}