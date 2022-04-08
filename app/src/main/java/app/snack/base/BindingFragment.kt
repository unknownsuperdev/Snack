package app.snack.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import app.snack.AppActivity
import app.snack.R
import app.snack.ui.login.LoginFragment
import app.snack.ui.login.screens.sign_in.SignInFragment
import app.snack.ui.login.screens.sign_in.SignInRestorePasswordFragment
import app.snack.ui.login.screens.sign_up.SignUpConfirmPasswordFragment
import app.snack.ui.login.screens.sign_up.SignUpCreatePasswordFragment
import app.snack.ui.login.screens.sign_up.SignUpEmailFragment
import app.snack.ui.login.screens.welcome.WelcomeFragment
import app.snack.ui.main.MainFragment
import app.snack.ui.main.screens.dashboard.DashboardFragment
import app.snack.ui.main.screens.profile.ProfileFragment
import app.snack.ui.main.screens.profile.screens.payout_history.ProfilePayoutHistoryFragment
import app.snack.ui.main.screens.profile.screens.payout_request.ProfilePayoutRequestFragment
import app.snack.ui.main.screens.settings.SettingsFragment
import app.snack.ui.main.screens.settings.screens.change_email.SettingsChangeEmailFragment
import app.snack.ui.main.screens.settings.screens.change_password.SettingsChangePasswordFragment
import app.snack.ui.main.screens.settings.screens.help.SettingsHelpFragment
import app.snack.ui.main.screens.settings.screens.help_details.SettingsHelpDetailsFragment
import app.snack.ui.main.screens.settings.screens.limit_traffic.SettingsLimitTrafficFragment
import app.snack.ui.main.screens.settings.screens.privacy_policy.SettingsPrivacyPolicyFragment
import app.snack.ui.main.screens.settings.screens.support.SettingsSupportFragment
import app.snack.ui.main.screens.settings.screens.terms.SettingsTermsFragment
import app.snack.ui.main.screens.settings.screens.termsConfirmation.PrivacyConfirmationFragment
import app.snack.ui.onboarding.OnboardingFragment
import app.snack.utils.NavAction
import app.snack.utils.Navigator
import app.snack.utils.Screen
import app.snack.utils.extensions.launchWhenStarted
import app.snack.utils.extensions.showDialogForgotPasswordDone
import kotlinx.coroutines.flow.onEach
import splitties.toast.toast

abstract class BindingFragment<out VB : ViewBinding, out VM : BaseViewModel> : Fragment() {

    private var _binding: ViewBinding? = null
    private var _viewModel: ViewModel? = null

    @Suppress("UNCHECKED_CAST")
    protected open val viewModel: VM?
        get() = _viewModel as? VM

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = _binding as VB

    protected abstract val bindingInflater: (LayoutInflater) -> ViewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater(inflater)

        return _binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        observeViewModels()

