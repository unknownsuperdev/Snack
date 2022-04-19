package app.snack.ui.main.screens.settings.screens.termsAndConditions

import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsTermsBinding
import app.snack.databinding.FragmentTermsAndConditionsBinding

class TermsAndConditionsFragment :
    BindingFragment<FragmentTermsAndConditionsBinding, TermsAndConditionsViewModel>() {

    override val viewModel by activityViewModels<TermsAndConditionsViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentTermsAndConditionsBinding::inflate

    override fun setupUI() {
        binding.termsOfUse.loadUrl(getString(R.string.terms_of_use))
    }
}