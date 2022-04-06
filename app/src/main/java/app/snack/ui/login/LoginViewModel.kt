package app.snack.ui.login

import android.app.Activity
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.data.sources.local.Preferences
import app.snack.databinding.FragmentSettingsPrivacyPolicyBinding
import app.snack.model.Error
import app.snack.model.SnackCredentials
import app.snack.model.request.UserRequest
import app.snack.model.result.GoogleCredentialResult
import app.snack.model.result.Result
import app.snack.model.result.SignInResult
import app.snack.model.result.SignUpResult
import app.snack.utils.NavAction
import app.snack.utils.Screen
import com.amplitude.api.AmplitudeClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository,
    private val amplitudeClient: AmplitudeClient
) : BaseViewModel() {


    private val _passwordError = MutableLiveData<Boolean>()
    val passwordError: LiveData<Boolean> get() = _passwordError

    private var email: String = ""
    private var password: String = ""

    fun amplitudeSignUpViewed() {
        amplitudeClient.logEvent("signup_form_viewed")
    }

    private fun amplitudeSignUpButtonClicked() {
        amplitudeClient.logEvent("signup_button_clicked")
    }

    fun checkEmail(email: String) = liveData(Dispatchers.IO) {
        when(val result = repository.checkEmail(email = UserRequest.Email(email))) {
            is Result.Success -> {
                emit(true)
            }
            is Result.Failure -> {
                //showError(result.error)
                emit(false)
            }
        }
    }

    fun handleGoogleCredentialsResult(activityResult: ActivityResult?) {


        val googleCredentialResult = if (activityResult?.resultCode == Activity.RESULT_OK) {
            GoogleSignIn.getSignedInAccountFromIntent(activityResult.data).result?.idToken?.let { token ->
                GoogleCredentialResult.Success(token)
            } ?: {
                GoogleCredentialResult.Failure(Error())
            }
        } else {
                //Log.d("AAA", ">>>>>>>>>>>>> FAILURE ${activityResult?.resultCode}")
//                val error = Error(message = "Failure, error code ${activityResult?.resultCode}", code = activityResult?.resultCode ?: -1000)
                val error = Error(message = "Registration cancelled (${activityResult?.resultCode})", code = activityResult?.resultCode ?: -1000)
                GoogleCredentialResult.Failure(error)
        }

        when (googleCredentialResult) {
            is GoogleCredentialResult.Success -> signInByGoogle(googleCredentialResult.token)
            is GoogleCredentialResult.Failure -> showError(googleCredentialResult.error)
        }
    }

    fun enterEmail(email: String) {
        this.email = email
    }

    fun enterPassword(password: String) {
        this.password = password
    }

    fun checkSignUpPassword(password: String) {
        _passwordError.postValue(false)

        if (this.password != password) {
            _passwordError.postValue(true)
        } else {
            signUp()
        }
    }

    private fun signInByGoogle(token: String) {

        amplitudeSignUpButtonClicked()

        viewModelScope.launch {
            showLoader(true)

            when (val result = repository.signInGoogle(token)) {
                is SignInResult.Success -> {
                    navigate(NavAction.Show(Screen.MAIN))
                }
                is SignInResult.Failure -> showError(result.error)
            }

            showLoader(false)
        }
    }

    private fun signUp() {
        amplitudeSignUpButtonClicked()

        viewModelScope.launch {
            showLoader(true)

            when (val result = repository.signUp(email, password)) {
                is SignUpResult.Success -> {
                    navigate(NavAction.Show(Screen.MAIN))
                }
                is SignUpResult.Failure -> showError(result.error)
            }

            showLoader(false)
        }
    }

    fun signIn() {
        viewModelScope.launch {
            showLoader(true)

            when (val result = repository.signIn(email, password)) {
                is SignInResult.Success -> {
                    navigate(NavAction.Show(Screen.MAIN))
                }
                is SignInResult.Failure -> showError(result.error)
            }

            showLoader(false)
        }
    }

    fun restorePassword() {
        viewModelScope.launch {
            showLoader(true)

            when (val result = repository.recoverPassword(email)) {
                is Result.Success -> {
                    navigate(NavAction.Close())
                    navigate(NavAction.Add(Screen.SIGN_IN_RESTORE_PASSWORD_DONE))
                }
                is Result.Failure -> showError(result.error)
            }

            showLoader(false)
        }
    }

}
