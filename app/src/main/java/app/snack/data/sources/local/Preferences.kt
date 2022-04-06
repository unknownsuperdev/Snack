package app.snack.data.sources.local

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import app.snack.data.sources.local.Preferences.Constants.PREF_AUTH_CREDENTIALS
import app.snack.data.sources.local.Preferences.Constants.PREF_BONUS_GIVEN
import app.snack.data.sources.local.Preferences.Constants.PREF_FIRST_LAUNCH
import app.snack.data.sources.local.Preferences.Constants.PREF_NATIVE_AUTH
import app.snack.data.sources.local.Preferences.Constants.PREF_TOKEN
import app.snack.model.SnackCredentials
import app.snack.model.request.UserRequest
import com.google.gson.Gson
import okhttp3.Credentials
import java.lang.Exception
import javax.inject.Inject

class Preferences @Inject constructor(
    private val preferences: SharedPreferences
) {

    object Constants {
        private const val PREFERENCES_NAME = "snack-preferences"
        const val PREF_TOKEN = "$PREFERENCES_NAME-PREF_TOKEN"
        const val PREF_FIRST_LAUNCH = "$PREFERENCES_NAME-PREF_FIRST_LAUNCH"
        const val PREF_NATIVE_AUTH = "$PREFERENCES_NAME-PREF_NATIVE_AUTH"
        const val PREF_AUTH_CREDENTIALS = "$PREFERENCES_NAME-PREF_AUTH_CREDENTIALS"
        const val PREF_BONUS_GIVEN = "$PREFERENCES_NAME-PREF_BONUS_GIVEN"
        const val PREF_DEVICE_REGISTERED = "$PREFERENCES_NAME-PREF_DEVICE_REGISTERED"
        const val PREF_ALLOW_MOBILE_DATA = "$PREFERENCES_NAME-PREF_ALLOW_MOBILE_DATA"
        const val PREF_STOP_SHARING_ON_BATTERY_LOW = "$PREFERENCES_NAME-PREF_STOP_SHARING_ON_BATTERY_LOW"
        const val PREF_TRAFFIC_LIMIT = "$PREFERENCES_NAME-PREF_TRAFFIC_LIMIT"
        const val PREF_LAST_SHARE_BUTTON_STATE = "$PREFERENCES_NAME-PREF_LAST_SHARE_BUTTON_STATE"
        const val PREF_CONFIRMATION_SEND = "$PREFERENCES_NAME-PREF_CONFIRMATION_SEND"
    }

    var isMobileAllowed: Boolean
        set(value) {
            preferences.edit { putBoolean(Constants.PREF_ALLOW_MOBILE_DATA, value) }
        }
        get() = preferences.getBoolean(Constants.PREF_ALLOW_MOBILE_DATA, true)


    var isConfirmationSend: Boolean
        set(value) {
            preferences.edit { putBoolean(Constants.PREF_CONFIRMATION_SEND, value) }
        }
        get() = preferences.getBoolean(Constants.PREF_CONFIRMATION_SEND, false)

    var lastShareButtonState: Boolean
        set(value) {
            preferences.edit { putBoolean(Constants.PREF_LAST_SHARE_BUTTON_STATE, value) }
        }
        get() = preferences.getBoolean(Constants.PREF_LAST_SHARE_BUTTON_STATE, false)


    var isStopSharingOnLowBatteryAllowed: Boolean
        set(value) {
            preferences.edit { putBoolean(Constants.PREF_STOP_SHARING_ON_BATTERY_LOW, value) }
        }
        get() = preferences.getBoolean(Constants.PREF_STOP_SHARING_ON_BATTERY_LOW, true)

    fun getStopSharingOnLowBatter(): Boolean {
        return preferences.getBoolean(Constants.PREF_STOP_SHARING_ON_BATTERY_LOW, true)
    }

    /*
    *
    *  0 - no limits
    *  1 - 100 MB
    *  2 - 500 MB
    *  3 - 1 GB
    *
    * */
    var trafficLimit: Int
        set(value) {
            preferences.edit { putInt(Constants.PREF_TRAFFIC_LIMIT, value) }
        }
        get() = preferences.getInt(Constants.PREF_TRAFFIC_LIMIT, 0)

    var token: String?
        set(value) {
            preferences.edit { putString(PREF_TOKEN, value) }
        }
        get() = preferences.getString(PREF_TOKEN, null)

    var credentials: SnackCredentials?
        set(value) = try {
            val json = Gson().toJson(value)
            preferences.edit { putString(PREF_AUTH_CREDENTIALS, json) }
        } catch (e: Exception) {
            preferences.edit { putString(PREF_AUTH_CREDENTIALS, null) }
            e.printStackTrace()
        }
        get() = preferences.getString(PREF_AUTH_CREDENTIALS, null)?.let {
            return try {
                Gson().fromJson(it, SnackCredentials::class.java)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        } ?: run {
            return null
        }

    fun updateLoginCredentials(newLogin: String) {
        credentials?.let {
            val newCredentials = SnackCredentials(newLogin, it.password)
            credentials = newCredentials
        }

    }

    fun updatePasswordCredentials(newPassword: String) {
        credentials?.let {
            val newCredentials = SnackCredentials(it.login, newPassword)
            credentials = newCredentials
        }

    }

    fun clearCredentials() {
        preferences.edit { putString(PREF_AUTH_CREDENTIALS, null) }
    }

    fun clearToken() {
        preferences.edit { putString(PREF_TOKEN, null) }
    }

    var isFirstLaunch: Boolean
        set(value) {
            preferences.edit { putBoolean(PREF_FIRST_LAUNCH, value) }
        }
        get() = preferences.getBoolean(PREF_FIRST_LAUNCH, true)


    var isBonusGiven: Boolean
        set(value) {
            preferences.edit { putBoolean(PREF_BONUS_GIVEN, value) }
        }
        get() = preferences.getBoolean(PREF_BONUS_GIVEN, false)

    var isNativeAuthentication: Boolean
        set(value) {
            preferences.edit { putBoolean(PREF_NATIVE_AUTH, value) }
        }
        get() = preferences.getBoolean(PREF_NATIVE_AUTH, true)

    var isDeviceRegistered: Boolean
        set(value) {
            preferences.edit { putBoolean(Constants.PREF_DEVICE_REGISTERED, value) }
        }
        get() = preferences.getBoolean(Constants.PREF_DEVICE_REGISTERED, false)

}