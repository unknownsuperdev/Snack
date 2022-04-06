package app.snack.ui.main.screens.profile.screens.payout_request

import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentProfilePayoutRequestBinding
import app.snack.model.request.TransactionAdd
import app.snack.ui.main.SharedViewModel
import app.snack.ui.main.screens.dashboard.DashboardViewModel
import app.snack.utils.Screen
import app.snack.utils.extensions.onClick
import app.snack.utils.extensions.onTextChanged
import app.snack.utils.extensions.text

class ProfilePayoutRequestFragment :
    BindingFragment<FragmentProfilePayoutRequestBinding, ProfilePayoutRequestViewModel>() {

    override val viewModel by activityViewModels<ProfilePayoutRequestViewModel>()

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val payoutAmount
        get() = binding.etAmount.text?.parseToFloat() ?: 0f

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProfilePayoutRequestBinding::inflate

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { closeScreen() }


        binding.etNumber.onTextChanged {
            validateCreditCard(it)
        }

        binding.etCardholder.onTextChanged {
            validateCardHolder(it)
        }

        binding.etAmount.onTextChanged {
             validatePayoutAmount()
        }

        binding.btnRequest.onClick {
            val cardNumber = binding.etNumber.text()
            val cardHolder = binding.etCardholder.text()

            if(validateCreditCard(cardNumber) && validateCardHolder(cardHolder) && validatePayoutAmount()) {
                if(payoutAmount == 0f)
                    return@onClick
                viewModel.makePayoutRequest(TransactionAdd(cardNumber, sum = payoutAmount)).observe(viewLifecycleOwner) { isPayoutAddedSuccess ->
                    if(isPayoutAddedSuccess) {
                        showScreen(Screen.MAIN)
                    }
                }
            }
        }
    }

    override fun observeViewModels() {
        super.observeViewModels()
        sharedViewModel.profile.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.balance.text = String.format("Balance: $%.2f", it.currentBalance)
            }
        }
    }

    private fun validateCreditCard(cardNumber: String): Boolean {
        return if(cardNumber.isEmpty()) {
            binding.tilNumber.error = getString(R.string.card_number_empty)
            binding.tilNumber.isErrorEnabled = true
            false
        } else if (cardNumber.length != 19)  {
            binding.tilNumber.error = getString(R.string.not_valid_card_number)
            binding.tilNumber.isErrorEnabled = true
            false
        } else {
            binding.tilNumber.isErrorEnabled = false
            true
        }
    }

    private fun validatePayoutAmount(): Boolean {
        if(payoutAmount == 0f) {
            binding.tilAmount.error = getString(R.string.payout_is_empty)
            binding.tilAmount.isErrorEnabled = true
            return false
        } else {
            sharedViewModel.profile.value?.let {
                if(payoutAmount > it.currentBalance.dollar) {
                    binding.tilAmount.error = "Payout can't be more than balance"
                    binding.tilAmount.isErrorEnabled = true
                } else {
                    binding.tilAmount.isErrorEnabled = false
                    return true
                }
                return false
            } ?: run {
                binding.tilAmount.error = "Balance value is empty"
                binding.tilAmount.isErrorEnabled = true
                return false
            }
        }

    }

    private fun validateCardHolder(cardHolder: String): Boolean {
        return if(cardHolder.isEmpty()) {
            binding.tilCardholder.error = "Card holder name is empty"
            binding.tilCardholder.isErrorEnabled = true
            false
        } else {
            binding.tilCardholder.isErrorEnabled = false
            true
        }
    }

}

fun Editable.parseToFloat(): Float {
    if(this.isNotEmpty()){
        val result = this.trim().toString().toFloatOrNull()
        if(result != null)
            return result
        else
            return 0.0f
    }else {
        return 0.0f
    }
}