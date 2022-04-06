package app.snack.ui.main.screens.settings.screens.limit_traffic

import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsLimitTrafficBinding
import app.snack.utils.extensions.onClick
import splitties.toast.toast

class SettingsLimitTrafficFragment :
    BindingFragment<FragmentSettingsLimitTrafficBinding, SettingsLimitTrafficViewModel>() {

    override val viewModel by viewModels<SettingsLimitTrafficViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsLimitTrafficBinding::inflate

    override fun setupUI() {
        with(binding) {
            toolbar.setNavigationOnClickListener { closeScreen() }
            btnSave.onClick {
                toast("You’ve successfully changed the limit")
                closeScreen()
            }
        }
    }
}
