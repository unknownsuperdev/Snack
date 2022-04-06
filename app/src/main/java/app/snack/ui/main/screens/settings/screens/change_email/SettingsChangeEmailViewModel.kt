package app.snack.ui.main.screens.settings.screens.change_email

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
import app.snack.model.result.SignInResult
import app.snack.utils.Event
import app.snack.utils.NavAction
import app.snack.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsChangeEmailViewModel @Inject constructor(private val repository: Repository, private val resources: Resources, private val preferences: Preferences) : BaseViewModel() {

    fun changeEmail(email: String) {
        viewModelScope.launch {
            when(val result = repository.editEmail(UserRequest.Email(email))) {
                is Result.Success -> {
                    showMessage(resources.getString(R.string.email_changed))
                    preferences.updateLoginCredentials(email)
                    // we need to refresh token with new credentials
                    preferences.credentials?.let {

                        when (repository.signIn(email, it.password)) {
                            is SignInResult.Success -> {
                                navigate(NavAction.Close())
                            }
                            is SignInResult.Failure -> {
                                showMessage("Error refreshing access token")
                                navigate(NavAction.Close())
                            }
                        }

                    }

                }
                is Result.Failure -> {
                    showError(result.error)
                }
            }
        }
    }

}
