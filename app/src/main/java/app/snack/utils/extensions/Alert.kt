package app.snack.utils.extensions

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import androidx.fragment.app.Fragment
import app.snack.R
import app.snack.ui.main.screens.settings.screens.alerts.LogoutAlert
import app.snack.ui.main.screens.settings.screens.alerts.SettingsDisableMobileDataAlert
import app.snack.ui.main.screens.settings.screens.alerts.SettingsMessageSentAlert
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView

fun Context.loader(): Dialog = Dialog(this, android.R.style.Theme_Dialog).apply {
    requestWindowFeature(Window.FEATURE_NO_TITLE)
    setContentView(R.layout.dialog_loader)
    window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    setCanceledOnTouchOutside(false)
}

fun Context.showDialogForgotPasswordDone(): MaterialDialog {
    val dialog = MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT))
        .customView(R.layout.alert_sign_in_restore_password_done)

    dialog.getCustomView().let { view ->
        view.findViewById<View>(R.id.btnOpenMail).setOnClickListener { dialog.cancel() }
    }
    dialog.show { cornerRadius(32f) }
    return dialog
}

fun Context.showAlertSignUpBonus(onOkListener: (() -> Unit), onCancelListener: (() -> Unit)): MaterialDialog {
    val dialog = MaterialDialog(this)
        .customView(R.layout.alert_sign_up_bonus)

    dialog.getCustomView().let { view ->
        view.findViewById<View>(R.id.btnOk).setOnClickListener {
            onOkListener.invoke()
            dialog.cancel()
        }
        view.findViewById<View>(R.id.btnCancel).setOnClickListener {
            onCancelListener.invoke()
            dialog.cancel()
        }
    }
    dialog.show { cornerRadius(32f) }
    return dialog
}

fun Context.showEmailConfirmation(onOkListener: (() -> Unit), onCancelListener: (() -> Unit)? = null): MaterialDialog {
    val dialog = MaterialDialog(this)
        .customView(R.layout.alert_confirm_email)

    dialog.getCustomView().let { view ->
        view.findViewById<View>(R.id.btnOk).setOnClickListener {
            onOkListener.invoke()
            dialog.cancel()
        }
        view.findViewById<View>(R.id.btnCancel).setOnClickListener {
            onCancelListener?.invoke()
            dialog.cancel()
        }
    }
    dialog.show { cornerRadius(32f) }
    return dialog
}

fun Context.showNewVersion(onOkListener: () -> Unit): MaterialDialog {
    val dialog = MaterialDialog(this)
        .customView(R.layout.alert_new_version)

    dialog.cancelOnTouchOutside(false)

    dialog.getCustomView().let { view ->
        view.findViewById<View>(R.id.btnOk).setOnClickListener {
            onOkListener.invoke()
            dialog.cancel()
        }
    }
    dialog.show { cornerRadius(32f) }
    return dialog

}

fun Fragment.showAlertDisableMobileData(okListener: () -> Unit, cancelListener: () -> Unit) {
    val alert = SettingsDisableMobileDataAlert()
    alert.onOkListener = okListener
    alert.onCancelListener = cancelListener
    alert.show(childFragmentManager, "Alert")
}

fun Fragment.showAlertLogout(okListener: () -> Unit) {
    val alert = LogoutAlert()
    alert.onOnClick = okListener
    alert.show(childFragmentManager, "Alert")

}

fun Fragment.showAlertMessageSent(okListener: () -> Unit, cancelListener: () -> Unit) {
    SettingsMessageSentAlert(okListener, cancelListener).show(childFragmentManager, "Alert")
}
