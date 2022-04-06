package app.snack.ui.main.screens.settings.screens.terms

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsTermsBinding

class SettingsTermsFragment :
    BindingFragment<FragmentSettingsTermsBinding, SettingsTermsViewModel>() {

    override val viewModel by viewModels<SettingsTermsViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsTermsBinding::inflate

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { closeScreen() }
        binding.termsOfUse.loadUrl(getString(R.string.terms_of_use))
    }
}
