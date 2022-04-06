package app.snack.ui.main.screens.profile.screens.payout_request

import androidx.lifecycle.liveData
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.model.request.TransactionAdd
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ProfilePayoutRequestViewModel @Inject constructor(private val repository: Repository): BaseViewModel() {


    fun makePayoutRequest(transactionAdd: TransactionAdd) = liveData(Dispatchers.IO) {
        when(val result = repository.transactionAdd(transactionRequest = transactionAdd)) {
            is app.snack.model.result.Result.Success -> {
                showMessage("Payout successfully added!")
                emit(true)
            }
            is app.snack.model.result.Result.Failure -> {
                showError(result.error)
                emit(false)
            }
        }
    }

}
