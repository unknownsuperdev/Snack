package app.snack.ui.main.screens.profile.screens.payout_history.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import app.snack.R
import app.snack.model.response.Transaction
import app.snack.model.response.TransactionResponse
import app.snack.utils.extensions.convertUnixTimestampToMediumDate
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.textview.MaterialTextView

class PayoutHistoryAdapter(data: MutableList<Transaction>) : BaseQuickAdapter<Transaction, BaseViewHolder>(R.layout.item_payout_history) {

    override fun convert(holder: BaseViewHolder, item: Transaction) {

        with(holder.getView<MaterialTextView>(R.id.status)) {

            var bgTintColor: Int? = null
            var textColor: Int? = null

            when(item.status) {
                "Sent" -> {
                    bgTintColor = R.color.green_12opacity
                    textColor = R.color.transaction_text_green
                }
                "Pending" -> {
                    bgTintColor = R.color.yellow_12opacity
                    textColor = R.color.transaction_text_yellow
                }
                else -> {
                    bgTintColor = R.color.red_12opacity
                    textColor = R.color.transaction_text_pink
                }
            }

            setTextColor(ContextCompat.getColor(context, textColor))
            backgroundTintList =  ContextCompat.getColorStateList(context, bgTintColor)

        }

        holder
            .setText(R.id.card_number, item.cardNumber)
            .setText(R.id.datetime, item.datetime.convertUnixTimestampToMediumDate())
            .setText(R.id.status, item.status)
            .setText(R.id.sum, String.format("$%.2f", item.sum))

    }

}
