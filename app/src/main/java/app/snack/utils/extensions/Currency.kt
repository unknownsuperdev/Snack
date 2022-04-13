package app.snack.utils.extensions

import java.text.DecimalFormat
import java.util.*

fun Float.csToUsd(): Double {
    if (this == 0F) {
        return 0.00
    }
    return DecimalFormat("#,##0.#").format(this / 1000).toDouble()
}

fun Float.toUsd(decimals: Int = 2) = "$%.${decimals}f".format(Locale.getDefault(), (this.toDouble()/100F))