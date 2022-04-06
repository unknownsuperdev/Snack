package app.snack.ui.main.screens.settings.screens.support

import androidx.lifecycle.liveData
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.model.request.SupportMessage
import app.snack.model.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class SettingsSupportViewModel @Inject constructor(private val repository: Repository) : BaseViewModel() {

    fun supportSendEmail(message: String) = liveData(Dispatchers.IO) {
        when(val response = repository.supportSendEmail(SupportMessage(message))) {
            is Result.Success -> emit(true)
            is Result.Failure -> emit(false)
        }

    }

}
