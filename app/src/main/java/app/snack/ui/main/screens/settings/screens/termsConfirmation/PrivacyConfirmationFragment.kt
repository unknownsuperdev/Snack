package app.snack.ui.main.screens.settings.screens.termsConfirmation

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentPrivacyConfirmationBinding
import app.snack.utils.extensions.onClick

class PrivacyConfirmationFragment:
BindingFragment<FragmentPrivacyConfirmationBinding, PrivacyConfirmationViewModel>() {

    override val viewModel by activityViewModels<PrivacyConfirmationViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPrivacyConfirmationBinding::inflate

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { activity?.finish() }
        binding.privacyPolicy.loadUrl(getString(R.string.privacy_policy))
        binding.btnAccept.onClick {
            viewModel.onTermsConfirmationScreenShow(false)
        }
    }
}