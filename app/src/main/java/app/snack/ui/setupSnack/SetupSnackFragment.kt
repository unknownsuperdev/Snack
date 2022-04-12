package app.snack.ui.setupSnack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSetupSnackBinding
import app.snack.utils.Screen
import app.snack.utils.enums.SetupSnackCheckableButtonsStateEnum
import app.snack.utils.extensions.enabled
import app.snack.utils.extensions.onClick
import app.snack.utils.hideKeyboard
import com.zackratos.ultimatebarx.ultimatebarx.statusBar


class SetupSnackFragment : BindingFragment<FragmentSetupSnackBinding, SetupSnackViewModel>() {

    override val viewModel by activityViewModels<SetupSnackViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentSetupSnackBinding::inflate

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
        with(binding) {
            btnNo.setOnClickListener {
                viewModel.onButtonNoClicked()
            }
            btnYes.setOnClickListener {
                viewModel.onButtonYesClicked()
            }
            swcSetupLimit.setOnCheckedChangeListener { buttonView, isChecked ->
                onSetupLimitSwitchButtonChecked(isChecked)
                viewModel.isDataLimitSwitchButtonChecked = isChecked
            }
            etMobileDataLimit.addTextChangedListener {
                val value: String = it.toString()
                val finalValue = value.toInt()
                viewModel.mobileDataUsageLimit = finalValue
            }
            btnDone.onClick {
                viewModel.setDataLimit()
                showScreen(Screen.LOGIN)
            }
            etMobileDataLimit.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    view?.hideKeyboard()
                }
            }
        }
    }

    private fun onSetupLimitSwitchButtonChecked(checked: Boolean) {
        with(binding) {
            if (checked) {
                tvStatisticHint.visibility = VISIBLE
                etMobileDataLimitType.visibility = VISIBLE
                etMobileDataLimit.visibility = VISIBLE
                tvLimitMobileDataHint.visibility = VISIBLE
                activity?.resources?.getColor(R.color.text_normal, requireActivity().theme)?.let {
                    tvSetupLimit.setTextColor(
                        it
                    )
                }
                activity?.resources?.getColor(R.color.text_normal, requireActivity().theme)?.let {
                    tvLimitMobileData.setTextColor(
                        it
                    )
                }
            } else {
                tvStatisticHint.visibility = GONE
                etMobileDataLimitType.visibility = GONE
                etMobileDataLimit.visibility = GONE
                tvLimitMobileDataHint.visibility = GONE
                activity?.resources?.getColor(R.color.text_normal, requireActivity().theme)?.let {
                    tvSetupLimit.setTextColor(
                        it
                    )
                }
                activity?.resources?.getColor(R.color.text_grey, requireActivity().theme)?.let {
                    tvLimitMobileData.setTextColor(
                        it
                    )
                }

            }
        }
    }

    override fun observeViewModels() {
        super.observeViewModels()

        viewModel.checkableButtonsState.observe(viewLifecycleOwner) {
            when (it) {
                SetupSnackCheckableButtonsStateEnum.NOTHING -> {
                    onCheckableButtonsUnchecked()
                }
                SetupSnackCheckableButtonsStateEnum.YES -> {
                    onYesButtonChecked()
                }
                SetupSnackCheckableButtonsStateEnum.NO -> {
                    onNoButtonChecked()
                }
            }
        }
    }

    private fun onCheckableButtonsUnchecked() {
        with(binding) {
            btnYes.isSelected = false
            btnNo.isSelected = false
            swcSetupLimit.visibility = GONE
            tvSetupLimitHint.visibility = GONE
            tvLimitMobileDataHint.visibility = GONE
            etMobileDataLimit.visibility = GONE
            etMobileDataLimitType.visibility = GONE
            tvStatisticHint.visibility = GONE
            btnDone.enabled(false)
            swcSetupLimit.isChecked = false
            activity?.resources?.getColor(R.color.text_grey, requireActivity().theme)?.let {
                tvSetupLimit.setTextColor(
                    it
                )
            }
            activity?.resources?.getColor(R.color.text_grey, requireActivity().theme)?.let {
                tvLimitMobileData.setTextColor(
                    it
                )
            }
        }
    }

    private fun onNoButtonChecked() {
        with(binding) {
            btnYes.isSelected = false
            btnNo.isSelected = true
            swcSetupLimit.visibility = GONE
            tvSetupLimitHint.visibility = GONE
            tvLimitMobileDataHint.visibility = GONE
            etMobileDataLimit.visibility = GONE
            etMobileDataLimitType.visibility = GONE
            tvStatisticHint.visibility = GONE
            btnDone.enabled(true)
            swcSetupLimit.isChecked = false
            activity?.resources?.getColor(R.color.text_grey, requireActivity().theme)?.let {
                tvSetupLimit.setTextColor(
                    it
                )
            }
            activity?.resources?.getColor(R.color.text_grey, requireActivity().theme)?.let {
                tvLimitMobileData.setTextColor(
                    it
                )
            }
        }
    }

    private fun onYesButtonChecked() {
        with(binding) {
            btnYes.isSelected = true
            btnNo.isSelected = false
            swcSetupLimit.visibility = VISIBLE
            tvSetupLimitHint.visibility = VISIBLE
            tvLimitMobileDataHint.visibility = GONE
            etMobileDataLimit.visibility = GONE
            etMobileDataLimitType.visibility = GONE
            tvStatisticHint.visibility = GONE
            btnDone.enabled(true)
            activity?.resources?.getColor(R.color.text_black, requireActivity().theme)?.let {
                tvSetupLimit.setTextColor(
                    it
                )
            }
            activity?.resources?.getColor(R.color.text_grey, requireActivity().theme)?.let {
                tvLimitMobileData.setTextColor(
                    it
                )
            }
        }
    }
}