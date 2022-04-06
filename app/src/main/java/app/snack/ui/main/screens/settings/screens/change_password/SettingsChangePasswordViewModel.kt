package app.snack.ui.main.screens.settings.screens.change_password

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.snack.R
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.data.sources.local.Preferences
import app.snack.model.request.UserRequest
import app.snack.model.result.Result
import app.snack.utils.Event
import app.snack.utils.NavAction
import app.snack.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsChangePasswordViewModel @Inject constructor(private val repository: Repository, private val resources: Resources, private val preferences: Preferences) : BaseViewModel() {


    val currentUserPassword: String?
        get() {
            val credentials = preferences.credentials
            return credentials?.password
        }

    fun changePassword(password: String) {
        viewModelScope.launch {
            when(val result = repository.editPassword(UserRequest.Password(password))) {
                is Result.Success -> {
                    showMessage(resources.getString(R.string.password_changed))
                    preferences.updatePasswordCredentials(password)
                    navigate(NavAction.Close())
                }
                is Result.Failure -> showError(result.error)
            }
        }
    }

}
