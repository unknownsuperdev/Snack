package app.snack.ui.main.screens.dashboard

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startForegroundService
import androidx.core.text.parseAsHtml
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.PowerSavingModeActivity
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentDashboardBinding
import app.snack.service.SnackService
import app.snack.ui.main.SharedViewModel
import app.snack.utils.ConnectionLiveData
import android.content.ActivityNotFoundException
import android.view.animation.AccelerateInterpolator
import androidx.lifecycle.lifecycleScope
import app.snack.utils.extensions.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class DashboardFragment : BindingFragment<FragmentDashboardBinding, DashboardViewModel>() {

    override val viewModel by activityViewModels<DashboardViewModel>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentDashboardBinding::inflate


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!viewModel.isBonusGiven) {
            viewModel.checkNegativeTransactionsWithDelay().observe(viewLifecycleOwner) {
                if(it) {
                    context?.showAlertSignUpBonus({
                        viewModel.addNegativeTransaction().observe(viewLifecycleOwner) { transactionAdded ->
                            if(transactionAdded) {
                                if(binding.btnShareTraffic.isChecked.not()) {
                                    startAnimation()
                                    toggleForegroundServiceState(true)
                                    viewModel.reportTrafficSharingEnabled()
                                }
                                sharedViewModel.fetchProfile()
                            }
                            viewModel.setBonusGiven()
                        }
                    }, {
                        viewModel.setBonusGiven()
                    })
                }
            }
        }

    }

    override fun setupUI() {
        // binding.tvGathered.text = "Gathered on this device: <b>0.0 B</b>".parseAsHtml()

        if(viewModel.lastShareButtonState) {
            // viewModel.reportTrafficSharingEnabled()
            toggleForegroundServiceState(true)
        } else {
            toggleForegroundServiceState(false)
        }

        binding.switchPowerSaving.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startActivity(Intent(context, PowerSavingModeActivity::class.java))
            }
        }

        binding.btnSendEmail.setOnClickListener {
            binding.emailConfirmationRequired.visibility = View.GONE
            viewModel.sendConfirmation()
        }


        binding.btnShareTraffic.setOnClickListener {
            if(binding.btnShareTraffic.isChecked) {
                viewModel.reportTrafficSharingEnabled()
                toggleForegroundServiceState(true)
                startAnimation()
            } else {
                viewModel.reportTrafficSharingDisabled()
                toggleForegroundServiceState(false)
                stopAnimation()
            }
        }


        sharedViewModel.fetchProfile()

        lifecycleScope.launch {
            while (true) {
                delay(30000)
                sharedViewModel.fetchProfile()
            }
        }

    }

    override fun observeViewModels() {
        super.observeViewModels()

        sharedViewModel.needToUpdate.observe(viewLifecycleOwner) {
            if(it) {
                requireContext().showNewVersion {
                    openWebPage(getString(R.string.snack_site))
                }
            }
        }

        sharedViewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {

                binding.emailConfirmationRequired.visibility = if(profile.emailConfirmed) View.GONE else View.VISIBLE

                binding.tvEarned.text = String.format("%.3f", it.currentBalance.dollar)
                binding.tvGathered.text = resources.getString(R.string.gathered_on_this_device, it.trafficPerWeek.readableFormat())
                binding.tvGatheredToday.text = String.format("%.0 MB", it.trafficPerToday.toMB(2))
                binding.tvEarnedToday.text = String.format("%.3f$", it.moneyEarnedPerToday.cent.toUsd(2))
            }
        }

        viewModel.confirmationEmailSent.observe(viewLifecycleOwner) {
            if(it) {
                binding.btnSendEmail.setText(R.string.resend_email)
            } else {
                binding.btnSendEmail.setText(R.string.send_email)
            }
        }

        ConnectionLiveData(requireContext()).observe(viewLifecycleOwner) { isInternetConnected ->
            if(isInternetConnected) {
                binding.labelConnecting.text = getString(R.string.on_air_wi_fi)
            } else {
                binding.labelConnecting.text = getString(R.string.not_connected)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.switchPowerSaving.isChecked = false
    }

    private fun startAnimation() {
        binding.lottieArrow.playAnimation()
        binding.lottieMain.playAnimation()
        binding.lottieMain.clearAnimation()
        val oa = ObjectAnimator.ofFloat(binding.lottieMain, "alpha", 0f, 1f)
        oa.duration = 1000
        oa.interpolator = AccelerateInterpolator()
        oa.start()
    }

    private fun stopAnimation() {
        binding.lottieArrow.cancelAnimation()
        binding.lottieMain.cancelAnimation()
        binding.lottieMain.clearAnimation()
        val oa = ObjectAnimator.ofFloat(binding.lottieMain, "alpha", 1f, 0f)
        oa.duration = 1000
        oa.interpolator = AccelerateInterpolator()
        oa.start()
    }

    private fun toggleForegroundServiceState(newState: Boolean) {
        if(newState) {
            if (SnackService.isAppInForeground.not()) {
                startForegroundService(
                    requireContext(),
                    Intent(requireContext(), SnackService::class.java)
                )
            }
            binding.btnShareTraffic.isChecked = true
            viewModel.lastShareButtonState = true
            startAnimation()
        } else {
            viewModel.lastShareButtonState = false
            binding.btnShareTraffic.isChecked = false
            requireContext().sendBroadcast(Intent(SnackService.SERVICE_ACTION).putExtra(SnackService.SERVICE_STATE_EXTRA, false))
        }
    }

    private fun openWebPage(url: String?) {
        try {
            val webpage = Uri.parse(url)
            val myIntent = Intent(Intent.ACTION_VIEW, webpage)
            startActivity(myIntent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                "No application can handle this request. Please install a web browser or check your URL.",
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }

}