package app.snack.ui.main.screens.settings.screens.support

import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsSupportBinding
import app.snack.ui.main.screens.settings.screens.alerts.SettingsMessageSentAlert
import app.snack.utils.Screen
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.onTextChanged
import app.snack.utils.extensions.text

class SettingsSupportFragment :
    BindingFragment<FragmentSettingsSupportBinding, SettingsSupportViewModel>() {

    override val viewModel by activityViewModels<SettingsSupportViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsSupportBinding::inflate

    override fun setupUI() {

        binding.etQuestion.onTextChanged {
            binding.tilQuestion.isErrorEnabled = false
        }

        with(binding) {
            toolbar.setNavigationOnClickListener { closeScreen() }
            btnSend.onClick {

                if(binding.etQuestion.text.isNullOrEmpty()) {
                    binding.tilQuestion.isErrorEnabled = true
                    binding.tilQuestion.error = getString(R.string.question_required)
                } else {

                    viewModel.supportSendEmail(binding.etQuestion.text()).observe(viewLifecycleOwner) {
                        SettingsMessageSentAlert({
                                Log.d("AAA", "close")
                                showScreen(Screen.MAIN)
                                // closeScreen()
                            }, {
                                // on cancel
                            binding.etQuestion.setText("")
                            }
                        ).show(childFragmentManager, "Alert")
                    }
                }

            }
        }
    }
}
