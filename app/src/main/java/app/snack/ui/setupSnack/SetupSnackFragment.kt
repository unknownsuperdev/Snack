package app.snack.ui.setupSnack

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentSetupSnackBinding
import app.snack.utils.enums.SetupSnackCheckableButtonsStateEnum
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
        activity?.window?.statusBarColor = activity?.getColor(R.color.white) ?: 0
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

    private fun onNoButtonChecked() {
        with(binding) {
            btnYes.isSelected = false
            btnNo.isSelected = true
        }
    }

    private fun onYesButtonChecked() {
        with(binding) {
            btnYes.isSelected = true
            btnNo.isSelected = false
        }
    }

    private fun onCheckableButtonsUnchecked() {
        with(binding) {
            btnYes.isSelected = false
            btnNo.isSelected = false
        }
    }
}