package tj.ikrom.cinemahall.ui.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tj.ikrom.cinemahall.data.database.entity.PaymentEntity

class PaymentAdapter : ListAdapter<PaymentEntity, PaymentAdapter.PaymentViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<PaymentEntity>() {
        override fun areItemsTheSame(oldItem: PaymentEntity, newItem: PaymentEntity) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: PaymentEntity, newItem: PaymentEntity) =
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
        holder.textView.text = "Сумма: ${payment.totalPrice} | Места:\n$seatsText"
    }
}