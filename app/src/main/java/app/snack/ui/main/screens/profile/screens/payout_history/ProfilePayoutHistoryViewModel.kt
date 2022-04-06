package app.snack.ui.main.screens.profile.screens.payout_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.model.response.Transaction
import app.snack.model.response.TransactionResponse
import app.snack.model.result.TransactionsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfilePayoutHistoryViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    private val _transactionsHistory = MutableLiveData<MutableList<Transaction>>()
    val transactionsHistory: LiveData<MutableList<Transaction>>
        get() = _transactionsHistory

    fun fetchTransactionsHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.transactionsHistory()) {
                is TransactionsResult.Success -> _transactionsHistory.postValue(result.transactions)
                is TransactionsResult.Failure -> showError(result.error)
            }
        }
    }

}
