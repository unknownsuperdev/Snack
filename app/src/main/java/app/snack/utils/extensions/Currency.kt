package app.snack.utils.extensions

import java.text.DecimalFormat
import java.util.*

fun Float.toUsd(decimals: Int = 2) = "%.${decimals}f".format(Locale.getDefault(), (this.toDouble()/1000F)).toFloat()