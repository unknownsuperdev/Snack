package app.snack.ui.splash

import androidx.lifecycle.viewModelScope
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.data.sources.local.Preferences
import app.snack.model.request.UserRequest
import app.snack.model.result.ProfileResult
import app.snack.model.result.SignInResult
import app.snack.utils.NavAction
import app.snack.utils.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: Repository,
    private val preferences: Preferences
) : BaseViewModel() {

    override fun onCreate() {
        super.onCreate()
        viewModelScope.launch {
            delay(1000)

            if (repository.isFirstLaunch) {
                repository.isFirstLaunch = false
                navigate(NavAction.Show(Screen.ONBOARDING))
            } else {
                val credentials = preferences.credentials


                if(preferences.isNativeAuthentication && credentials != null) {
                    when (repository.signIn(credentials.login, credentials.password)) {
                        is SignInResult.Success -> {
                            navigate(NavAction.Show(Screen.MAIN))
                        }
                        is SignInResult.Failure -> navigate(NavAction.Show(Screen.LOGIN))
                    }
                } else {

                    preferences.token?.let {
                        // try to check if token is valid by fetching profile
                        when(val result = repository.fetchProfile(UserRequest.Profile())) {
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
}
