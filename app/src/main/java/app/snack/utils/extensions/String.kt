package app.snack.utils.extensions

import android.util.Patterns
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.util.*
import java.util.regex.Pattern

val String.isValidEmail: Boolean
    get() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

val String.contains8Characters: Boolean
    get() = length >= 8

val String.containsDigits: Boolean
    get() = matches(Regex(".*\\d.*"))

val String.containsSpecialSymbols: Boolean
    get() = Pattern.compile("[!@#\$%&]").matcher(this).find()

fun String.convertUnixTimestampToMediumDate(): String = DateFormat
    .getDateInstance(DateFormat.MEDIUM)
    .format(Date(this.toLong()))