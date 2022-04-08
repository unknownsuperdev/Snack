package app.snack.ui.main.screens.settings.screens.termsConfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.model.request.UserRequest
import app.snack.model.result.ProfileResult
import app.snack.model.result.SignInResult
import app.snack.utils.NavAction
import app.snack.data.sources.local.Preferences
import app.snack.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrivacyConfirmationViewModel
@Inject constructor(
    private val repository: Repository,
    private val preferences: Preferences
) : BaseViewModel() {

//    private val _termsAccepted = MutableLiveData<Boolean>()
//    val termsAccepted: LiveData<Boolean>
//        get() = _termsAccepted

    fun onTermsConfirmationScreenShow(showTermsConfirmationScreen: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.showPrivacyConfirmationScreen = showTermsConfirmationScreen
            navigateFromPrivacyConfirmationFragment()
//            _termsAccepted.postValue(true)
        }
    }

    suspend fun navigateFromPrivacyConfirmationFragment() {


        if (repository.isFirstLaunch) {
            repository.isFirstLaunch = false
            navigate(NavAction.Show(Screen.ONBOARDING))
        } else {
            val credentials = preferences.credentials


            if (preferences.isNativeAuthentication && credentials != null) {
                when (repository.signIn(credentials.login, credentials.password)) {
                    is SignInResult.Success -> {
                        navigate(NavAction.Show(Screen.MAIN))
                    }
                    is SignInResult.Failure -> navigate(NavAction.Show(Screen.LOGIN))
                }
            } else {

                preferences.token?.let {
                    // try to check if token is valid by fetching profile
                    when (val result = repository.fetchProfile(UserRequest.Profile())) {
                        is ProfileResult.Success -> {
                            navigate(NavAction.Show(Screen.MAIN))
                        }
                        is ProfileResult.Failure -> {
                            // token is not valid
                            navigate(NavAction.Show(Screen.LOGIN))
                        }
                    }
                } ?: run {
                    navigate(NavAction.Show(Screen.LOGIN))
                }

            }
        }
    }

}