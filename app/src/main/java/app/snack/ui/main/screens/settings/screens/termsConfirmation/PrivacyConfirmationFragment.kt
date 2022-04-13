package app.snack.ui.main.screens.settings.screens.termsConfirmation

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentPrivacyConfirmationBinding
import app.snack.utils.extensions.onClick
import com.zackratos.ultimatebarx.ultimatebarx.statusBar

class PrivacyConfirmationFragment:
BindingFragment<FragmentPrivacyConfirmationBinding, PrivacyConfirmationViewModel>() {

    override val viewModel by activityViewModels<PrivacyConfirmationViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentPrivacyConfirmationBinding::inflate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.window?.statusBarColor = activity?.getColor(R.color.bg_main) ?: 0
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setupUI() {
        statusBar {
            fitWindow = true
            background.colorRes = R.color.bg_main
            light = true
        }
        binding.toolbar.setTitleTextAppearance(activity, R.style.InterMediumTextAppearance);
        binding.toolbar.setNavigationOnClickListener { activity?.finish() }
        binding.privacyPolicy.loadUrl(getString(R.string.privacy_policy))
        binding.btnAccept.onClick {
            viewModel.onTermsConfirmationScreenShow(false)
        }
    }
}