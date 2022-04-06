package app.snack.ui.main.screens.settings

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.BuildConfig
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSettingsBinding
import app.snack.service.SnackService
import app.snack.ui.main.SharedViewModel
import app.snack.utils.Screen
import app.snack.utils.extensions.onCheck
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.showAlertDisableMobileData
import app.snack.utils.extensions.showAlertLogout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes

class SettingsFragment : BindingFragment<FragmentSettingsBinding, SettingsViewModel>() {

    override val viewModel by activityViewModels<SettingsViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSettingsBinding::inflate

    override fun setupUI() {
        with(binding) {
            btnChangeEmail.onClick { addScreen(Screen.SETTINGS_CHANGE_EMAIL) }
            btnChangePassword.onClick { addScreen(Screen.SETTINGS_CHANGE_PASSWORD) }

            btnLimitTraffic.onClick { addScreen(Screen.SETTINGS_LIMIT_TRAFFIC) }
            btnHelp.onClick { addScreen(Screen.SETTINGS_HELP) }
            btnPrivacyPolicy.onClick { addScreen(Screen.SETTINGS_PRIVACY_POLICY) }
            btnTerms.onClick { addScreen(Screen.SETTINGS_TERMS) }
            btnLogout.onClick {
                showAlertLogout {

                    requireContext().sendBroadcast(
                        Intent(SnackService.SERVICE_ACTION).putExtra(
                            SnackService.SERVICE_STATE_EXTRA, false))

                    sharedViewModel.logout()
                    showScreen(Screen.LOGIN)

//                    val googleSignInClient = GoogleSignIn.getClient(
//                        requireActivity(), GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                            .requestEmail()
//                            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
//                            .build()
//                    )
//
//                    googleSignInClient.revokeAccess().addOnCompleteListener {
//                        googleSignInClient.signOut()
//                        sharedViewModel.logout()
//                        showScreen(Screen.LOGIN)
//                    }
                }
            }

            switchStopSharingData.onCheck { isChecked ->
                viewModel.setStopSharingOnLowBattery(isChecked)
            }

        }
    }

    override fun observeViewModels() {
        super.observeViewModels()

        viewModel.fetchAllowOnMobileTraffic().observe(viewLifecycleOwner) { isCheckedFetchedValue ->
            binding.switchAllowMobileData.isChecked = isCheckedFetchedValue

            binding.switchAllowMobileData.onCheck { isChecked ->
                if(isChecked.not()) {
                    showAlertDisableMobileData({
                        viewModel.setAllowMobileData(false)
                    }, {
                        // cancelled
                        binding.switchAllowMobileData.isChecked = true
                        viewModel.setAllowMobileData(true)
                    })
                } else {
                    viewModel.setAllowMobileData(true)
                }
            }
        }

        viewModel.fetchStopValueOnLowBattery().observe(viewLifecycleOwner) { isCheckedFetchedValue ->
            binding.switchStopSharingData.isChecked = isCheckedFetchedValue
        }

        viewModel.isNativeAuthentication().observe(viewLifecycleOwner) { isNativeAuth ->

            val visibility = if(isNativeAuth) View.VISIBLE else View.GONE

            binding.btnChangeEmail.visibility = visibility
            binding.divider.visibility = visibility

        }
    }
}