package app.snack.utils

import android.os.Bundle

sealed class NavAction {

    data class Show(val screen: Screen, val data: Bundle? = null) : NavAction()
    data class Add(val screen: Screen, val data: Bundle? = null) : NavAction()
    data class Close(val data: Any? = null) : NavAction()

    val contentIfNotHandled: NavAction?
        get() = if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            this
        }

    private var hasBeenHandled = false
        private set
}