package app.snack.ui.main.screens.settings.screens.alerts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.snack.R
import app.snack.utils.extensions.onClick
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsDisableMobileDataAlert : BottomSheetDialogFragment() {

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    var onOkListener: (() -> Unit)? = null
    var onCancelListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.alert_disable_mobile_data, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.btnOk).onClick {
            onOkListener?.invoke()
            dismiss()
        }
        view.findViewById<View>(R.id.btnCancel).onClick {
            onCancelListener?.invoke()
            dismiss()
        }
    }
}