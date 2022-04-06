package app.snack.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentMainBinding
import app.snack.utils.Screen
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.setDrawableTint
import app.snack.utils.extensions.showAlertSignUpBonus
import com.zackratos.ultimatebarx.ultimatebarx.statusBar
import kotlinx.coroutines.*
import splitties.views.textColorResource

class MainFragment : BindingFragment<FragmentMainBinding, MainViewModel>() {

    override val viewModel by viewModels<MainViewModel>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private var job: Job? = null

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentMainBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (childFragmentManager.backStackEntryCount != 0) {
                        childFragmentManager.popBackStack()
                    } else {
                        isEnabled = false
                        activity?.onBackPressed()
                    }
                }
            })
    }

    override fun onDestroy() {
        job?.cancel()
        super.onDestroy()
    }

    override fun setupUI() {
        super.setupUI()

        statusBar {
            fitWindow = true
            background.colorRes = R.color.bg_main
            light = true
        }

        with(binding) {
            btnDashboard.onClick {
                selectItem(binding.btnDashboard)
                showScreen(Screen.DASHBOARD)
            }
            btnProfile.onClick {
                selectItem(binding.btnProfile)
                showScreen(Screen.PROFILE)
            }
            btnSettings.onClick {
                selectItem(binding.btnSettings)
                showScreen(Screen.SETTINGS)
            }

            btnDashboard.callOnClick()
        }

    }

    override fun observeViewModels() {
        super.observeViewModels()
        sharedViewModel.profile.observe(viewLifecycleOwner) {
            it?.let {
                binding.navigation.visibility = View.VISIBLE
            } ?: run {
                binding.navigation.visibility = View.GONE
            }
        }
    }

    private fun selectItem(item: TextView) {
        clearNavigationSelecting()
        item.setDrawableTint(R.color.accent)
        item.textColorResource = R.color.text_normal
    }

    private fun hideNavigationBar() {
        binding.navigation.visibility = View.INVISIBLE
    }

    private fun clearNavigationSelecting() {
        binding.btnDashboard.textColorResource = R.color.grey_light
        binding.btnProfile.textColorResource = R.color.grey_light
        binding.btnSettings.textColorResource = R.color.grey_light

        binding.btnDashboard.setDrawableTint(R.color.grey_light)
        binding.btnProfile.setDrawableTint(R.color.grey_light)
        binding.btnSettings.setDrawableTint(R.color.grey_light)
    }
}