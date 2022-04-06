package app.snack.ui.main.screens.settings.screens.privacy_policy

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsPrivacyPolicyBinding

class SettingsPrivacyPolicyFragment :
    BindingFragment<FragmentSettingsPrivacyPolicyBinding, SettingsPrivacyPolicyViewModel>() {

    override val viewModel by viewModels<SettingsPrivacyPolicyViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsPrivacyPolicyBinding::inflate

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { closeScreen() }
        binding.privacyPolicy.loadUrl(getString(R.string.privacy_policy))
    }
}
