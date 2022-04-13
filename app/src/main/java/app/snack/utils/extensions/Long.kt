package app.snack.utils.extensions

import java.text.DecimalFormat
import java.util.*

fun Long.readableFormat(unitRequire: Boolean = true): String {
    if (this <= 0)
        return "0 B"

    val units = arrayOf("B", "kB", "MB", "GB", "TB")

    val digitGroups = (Math.log10(this.toDouble()) / Math.log10(1024.0)).toInt()

    return DecimalFormat("#,##0.#").format(this / Math.pow(1024.0, digitGroups.toDouble()))
        .toString() + " " + if (unitRequire) (units[digitGroups]) else ""
}

fun Long.toMB(decimals: Int = 2) = "%.${decimals}f MB".format(Locale.getDefault(), (this.toDouble()/1024F/1024F))