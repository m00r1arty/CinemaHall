package tj.ikrom.cinemahall.ui.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tj.ikrom.cinemahall.data.database.entity.HistoryEntity

class HistoryAdapter : ListAdapter<HistoryEntity, HistoryAdapter.PaymentViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<HistoryEntity>() {
        override fun areItemsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: HistoryEntity, newItem: HistoryEntity) =
            oldItem == newItem
    }

    inner class PaymentViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val textView = TextView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            textSize = 16f
            setPadding(16, 16, 16, 16)
        }
        return PaymentViewHolder(textView)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val payment = getItem(position)
        val seatsText = payment.seats.joinToString("\n") { it.objectDescription.toString() }
        holder.textView.apply {
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
            text = "Сумма: ${payment.totalPrice} | Места:\n$seatsText"
        }
    }
}