package app.snack.base

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.snack.model.Error
import app.snack.utils.NavAction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    private val _error = Channel<String>(capacity = 1)
    val error = _error.receiveAsFlow()

    private val _message = Channel<String>(capacity = 1)
    val message = _message.receiveAsFlow()

    private val _loading = Channel<Boolean>(capacity = 1)
    val loading = _loading.receiveAsFlow()

    private val _navigation = Channel<NavAction>(capacity = Channel.CONFLATED)
    val navigation = _navigation.receiveAsFlow()

    open fun onCreate() = Unit

    override fun onCleared() {
        _loading.trySend(false)
        super.onCleared()
    }

    fun navigate(action: NavAction) {
        viewModelScope.launch {
            _navigation.send(action)
        }
    }

    fun showMessage(message: String) {
        viewModelScope.launch {
            _message.send(message)
        }
    }

    fun showError(error: Error) {
        viewModelScope.launch {
            _error.send(error.message ?: "Unknown error")
        }
    }

    fun showLoader(isVisible: Boolean) {
        viewModelScope.launch {
            _loading.trySend(isVisible)
        }
    }
}