        viewModel?.onCreate()
    }

    open fun setupUI() = Unit

    open fun observeViewModels() {
        viewModel?.loading
            ?.onEach((requireActivity() as AppActivity)::showLoading)
            ?.launchWhenStarted(lifecycleScope)

        viewModel?.message
            ?.onEach(requireActivity()::toast)
            ?.launchWhenStarted(lifecycleScope)

        viewModel?.error
            ?.onEach(requireActivity()::toast)
            ?.launchWhenStarted(lifecycleScope)

        viewModel?.navigation
            ?.onEach { action ->
                when (action) {
                    is NavAction.Close -> closeScreen()
                    is NavAction.Show -> showScreen(action.screen, action.data)
                    is NavAction.Add -> addScreen(action.screen, action.data)
                }
            }
            ?.launchWhenStarted(lifecycleScope)
    }

    fun showScreen(screen: Screen, data: Any? = null) {
        Navigator.screens = mutableListOf(screen)

        when (screen) {
            Screen.ONBOARDING -> showFragment(
                fragment = OnboardingFragment(),
                animEnter = R.anim.slide_in_bottom,
                animExit = R.anim.slide_out_bottom,
            )
            Screen.LOGIN -> showFragment(fragment = LoginFragment())
            Screen.WELCOME -> showFragment(
                fm = childFragmentManager,
                fragment = WelcomeFragment(),
                animEnter = R.anim.slide_in_bottom,
                animExit = R.anim.slide_out_bottom,
            )
            Screen.MAIN -> showFragment(
                fm = requireActivity().supportFragmentManager,
                fragment = MainFragment()
            )
            Screen.DASHBOARD -> showFragment(
                fm = childFragmentManager,
                fragment = DashboardFragment()
            )
            Screen.PROFILE -> showFragment(
                fm = childFragmentManager,
                fragment = ProfileFragment()
            )
            Screen.SETTINGS -> showFragment(
                fm = childFragmentManager,
                fragment = SettingsFragment()
            )
            Screen.PRIVACY_CONFIRMATION -> showFragment(
                fragment = PrivacyConfirmationFragment(),
                animEnter = R.anim.slide_in_bottom,
                animExit = R.anim.slide_out_bottom,
            )
        }
    }

    fun addScreen(screen: Screen, data: Any? = null) {
        Navigator.screens.add(screen)

        when (screen) {
            Screen.FACEBOOK -> toast("Facebook")

            Screen.SIGN_UP_EMAIL -> showFragment(
                fragment = SignUpEmailFragment(),
                tag = screen.name,
                animEnter = R.anim.slide_in_bottom,
                animExit = R.anim.slide_out_bottom,
            )
            Screen.SIGN_UP_CREATE_PASSWORD -> showFragment(
                fragment = SignUpCreatePasswordFragment(),
                tag = screen.name,
                animEnter = R.anim.slide_in_bottom,
                animExit = R.anim.slide_out_bottom,
            )
            Screen.SIGN_UP_CONFIRM_PASSWORD -> showFragment(
                fragment = SignUpConfirmPasswordFragment(),
                tag = screen.name,
                animEnter = R.anim.slide_in_bottom,
                animExit = R.anim.slide_out_bottom,
            )

            Screen.SIGN_IN -> showFragment(
                fragment = SignInFragment(),
                tag = screen.name,
                animEnter = R.anim.slide_in_bottom,
                animExit = R.anim.slide_out_bottom,
            )
            Screen.SIGN_IN_RESTORE_PASSWORD -> showFragment(
                fragment = SignInRestorePasswordFragment(),
                tag = screen.name,
                animEnter = R.anim.slide_in_bottom,
                animExit = R.anim.slide_out_bottom,
            )
            Screen.SIGN_IN_RESTORE_PASSWORD_DONE -> context?.showDialogForgotPasswordDone()

            Screen.PROFILE_PAYOUT_REQUEST -> showFragment(
                fragment = ProfilePayoutRequestFragment(),
                modal = true,
                tag = screen.name,
                animEnter = R.anim.slide_in_right,
                animExit = R.anim.slide_out_right,
            )
            Screen.PROFILE_PAYOUT_HISTORY -> showFragment(
                fragment = ProfilePayoutHistoryFragment(),
                modal = true,
                tag = screen.name,
                animEnter = R.anim.slide_in_right,
                animExit = R.anim.slide_out_right,
            )

            Screen.SETTINGS_CHANGE_EMAIL -> showFragment(
                fragment = SettingsChangeEmailFragment(),
                modal = true,
                tag = screen.name,
                animEnter = R.anim.slide_in_right,
                animExit = R.anim.slide_out_right,
            )
            Screen.SETTINGS_CHANGE_PASSWORD -> showFragment(
                fragment = SettingsChangePasswordFragment(),
                modal = true,
                tag = screen.name,
                animEnter = R.anim.slide_in_right,
                animExit = R.anim.slide_out_right,
            )
            Screen.SETTINGS_LIMIT_TRAFFIC -> showFragment(
                fragment = SettingsLimitTrafficFragment(),
                modal = true,
                tag = screen.name,
                animEnter = R.anim.slide_in_right,
                animExit = R.anim.slide_out_right,
            )
            Screen.SETTINGS_HELP -> showFragment(
                fragment = SettingsHelpFragment(),
                modal = true,
                tag = screen.name,
                animEnter = R.anim.slide_in_right,
                animExit = R.anim.slide_out_right,
            )
            Screen.SETTINGS_HELP_DETAILS -> {

                val fragment = SettingsHelpDetailsFragment()

                data?.let {
                    when(it) {
                        is Int -> {
                            fragment.arguments = Bundle().apply {
                                putInt("id", it)
                            }
                        }
                    }
                }

                showFragment(
                    fragment = fragment,
                    modal = true,
                    tag = screen.name,
                    animEnter = R.anim.slide_in_right,
                    animExit = R.anim.slide_out_right,
                )
            }
            Screen.SETTINGS_SUPPORT -> {
                showFragment(
                    fragment = SettingsSupportFragment(),
                    modal = true,
                    tag = screen.name,
                    animEnter = R.anim.slide_in_right,
                    animExit = R.anim.slide_out_right,
                )
            }
            Screen.SETTINGS_PRIVACY_POLICY -> showFragment(
                fragment = SettingsPrivacyPolicyFragment(),
                modal = true,
                tag = screen.name,
                animEnter = R.anim.slide_in_right,
                animExit = R.anim.slide_out_right,
            )
            Screen.SETTINGS_TERMS -> showFragment(
                fragment = SettingsTermsFragment(),
                modal = true,
                tag = screen.name,
                animEnter = R.anim.slide_in_right,
                animExit = R.anim.slide_out_right,
            )
        }
    }

    fun closeScreen() {
        Navigator.screens.removeLast()
        parentFragmentManager.popBackStack()
    }

    private fun showFragment(
        modal: Boolean = false,
        fm: FragmentManager = parentFragmentManager,
        fragment: Fragment,
        tag: String? = null,
        animEnter: Int? = null,
        animExit: Int? = null,
    ) {
        if (tag == null) {
            fm.commit {
                setCustomAnimations(
                    animEnter ?: 0,
                    animExit ?: 0,
                    animEnter ?: 0,
                    animExit ?: 0,
                )
                replace(if (modal) R.id.container2 else R.id.container, fragment)
            }
        } else {
            fm.commit {
                setCustomAnimations(
                    animEnter ?: 0,
                    animExit ?: 0,
                    animEnter ?: 0,
                    animExit ?: 0,
                )
                add(if (modal) R.id.container2 else R.id.container, fragment, tag)
                addToBackStack(tag)
            }
        }
    }
}