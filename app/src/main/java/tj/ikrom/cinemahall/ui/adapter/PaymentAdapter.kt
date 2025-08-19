package tj.ikrom.cinemahall.ui.adapter

import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tj.ikrom.cinemahall.data.network.model.Seat

class PaymentAdapter : ListAdapter<Seat, PaymentAdapter.PaymentViewHolder>(DiffCallback) {

    object DiffCallback : DiffUtil.ItemCallback<Seat>() {
        override fun areItemsTheSame(oldItem: Seat, newItem: Seat) =
            oldItem.place == newItem.place

        override fun areContentsTheSame(oldItem: Seat, newItem: Seat) =
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
            setTextColor(ContextCompat.getColor(context, android.R.color.white))
        }
        return PaymentViewHolder(textView)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val seat: Seat = getItem(position)
        holder.textView.text = seat.objectDescription.toString()
    }
}
