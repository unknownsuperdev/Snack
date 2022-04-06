package app.snack.ui.main.screens.profile

import androidx.lifecycle.viewModelScope
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.data.sources.local.Preferences
import app.snack.model.result.ConfirmationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val repository: Repository, private val preferences: Preferences) : BaseViewModel() {

    fun sendConfirmation() {
        viewModelScope.launch {
            when (val response = repository.sendConfirmation()) {
                is ConfirmationResult.Success -> {
                    showMessage("Confirmation email send")
                    preferences.isConfirmationSend = true
                }
                is ConfirmationResult.Failure -> {
                    showError(response.error)
                }
            }
        }
    }

}
