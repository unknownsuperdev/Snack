package app.snack.ui.setupSnack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import app.snack.base.BaseViewModel
import app.snack.data.sources.local.Preferences
import app.snack.utils.enums.SetupSnackCheckableButtonsStateEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupSnackViewModel @Inject constructor(private val preferences: Preferences) : BaseViewModel() {

    private var buttonNoChecked: Boolean = false
    private var buttonYesChecked: Boolean = false
    var mobileDataUsageLimit: Int ? = null
    var isDataLimitSwitchButtonChecked: Boolean = false

    private val _checkableButtonsState = MutableLiveData<SetupSnackCheckableButtonsStateEnum>()
    val checkableButtonsState: LiveData<SetupSnackCheckableButtonsStateEnum>
        get() = _checkableButtonsState


    fun onButtonNoClicked() {
        buttonNoChecked = !buttonNoChecked
        buttonYesChecked = false
        onCheckableButtonSelected()
    }


    fun onButtonYesClicked() {
        buttonYesChecked = !buttonYesChecked
        buttonNoChecked = false
        onCheckableButtonSelected()
    }

    private fun onCheckableButtonSelected() {
        viewModelScope.launch {
            when {
                !buttonNoChecked && !buttonYesChecked -> {
                    _checkableButtonsState.postValue(SetupSnackCheckableButtonsStateEnum.NOTHING)
                }
                buttonYesChecked -> {
                    _checkableButtonsState.postValue(SetupSnackCheckableButtonsStateEnum.YES)
                }
                buttonNoChecked -> {
                    _checkableButtonsState.postValue(SetupSnackCheckableButtonsStateEnum.NO)
                }
            }
        }
    }

    fun setDataLimit() {
        if (isDataLimitSwitchButtonChecked) {
            preferences.mobileDataUsageLimit = mobileDataUsageLimit
        }
    }
}