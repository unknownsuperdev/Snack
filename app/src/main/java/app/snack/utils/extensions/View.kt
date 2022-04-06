package app.snack.utils.extensions

import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.*
import android.text.Spanned.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import app.snack.R
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.chad.library.adapter.base.module.BaseDraggableModule
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

// =============================================================================================
// View
// =============================================================================================

fun View.onClick(
    context: CoroutineContext = Dispatchers.Main,
    handler: suspend CoroutineScope.(v: View?) -> Unit
) {
    setOnClickListener { v ->
        v.postDelayed({
            GlobalScope.launch(context, CoroutineStart.DEFAULT) {
                handler(v)
            }
        }, 100)
    }
}

fun View.enabled(isEnabled: Boolean) {
    this.isEnabled = isEnabled
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show(isEnabled: Boolean = true) {
    this.visibility = View.VISIBLE
    if (isEnabled) enable() else disable()
}

fun View.showed(isShowed: Boolean = true) {
    visibility = if (isShowed) View.VISIBLE else View.GONE
}

fun View.visibled(isVisible: Boolean = true) {
    visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun View.color(color: Int) = resources.getColor(color)

fun View.stopBlink() {
    clearAnimation()
}

fun TextView.setClickableLink(
    fullText: String,
    clickableText: String,
    isUnderline: Boolean = true,
    isBold: Boolean = true,
    isPrimaryColor: Boolean = true,
    listener: () -> Unit
) {
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            listener.invoke()
            widget.cancelPendingInputEvents()
        }
    }

    val linkText = SpannableString(fullText)

    val start = linkText.indexOf(clickableText)
    val end = linkText.length

    linkText.setSpan(clickableSpan, start, end, SPAN_EXCLUSIVE_EXCLUSIVE)

    if (isBold) linkText.setSpan(StyleSpan(Typeface.BOLD), start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    if (isUnderline) linkText.setSpan(UnderlineSpan(), start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    if (isPrimaryColor) {
        val colorPrimary = resources.getColor(R.color.primary)
        linkText.setSpan(ForegroundColorSpan(colorPrimary), start, end, SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    text = linkText
    movementMethod = LinkMovementMethod.getInstance()
}

// =============================================================================================
// TextView
// =============================================================================================

fun TextView.text() = text.toString()

fun TextView.removeDrawable() = setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0)

fun TextView.setDrawableTop(drawable: Int) =
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, drawable, 0, 0)

fun TextView.setDrawableEnd(drawable: Int) =
    setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawable, 0)

fun TextView.setDrawableStart(drawable: Int) =
    setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, 0, 0, 0)

fun TextView.setDrawableEnd(drawable: Drawable) =
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)

fun TextView.setDrawableTint(color: Int) =
    DrawableCompat.setTint(
        DrawableCompat.wrap(compoundDrawables[1]),
        ContextCompat.getColor(context, color)
    )

fun TextView.setBoldText(text: String, boldText: String) {
    val bss = StyleSpan(Typeface.BOLD)
    val ssb = SpannableStringBuilder(text)
    ssb.setSpan(
        bss,
        text.indexOf(boldText),
        text.indexOf(boldText) + boldText.length,
        Spannable.SPAN_INCLUSIVE_INCLUSIVE
    )
    setText(ssb)
}

// =============================================================================================
// EditText
// =============================================================================================

fun EditText.clear() = setText("")

fun EditText.onTextChanged(fn: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {}

        override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            fn.invoke(text.toString())
        }
    })
}

fun EditText.addOnImeActionClick(listener: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) listener.invoke()
        false
    }
}

fun EditText.addOnReturnActionClick(listener: () -> Unit) {
    setOnEditorActionListener { textView, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            listener.invoke()
            return@setOnEditorActionListener true
        }

        return@setOnEditorActionListener false
    }
}

fun EditText.text() = text.toString()

// =============================================================================================
// ProgressBar
// =============================================================================================

//fun ProgressBar.setColor(color: Int) {
//    indeterminateDrawable.setColor(context, color)
//}


// =============================================================================================
// Tabs
// =============================================================================================

fun TabLayout.onTabSelected(listener: (Int) -> Unit) {
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {

        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {

        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            listener.invoke(tab!!.position)
        }

    })
}

// ===============================================================================================================================
// DatePicker
// ===============================================================================================================================

//fun DatePicker.dateAsString(pattern: String = "yyyy-MM-dd"): String {
//    val calendar = Calendar.getInstance()
//    calendar.set(year, month, dayOfMonth)
//    return Date(calendar.timeInMillis).asDateString(pattern)
//}

// =============================================================================================
// ImageView
// =============================================================================================

fun ImageView.setTint(color: Int) {
    setColorFilter(color, PorterDuff.Mode.SRC_IN)
}

fun ImageView.setTintResource(colorRes: Int) {
    setColorFilter(ContextCompat.getColor(context, colorRes), PorterDuff.Mode.SRC_IN)
}

// =============================================================================================
// Switch
// =============================================================================================

fun SwitchCompat.onCheck(listener: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked -> listener.invoke(isChecked) }
}

// =============================================================================================
// ViewPager
// =============================================================================================

fun ViewPager.onPageSelect(listener: (Int) -> Unit) {
    addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            listener.invoke(position)
        }

    })
}

// =============================================================================================
// RecyclerView
// =============================================================================================

fun BaseDraggableModule.onDragEnd(listener: (Int) -> Unit) {
    isDragEnabled = true
    setOnItemDragListener(object : OnItemDragListener {
        override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {

        }

        override fun onItemDragMoving(
            source: RecyclerView.ViewHolder?,
            from: Int,
            target: RecyclerView.ViewHolder?,
            to: Int
        ) {

        }

        override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
            listener.invoke(pos)
        }

    })
}

fun ViewPager2.onPageSelected(listener: (Int) -> Unit) {
    registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            listener.invoke(position)
        }
    })
}

fun ViewPager2.nextPage() {
    if (currentItem + 1 < adapter?.itemCount ?: 0) {
        setCurrentItem(currentItem + 1, true)
    }
}
