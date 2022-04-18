package app.snack.ui.main.screens.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.data.sources.local.Preferences
import app.snack.model.result.ConfirmationResult
import app.snack.model.result.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: Repository,
    private val preferences: Preferences,
//    @Named("service_intent") private val serviceIntent: Intent
) : BaseViewModel() {

    var lastShareButtonState: Boolean = preferences.lastShareButtonState
        set(value) {
            field = value
            preferences.lastShareButtonState = value
        }
        get() = preferences.lastShareButtonState

//    val isBonusGiven
//        get() = preferences.isBonusGiven
//
//    fun setBonusGiven() {
//        preferences.isBonusGiven = true
//    }

    private val _confirmationEmailSent = MutableLiveData<Boolean>()
    val confirmationEmailSent: LiveData<Boolean>
        get() = _confirmationEmailSent

    init {
        if(preferences.isConfirmationSend) {
            _confirmationEmailSent.value = true
        }
    }

    fun reportTrafficSharingEnabled() {
        viewModelScope.launch {
            when (repository.enableTrafficSharing()) {
                is Result.Success -> {
                    Log.d("AAA", "sharing enabled sent ok")
                }
                is Result.Failure -> {
                    Log.d("AAA", "sharing enabled sent error")
                }
            }
        }
    }

    fun reportTrafficSharingDisabled() {
        viewModelScope.launch {
            when (repository.disableTrafficSharing()) {
                is Result.Success -> {
                    Log.d("AAA", "sharing disabled sent ok")
                }
                is Result.Failure -> {
                    Log.d("AAA", "sharing disabled sent error")
                }
            }
        }
    }


    fun sendConfirmation() {
        viewModelScope.launch {
            when (val response = repository.sendConfirmation()) {
                is ConfirmationResult.Success -> {
                    showMessage("Confirmation email send")
                    preferences.isConfirmationSend = true
                    _confirmationEmailSent.value = true
                }
                is ConfirmationResult.Failure -> {
                    showError(response.error)
                }
            }
        }
    }


    fun applyWelcomeBonus() = liveData(Dispatchers.IO) {

        when(val result = repository.applyWelcomeBonus()) {
            is Result.Success -> {
                showMessage("Bonus balance added")
                emit(true)
            }
            is Result.Failure -> {
                showError(result.error)
                emit(false)
            }
        }
    }

    fun checkNegativeTransactionsWithDelay() = liveData(Dispatchers.IO) {
        delay(3000)
        when(val result = repository.isWelcomeBonusActivated()) {
            is Result.Success -> emit(true)
            is Result.Failure -> emit(false)
        }
    }

}
