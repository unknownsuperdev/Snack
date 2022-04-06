package app.snack.ui.main

import android.util.Log
import androidx.lifecycle.*
import app.snack.BuildConfig
import app.snack.base.BaseViewModel
import app.snack.data.repository.Repository
import app.snack.data.sources.local.Preferences
import app.snack.model.request.UserRequest
import app.snack.model.response.Profile
import app.snack.model.result.ProfileResult
import app.snack.model.result.Result
import com.amplitude.api.AmplitudeClient
import org.snack.prx.SwipeSdk
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository, private val preferences: Preferences, private val proxySdk: SwipeSdk, private val amplitudeClient: AmplitudeClient) : BaseViewModel() {

    private val _profile = MutableLiveData<Profile?>()

    val profile: LiveData<Profile?>
        get() = _profile

    private val _logoutTriggered = MutableLiveData<Boolean>()
    val logoutTriggered: LiveData<Boolean>
        get() = _logoutTriggered

    private val _needTopUpdate = MutableLiveData<Boolean>()
    val needToUpdate: LiveData<Boolean>
        get() = _needTopUpdate

    private var isAmplitudeUserIdSet: Boolean = false

    init {
        _logoutTriggered.value = false

        if(preferences.isDeviceRegistered.not()) {
            viewModelScope.launch(Dispatchers.IO) {
                when (val result = repository.registerDevice(proxySdk.deviceUid)) {
                    is Result.Success -> {
                        preferences.isDeviceRegistered = true
                    }
                    is Result.Failure -> {
                        if(result.error.message == "Device already registred") {
                            preferences.isDeviceRegistered = true
                        }
                        showError(result.error)
                    }
                }

            }
        } else {
            Log.d("AAA", ">>> registered already")
        }
    }

    fun fetchProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            when(val result = repository.fetchProfile(UserRequest.Profile())) {
                is ProfileResult.Success -> {

                    try {

                        val versionFromResponse =
                            result.profileResponse.version.replace(".", "").toInt()
                        val versionFromApplication =
                            "${BuildConfig.VERSION_NAME}.${BuildConfig.VERSION_CODE}".replace(
                                ".",
                                ""
                            ).toInt()

                        Log.d("AAA", ">>versions ${versionFromResponse} ${versionFromApplication}")
                        if(versionFromResponse > versionFromApplication) {
                            _needTopUpdate.value = true
                        }

                        if(!isAmplitudeUserIdSet) {
                            amplitudeSetUserId(result.profileResponse.data.email)
                        }

                    } catch (e: Exception) {
                        Log.d("AAA", "can not parse version")
                    }

                    _profile.postValue(result.profileResponse.data)
                }
                is ProfileResult.Failure -> {
                    showError(result.error)
                }
            }
        }
    }

    fun logout() {
        _logoutTriggered.value = true

        _profile.value = null
        preferences.lastShareButtonState = false
        preferences.isBonusGiven = false
        preferences.isDeviceRegistered = false
        preferences.clearCredentials()
        preferences.clearToken()
    }

    private fun amplitudeSetUserId(email: String) {
        Log.d("AAA", "Set user ID $email")
        amplitudeClient.userId = email
        isAmplitudeUserIdSet = true
    }

}