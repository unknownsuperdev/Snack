package app.snack.ui.main.screens.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import app.snack.base.BaseViewModel
import app.snack.data.sources.local.Preferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(val preferences: Preferences) : BaseViewModel() {

//    private val _allowMobileDataUsage = MutableLiveData<Boolean>()
//    private val _stopOnBatteryLow = MutableLiveData<Boolean>()
//
//    val allowMobileDataUsage: LiveData<Boolean>
//        get() = _allowMobileDataUsage
//
//    val stopOnBatteryLow: LiveData<Boolean>
//        get() = _stopOnBatteryLow
//
//    init {
//
//    }

    fun isNativeAuthentication() = liveData {
        emit(preferences.isNativeAuthentication)
    }

    fun setAllowMobileData(newValue: Boolean) {
        preferences.isMobileAllowed = newValue
    }

    fun setStopSharingOnLowBattery(newValue: Boolean) {
        preferences.isStopSharingOnLowBatteryAllowed = newValue
    }

    fun fetchAllowOnMobileTraffic() = liveData(Dispatchers.IO) {
        emit(preferences.isMobileAllowed)
    }

    fun fetchStopValueOnLowBattery() = liveData(Dispatchers.IO) {
        emit(preferences.isStopSharingOnLowBatteryAllowed)
    }

}
