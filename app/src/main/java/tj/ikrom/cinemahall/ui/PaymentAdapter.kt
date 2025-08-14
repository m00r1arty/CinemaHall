package tj.ikrom.cinemahall.ui

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tj.ikrom.cinemahall.data.database.entity.PaymentEntity

class PaymentAdapter : RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder>() {

    private val payments = mutableListOf<PaymentEntity>()

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
        val payment = payments[position]
        payment.seats.forEach { seat ->
            holder.textView.text = "Сумма: ${payment.totalPrice} Место: ${seat.objectDescription} "
        }
    }

    override fun getItemCount(): Int = payments.size

    fun submitList(list: List<PaymentEntity>) {
        payments.clear()
        payments.addAll(list)
        notifyDataSetChanged()
    }
}