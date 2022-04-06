package app.snack.ui.main.screens.profile.screens.payout_history

import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import app.snack.R
import app.snack.base.BindingFragment
import app.snack.databinding.FragmentProfilePayoutHistoryBinding
import app.snack.databinding.FragmentProfilePayoutRequestBinding
import app.snack.ui.main.screens.profile.screens.payout_history.adapter.PayoutHistoryAdapter
import app.snack.ui.main.screens.profile.screens.payout_request.ProfilePayoutRequestViewModel
import app.snack.utils.extensions.onClick

class ProfilePayoutHistoryFragment :
    BindingFragment<FragmentProfilePayoutHistoryBinding, ProfilePayoutHistoryViewModel>() {

    override val viewModel by activityViewModels<ProfilePayoutHistoryViewModel>()

    override val bindingInflater: (LayoutInflater) -> ViewBinding
        get() = FragmentProfilePayoutHistoryBinding::inflate

    private var payoutHistoryAdapter = PayoutHistoryAdapter(mutableListOf())

    override fun setupUI() {
        binding.toolbar.setNavigationOnClickListener { closeScreen() }
        binding.list.adapter = payoutHistoryAdapter
        payoutHistoryAdapter.setEmptyView(R.layout.payout_history_empty)
    }

    override fun observeViewModels() {
        super.observeViewModels()
        viewModel.transactionsHistory.observe(viewLifecycleOwner) { transactions ->
             payoutHistoryAdapter.setNewInstance(transactions)
        }

        viewModel.fetchTransactionsHistory()
    }
}
