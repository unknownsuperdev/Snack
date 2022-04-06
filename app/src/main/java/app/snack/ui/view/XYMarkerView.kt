package app.snack.ui.view

import android.content.Context
import android.widget.TextView
import app.snack.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class XYMarkerView(context: Context?) : MarkerView(context, R.layout.view_diagram_marker) {

    override fun refreshContent(e: Entry, highlight: Highlight) {
        findViewById<TextView>(R.id.tvEarned).text = "$2.4"
        findViewById<TextView>(R.id.tvShared).text = "240 Mb"
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }
}